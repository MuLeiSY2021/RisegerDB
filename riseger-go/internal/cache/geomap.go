package cache

import (
	"fmt"
	"sync"

	"github.com/riseger/riseger-go/pkg/rtree"
)

// GeoMap represents a geographic map containing layers.
// It is itself a Rectangle so it can be stored in a parent layer's R-tree
// (as a submap entry).
type GeoMap struct {
	rtree.Rect
	Name     string
	Config   *ConfigManager
	Layers   map[string]*Layer
	Database *Database // back-reference, not serialized
	changed  bool
	mu       sync.RWMutex
}

func NewGeoMap(name string, nodeSize int, threshold float64, db *Database) *GeoMap {
	cfg := NewConfigManager()
	cfg.Set("node_size", fmt.Sprintf("%d", nodeSize))
	cfg.Set("threshold", fmt.Sprintf("%g", threshold))
	return &GeoMap{
		Rect:     *rtree.NewEmptyRect(threshold),
		Name:     name,
		Config:   cfg,
		Layers:   make(map[string]*Layer),
		Database: db,
	}
}

func NewGeoMapFromConfig(name string, cfg *ConfigManager, db *Database) *GeoMap {
	threshold := cfg.Threshold()
	return &GeoMap{
		Rect:     *rtree.NewEmptyRect(threshold),
		Name:     name,
		Config:   cfg,
		Layers:   make(map[string]*Layer),
		Database: db,
	}
}

func (m *GeoMap) NodeSize() int {
	return m.Config.NodeSize()
}

func (m *GeoMap) Threshold() float64 {
	return m.Config.Threshold()
}

// GetOrCreateLayer returns the layer with the given name, creating it if needed.
func (m *GeoMap) GetOrCreateLayer(name string) *Layer {
	m.mu.Lock()
	defer m.mu.Unlock()
	if l, ok := m.Layers[name]; ok {
		return l
	}
	l := NewLayer(name, m.NodeSize(), m.Threshold())
	m.Layers[name] = l
	return l
}

func (m *GeoMap) GetLayer(name string) (*Layer, bool) {
	m.mu.RLock()
	defer m.mu.RUnlock()
	l, ok := m.Layers[name]
	return l, ok
}

func (m *GeoMap) ElementLayerName(modelName string) string {
	return ModelPrefix + "_" + modelName
}

func (m *GeoMap) SubmapLayerName(scopeName string) string {
	return SubmapPrefix + "_" + scopeName
}

// AddElement adds an element to the appropriate model layer.
func (m *GeoMap) AddElement(elem *Element) {
	layerName := m.ElementLayerName(elem.ModelName)
	layer := m.GetOrCreateLayer(layerName)
	layer.AddElement(elem)
	m.mu.Lock()
	m.Expand(elem)
	m.changed = true
	m.mu.Unlock()
}

func (m *GeoMap) ListLayers() []*Layer {
	m.mu.RLock()
	defer m.mu.RUnlock()
	layers := make([]*Layer, 0, len(m.Layers))
	for _, l := range m.Layers {
		layers = append(layers, l)
	}
	return layers
}

func (m *GeoMap) IsChanged() bool {
	m.mu.RLock()
	defer m.mu.RUnlock()
	return m.changed
}

func (m *GeoMap) ResetChanged() {
	m.mu.Lock()
	defer m.mu.Unlock()
	m.changed = false
}
