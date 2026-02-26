package storage

import (
	"encoding/json"
	"fmt"
	"log/slog"
	"os"
	"path/filepath"
	"strings"

	"github.com/riseger/riseger-go/internal/cache"
	"github.com/riseger/riseger-go/pkg/rtree"
)

// StorageManager handles persistence of databases, maps, layers, and R-tree
// indexes to the filesystem.
type StorageManager struct {
	RootPath string
	Logger   *slog.Logger
}

func NewStorageManager(rootPath string, logger *slog.Logger) *StorageManager {
	if logger == nil {
		logger = slog.Default()
	}
	return &StorageManager{RootPath: rootPath, Logger: logger}
}

func (s *StorageManager) databasesDir() string {
	return filepath.Join(s.RootPath, "data", "databases")
}

// ---------- load ----------

// LoadDatabases scans the data directory and loads all databases from disk.
func (s *StorageManager) LoadDatabases() ([]*cache.Database, error) {
	root := s.databasesDir()
	if _, err := os.Stat(root); os.IsNotExist(err) {
		return nil, nil
	}

	entries, err := os.ReadDir(root)
	if err != nil {
		return nil, fmt.Errorf("read databases dir: %w", err)
	}

	var databases []*cache.Database
	for _, e := range entries {
		if !e.IsDir() || !strings.HasSuffix(e.Name(), "."+cache.DatabasePrefix) {
			continue
		}
		db, err := s.loadDatabase(filepath.Join(root, e.Name()))
		if err != nil {
			s.Logger.Error("load database failed", "dir", e.Name(), "error", err)
			continue
		}
		databases = append(databases, db)
	}
	return databases, nil
}

func (s *StorageManager) loadDatabase(dbDir string) (*cache.Database, error) {
	name := strings.TrimSuffix(filepath.Base(dbDir), "."+cache.DatabasePrefix)
	db := cache.NewDatabase(name)

	entries, err := os.ReadDir(dbDir)
	if err != nil {
		return nil, fmt.Errorf("read db dir %s: %w", dbDir, err)
	}

	for _, e := range entries {
		path := filepath.Join(dbDir, e.Name())
		switch {
		case e.IsDir() && strings.HasSuffix(e.Name(), "."+cache.MapPrefix):
			geoMap, err := s.loadMap(path, db)
			if err != nil {
				s.Logger.Error("load map failed", "path", path, "error", err)
				continue
			}
			db.AddMap(geoMap)

		case strings.HasPrefix(e.Name(), cache.ConfigFileName):
			cfg, err := s.loadConfigManager(path)
			if err != nil {
				s.Logger.Error("load config failed", "path", path, "error", err)
				continue
			}
			db.Config.Merge(cfg)

		case strings.HasPrefix(e.Name(), cache.ModelFileName):
			models, err := s.loadModels(path)
			if err != nil {
				s.Logger.Error("load models failed", "path", path, "error", err)
				continue
			}
			for _, m := range models {
				db.AddModel(m)
			}
		}
	}
	db.ResetChanged()
	return db, nil
}

func (s *StorageManager) loadMap(mapDir string, db *cache.Database) (*cache.GeoMap, error) {
	name := strings.TrimSuffix(filepath.Base(mapDir), "."+cache.MapPrefix)

	entries, err := os.ReadDir(mapDir)
	if err != nil {
		return nil, fmt.Errorf("read map dir %s: %w", mapDir, err)
	}

	var cfg *cache.ConfigManager
	var layerFiles []os.DirEntry

	for _, e := range entries {
		if strings.HasPrefix(e.Name(), cache.ConfigFileName) {
			c, err := s.loadConfigManager(filepath.Join(mapDir, e.Name()))
			if err != nil {
				return nil, err
			}
			cfg = c
		} else if strings.HasSuffix(e.Name(), "."+cache.LayerPrefix) {
			layerFiles = append(layerFiles, e)
		}
	}

	if cfg == nil {
		cfg = cache.NewConfigManager()
		cfg.Set("node_size", "4")
		cfg.Set("threshold", "0.5")
	}

	geoMap := cache.NewGeoMapFromConfig(name, cfg, db)

	for _, lf := range layerFiles {
		path := filepath.Join(mapDir, lf.Name())
		layerName := strings.TrimSuffix(lf.Name(), "."+cache.LayerPrefix)

		if lf.IsDir() {
			layer := cache.NewLayer(layerName, geoMap.NodeSize(), geoMap.Threshold())
			if err := s.loadSubmapLayer(path, layer, geoMap); err != nil {
				s.Logger.Error("load submap layer failed", "path", path, "error", err)
				continue
			}
			geoMap.Layers[layerName] = layer
		} else {
			layer, err := s.loadElementLayer(path, layerName, geoMap.NodeSize(), geoMap.Threshold())
			if err != nil {
				s.Logger.Error("load element layer failed", "path", path, "error", err)
				continue
			}
			geoMap.Layers[layerName] = layer
		}
	}

	geoMap.ResetChanged()
	return geoMap, nil
}

