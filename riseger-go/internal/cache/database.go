package cache

import (
	"sync"
)

// Database is the top-level entity holding maps, models, and configuration.
type Database struct {
	Name    string
	Status  Status
	Maps    map[string]*GeoMap
	Models  map[string]*Model
	Config  *ConfigManager
	changed bool
	mu      sync.RWMutex
}

func NewDatabase(name string) *Database {
	return &Database{
		Name:   name,
		Status: StatusLoading,
		Maps:   make(map[string]*GeoMap),
		Models: make(map[string]*Model),
		Config: NewConfigManager(),
	}
}

func (db *Database) Activate() {
	db.mu.Lock()
	defer db.mu.Unlock()
	db.Status = StatusActive
}

func (db *Database) AddModel(m *Model) {
	db.mu.Lock()
	defer db.mu.Unlock()
	db.Models[m.Name] = m
	db.changed = true
}

func (db *Database) GetModel(name string) (*Model, bool) {
	db.mu.RLock()
	defer db.mu.RUnlock()
	m, ok := db.Models[name]
	return m, ok
}

func (db *Database) ListModels() []*Model {
	db.mu.RLock()
	defer db.mu.RUnlock()
	models := make([]*Model, 0, len(db.Models))
	for _, m := range db.Models {
		models = append(models, m)
	}
	return models
}

func (db *Database) AddMap(m *GeoMap) {
	db.mu.Lock()
	defer db.mu.Unlock()
	db.Maps[m.Name] = m
	db.changed = true
}

func (db *Database) GetMap(name string) (*GeoMap, bool) {
	db.mu.RLock()
	defer db.mu.RUnlock()
	m, ok := db.Maps[name]
	return m, ok
}

func (db *Database) ListMaps() []*GeoMap {
	db.mu.RLock()
	defer db.mu.RUnlock()
	maps := make([]*GeoMap, 0, len(db.Maps))
	for _, m := range db.Maps {
		maps = append(maps, m)
	}
	return maps
}

func (db *Database) Threshold() float64 {
	return db.Config.Threshold()
}

func (db *Database) IsChanged() bool {
	db.mu.RLock()
	defer db.mu.RUnlock()
	return db.changed
}

func (db *Database) ResetChanged() {
	db.mu.Lock()
	defer db.mu.Unlock()
	db.changed = false
}
