package main

import (
	"context"
	"flag"
	"fmt"
	"log/slog"
	"os"
	"os/signal"
	"syscall"

	"github.com/riseger/riseger-go/internal/cache"
	"github.com/riseger/riseger-go/internal/compile"
	"github.com/riseger/riseger-go/internal/config"
	"github.com/riseger/riseger-go/internal/engine"
	"github.com/riseger/riseger-go/internal/server"
)

func main() {
	configPath := flag.String("config", "", "path to config.json")
	dataDir := flag.String("data", ".", "data directory")
	port := flag.Int("port", 12000, "server listen port")
	flag.Parse()

	var cfg *config.ServerConfig
	if *configPath != "" {
		var err error
		cfg, err = config.LoadFromFile(*configPath)
		if err != nil {
			fmt.Fprintf(os.Stderr, "Failed to load config: %v\n", err)
			os.Exit(1)
		}
	} else {
		cfg = config.DefaultConfig()
	}

	if *dataDir != "." {
		cfg.DataDir = *dataDir
	}
	if *port != 12000 {
		cfg.Port = *port
	}

	e, err := engine.New(cfg)
	if err != nil {
		fmt.Fprintf(os.Stderr, "Failed to create engine: %v\n", err)
		os.Exit(1)
	}
	if err := e.Start(); err != nil {
		fmt.Fprintf(os.Stderr, "Failed to start engine: %v\n", err)
		os.Exit(1)
	}

	compiler := compile.NewCompiler(e.Cache)
	compiler.SetPreloadHandler(e.Preload)
	compiler.SetCreateDatabaseHandler(func(name string) error {
		_, err := e.CreateDatabase(name)
		return err
	})
	compiler.SetCreateMapHandler(func(db *cache.Database, name string, nodeSize int, threshold float64) error {
		m := cache.NewGeoMap(name, nodeSize, threshold, db)
		db.AddMap(m)
		return e.Storage.WriteDatabase(db)
	})

	addr := fmt.Sprintf(":%d", cfg.Port)
	srv := server.New(addr, compiler, e.Logger)

	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()

	sigCh := make(chan os.Signal, 1)
	signal.Notify(sigCh, syscall.SIGINT, syscall.SIGTERM)

	go func() {
		<-sigCh
		slog.Info("shutting down...")
		cancel()
		srv.Stop()
		e.Stop()
	}()

	if err := srv.Start(ctx); err != nil {
		fmt.Fprintf(os.Stderr, "Server error: %v\n", err)
		os.Exit(1)
	}
}