func (s *StorageManager) loadElementLayer(path string, name string, nodeSize int, threshold float64) (*cache.Layer, error) {
	data, err := os.ReadFile(path)
	if err != nil {
		return nil, fmt.Errorf("read layer file %s: %w", path, err)
	}

	ser := &elementSerializer{}
	tree, err := rtree.DeserializeRStarTree[rtree.Rectangle](data, ser)
	if err != nil {
		return nil, fmt.Errorf("deserialize layer %s: %w", path, err)
	}

	layer := &cache.Layer{
		Name:  name,
		Index: tree,
	}
	return layer, nil
}

func (s *StorageManager) loadSubmapLayer(dir string, layer *cache.Layer, parent *cache.GeoMap) error {
	entries, err := os.ReadDir(dir)
	if err != nil {
		return fmt.Errorf("read submap dir %s: %w", dir, err)
	}
	for _, e := range entries {
		if e.IsDir() && strings.HasSuffix(e.Name(), "."+cache.SubmapPrefix) {
			smpDir := filepath.Join(dir, e.Name())
			subMap, err := s.loadMap(smpDir, parent.Database)
			if err != nil {
				s.Logger.Error("load submap failed", "path", smpDir, "error", err)
				continue
			}
			layer.AddElement(subMap)
		}
	}
	return nil
}

// ---------- save ----------

// WriteDatabase persists a full database to disk.
func (s *StorageManager) WriteDatabase(db *cache.Database) error {
	root := s.databasesDir()
	if err := os.MkdirAll(root, 0755); err != nil {
		return fmt.Errorf("create databases dir: %w", err)
	}

	dbDir := filepath.Join(root, db.Name+"."+cache.DatabasePrefix)
	if err := os.MkdirAll(dbDir, 0755); err != nil {
		return fmt.Errorf("create db dir: %w", err)
	}

	if err := s.writeConfigManager(dbDir, db.Config); err != nil {
		return err
	}

	if err := s.writeModels(dbDir, db.ListModels()); err != nil {
		return err
	}

	for _, m := range db.ListMaps() {
		if err := s.WriteMap(dbDir, m); err != nil {
			return err
		}
	}

	s.Logger.Info("database written", "name", db.Name)
	return nil
}

// WriteMap persists a map directory.
func (s *StorageManager) WriteMap(parentDir string, m *cache.GeoMap) error {
	mapDir := filepath.Join(parentDir, m.Name+"."+cache.MapPrefix)
	if err := os.MkdirAll(mapDir, 0755); err != nil {
		return fmt.Errorf("create map dir: %w", err)
	}

	if err := s.writeConfigManager(mapDir, m.Config); err != nil {
		return err
	}

	for _, layer := range m.ListLayers() {
		if err := s.writeLayer(mapDir, layer); err != nil {
			return err
		}
	}
	return nil
}

func (s *StorageManager) writeLayer(parentDir string, layer *cache.Layer) error {
	if layer.IsSubMap() {
		layerDir := filepath.Join(parentDir, layer.Name+"."+cache.LayerPrefix)
		if err := os.MkdirAll(layerDir, 0755); err != nil {
			return fmt.Errorf("create submap layer dir: %w", err)
		}
		for _, elem := range layer.Elements() {
			if subMap, ok := elem.(*cache.GeoMap); ok {
				smpDir := filepath.Join(layerDir, subMap.Name+"."+cache.SubmapPrefix)
				if err := os.MkdirAll(smpDir, 0755); err != nil {
					return err
				}
				if err := s.WriteMap(smpDir, subMap); err != nil {
					return err
				}
			}
		}
		return nil
	}

	ser := &elementSerializer{}
	data, err := rtree.SerializeToBytes[rtree.Rectangle](&layer.Index.RTree, "rstar", ser)
	if err != nil {
		return fmt.Errorf("serialize layer %s: %w", layer.Name, err)
	}

	layerFile := filepath.Join(parentDir, layer.Name+"."+cache.LayerPrefix)
	if err := os.WriteFile(layerFile, data, 0644); err != nil {
		return fmt.Errorf("write layer file %s: %w", layerFile, err)
	}
	return nil
}

