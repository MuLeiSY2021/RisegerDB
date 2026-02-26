package cache

import (
	"sync"

	"github.com/riseger/riseger-go/pkg/rtree"
)

// Element is a spatial data entity stored in the R-tree index.
// It extends Rect (spatial bounding box) with typed attributes.
type Element struct {
	rtree.Rect
	ParentModel string                 `json:"parentModel"`
	ModelName   string                 `json:"modelName"`
	Attributes  map[string]interface{} `json:"attributes"`
	mu          sync.RWMutex
}

func NewElement(minX, minY, maxX, maxY, threshold float64, parentModel, modelName string) *Element {
	return &Element{
		Rect:        *rtree.NewRect(minX, minY, maxX, maxY, threshold),
		ParentModel: parentModel,
		ModelName:   modelName,
		Attributes:  make(map[string]interface{}),
	}
}

func (e *Element) GetAttribute(key string) (interface{}, bool) {
	e.mu.RLock()
	defer e.mu.RUnlock()
	v, ok := e.Attributes[key]
	return v, ok
}

func (e *Element) SetAttribute(key string, value interface{}) {
	e.mu.Lock()
	defer e.mu.Unlock()
	e.Attributes[key] = value
}
