package engine

import (
	"fmt"
	"log/slog"
	"os"
	"time"

	"github.com/riseger/riseger-go/internal/cache"
	"github.com/riseger/riseger-go/internal/config"
	"github.com/riseger/riseger-go/internal/storage"
	"github.com/riseger/riseger-go/internal/wal"
)

// Engine is the core database engine, orchestrating the cache, storage,
// and WAL subsystems.
type Engine struct {
	Config     *config.ServerConfig
	Cache      *cache.CacheManager
	Storage    *storage.StorageManager
	WAL        *wal.Manager
	Logger     *slog.Logger
	preloadMgr *PreloadManager
}

// New creates and initializes the engine from a config.
func New(cfg *config.ServerConfig) (*Engine, error) {
	logger := slog.New(slog.NewTextHandler(os.Stdout, &slog.HandlerOptions{
		Level: slog.LevelInfo,
	}))

	e := &Engine{
		Config:     cfg,
		Cache:      cache.NewCacheManager(),
		Storage:    storage.NewStorageManager(cfg.DataDir, logger),
		Logger:     logger,
		preloadMgr: NewPreloadManager(),
	}

	e.WAL = wal.NewManager(
		cfg.DataDir,
		cfg.FlushThreshold,
		time.Duration(cfg.FlushIntervalSec)*time.Second,
		e.flushCallback,
		logger,
	)

	return e, nil
}

// Start performs the full initialization sequence:
// 1. Load databases from disk
// 2. Populate the cache
// 3. Replay WAL logs for crash recovery
// 4. Start the WAL daemon
func (e *Engine) Start() error {
	e.Logger.Info("engine starting", "dataDir", e.Config.DataDir)

	databases, err := e.Storage.LoadDatabases()
	if err != nil {
		return fmt.Errorf("load databases: %w", err)
	}
	for _, db := range databases {
		db.Activate()
		e.Cache.AddDatabase(db)
		e.Logger.Info("database loaded", "name", db.Name)
	}

	if err := e.recoverWAL(); err != nil {
		e.Logger.Error("WAL recovery failed", "error", err)
	}

	e.WAL.Start()
	e.Logger.Info("engine started",
		"databases", e.Cache.Size(),
		"port", e.Config.Port,
	)
	return nil
}

// Stop gracefully shuts down the engine: stops the WAL daemon,
// persists all databases.
func (e *Engine) Stop() error {
	e.Logger.Info("engine stopping")
	e.WAL.Stop()

	if err := e.Storage.OrganizeDatabases(e.Cache.ListDatabases()); err != nil {
		e.Logger.Error("final persist failed", "error", err)
		return err
	}

	e.Logger.Info("engine stopped")
	return nil
}

// GetDatabase retrieves a database by name from the cache.
func (e *Engine) GetDatabase(name string) (*cache.Database, bool) {
	return e.Cache.GetDatabase(name)
}

// CreateDatabase creates a new empty database.
func (e *Engine) CreateDatabase(name string) (*cache.Database, error) {
	if _, ok := e.Cache.GetDatabase(name); ok {
		return nil, fmt.Errorf("database %q already exists", name)
	}
	db := cache.NewDatabase(name)
	db.Activate()
	e.Cache.AddDatabase(db)

	if err := e.Storage.WriteDatabase(db); err != nil {
		return nil, fmt.Errorf("persist new database: %w", err)
	}
	e.Logger.Info("database created", "name", name)
	return db, nil
}

// recoverWAL replays all WAL entries for all loaded databases.
func (e *Engine) recoverWAL() error {
	for _, dbName := range e.Cache.ListDatabaseNames() {
		entries, err := e.WAL.Recover(dbName)
		if err != nil {
			return fmt.Errorf("recover WAL for %s: %w", dbName, err)
		}
		if len(entries) == 0 {
			continue
		}
		e.Logger.Info("replaying WAL", "database", dbName, "entries", len(entries))
		for _, entry := range entries {
			e.replayEntry(entry)
		}
		if err := e.WAL.DeleteLogs(dbName); err != nil {
			e.Logger.Error("delete WAL after recovery", "database", dbName, "error", err)
		}
	}
	return nil
}

func (e *Engine) replayEntry(entry wal.LogEntry) {
	db, ok := e.Cache.GetDatabase(entry.DatabaseName)
	if !ok {
		e.Logger.Warn("WAL replay: database not found", "database", entry.DatabaseName)
		return
	}
	for _, op := range entry.Operations {
		switch op.Type {
		case "INSERT":
			geoMap, ok := db.GetMap(op.MapName)
			if !ok {
				e.Logger.Warn("WAL replay: map not found", "map", op.MapName)
				continue
			}
			elem := cache.NewElement(0, 0, 0, 0, geoMap.Threshold(), "", op.ModelName)
			for k, v := range op.Attributes {
				elem.SetAttribute(k, v)
			}
			geoMap.AddElement(elem)
			e.Logger.Debug("WAL replay: inserted element", "map", op.MapName, "model", op.ModelName)
		case "DELETE":
			// future: implement delete replay
		default:
			e.Logger.Warn("WAL replay: unknown operation", "type", op.Type)
		}
	}
}

// flushCallback is called by the WAL daemon to persist databases and clear logs.
func (e *Engine) flushCallback() error {
	dbs := e.Cache.ListDatabases()
	if err := e.Storage.OrganizeDatabases(dbs); err != nil {
		return err
	}
	return e.WAL.DeleteAllLogs()
}