// OrganizeDatabases incrementally persists only changed databases.
func (s *StorageManager) OrganizeDatabases(databases []*cache.Database) error {
	for _, db := range databases {
		if db.IsChanged() {
			if err := s.WriteDatabase(db); err != nil {
				return err
			}
			db.ResetChanged()
		}
	}
	return nil
}

// ---------- config/model I/O ----------

// configJSON matches the Java JSON format:
// { "configs": {...}, "entity": { "isChanged": false } }
type configJSON struct {
	Configs map[string]*cache.ConfigEntry `json:"configs"`
	Entity  entityJSON                    `json:"entity"`
}

type entityJSON struct {
	IsChanged bool `json:"isChanged"`
}

func (s *StorageManager) loadConfigManager(path string) (*cache.ConfigManager, error) {
	data, err := os.ReadFile(path)
	if err != nil {
		return nil, fmt.Errorf("read config %s: %w", path, err)
	}
	var cj configJSON
	if err := json.Unmarshal(data, &cj); err != nil {
		return nil, fmt.Errorf("parse config %s: %w", path, err)
	}
	cm := cache.NewConfigManager()
	for k, v := range cj.Configs {
		cm.Set(k, v.Value)
	}
	return cm, nil
}

func (s *StorageManager) writeConfigManager(dir string, cm *cache.ConfigManager) error {
	cj := configJSON{
		Configs: cm.Configs,
		Entity:  entityJSON{IsChanged: false},
	}
	data, err := json.MarshalIndent(cj, "", "  ")
	if err != nil {
		return fmt.Errorf("marshal config: %w", err)
	}
	path := filepath.Join(dir, cache.ConfigFileName+cache.DotPrefix+cache.JSONSuffix)
	return os.WriteFile(path, data, 0644)
}

// modelFileJSON matches Java: { "entity": {...}, "models": { "name": {...} } }
type modelFileJSON struct {
	Entity entityJSON                 `json:"entity"`
	Models map[string]cache.ModelJSON `json:"models"`
}

func (s *StorageManager) loadModels(path string) ([]*cache.Model, error) {
	data, err := os.ReadFile(path)
	if err != nil {
		return nil, fmt.Errorf("read models %s: %w", path, err)
	}
	var mf modelFileJSON
	if err := json.Unmarshal(data, &mf); err != nil {
		return nil, fmt.Errorf("parse models %s: %w", path, err)
	}
	models := make([]*cache.Model, 0, len(mf.Models))
	for _, mj := range mf.Models {
		models = append(models, cache.ModelFromJSON(mj))
	}
	return models, nil
}

func (s *StorageManager) writeModels(dir string, models []*cache.Model) error {
	mf := modelFileJSON{
		Entity: entityJSON{IsChanged: false},
		Models: make(map[string]cache.ModelJSON, len(models)),
	}
	for _, m := range models {
		mf.Models[m.Name] = m.ToJSON()
	}
	data, err := json.MarshalIndent(mf, "", "  ")
	if err != nil {
		return fmt.Errorf("marshal models: %w", err)
	}
	path := filepath.Join(dir, cache.ModelFileName+cache.DotPrefix+cache.JSONSuffix)
	return os.WriteFile(path, data, 0644)
}

// ---------- log file access ----------

// GetLogFiles returns sorted log files for a database directory.
func (s *StorageManager) GetLogFiles(dbName string) ([]string, error) {
	logDir := filepath.Join(s.databasesDir(), dbName+"."+cache.DatabasePrefix, "logs")
	if _, err := os.Stat(logDir); os.IsNotExist(err) {
		return nil, nil
	}
	entries, err := os.ReadDir(logDir)
	if err != nil {
		return nil, fmt.Errorf("read log dir: %w", err)
	}
	var files []string
	for _, e := range entries {
		if !e.IsDir() && strings.HasSuffix(e.Name(), ".log") {
			files = append(files, filepath.Join(logDir, e.Name()))
		}
	}
	return files, nil
}
