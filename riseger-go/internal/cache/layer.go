package cache

import (
	"strings"
	"sync"

	"github.com/riseger/riseger-go/pkg/rtree"
)

// Layer groups elements indexed by an R*-tree.
// A layer whose name starts with "smp" is a submap layer (contains GeoMaps),
// otherwise it is a model layer (contains Elements).
type Layer struct {
	Name    string
	Index   *rtree.RStarTree[rtree.Rectangle]
	changed bool
	mu      sync.RWMutex
}

func NewLayer(name string, nodeSize int, threshold float64) *Layer {
	return &Layer{
		Name:  name,
		Index: rtree.NewRStarTree[rtree.Rectangle](nodeSize, threshold),
	}
}

func (l *Layer) IsSubMap() bool {
	return strings.HasPrefix(l.Name, SubmapPrefix)
}

func (l *Layer) IsModel() bool {
	return strings.HasPrefix(l.Name, ModelPrefix)
}

func (l *Layer) AddElement(elem rtree.Rectangle) {
	l.mu.Lock()
	defer l.mu.Unlock()
	l.Index.Insert(elem)
	l.changed = true
}

func (l *Layer) Search(scope rtree.Rectangle) []rtree.Rectangle {
	l.mu.RLock()
	defer l.mu.RUnlock()
	return l.Index.Search(scope)
}

func (l *Layer) Elements() []rtree.Rectangle {
	l.mu.RLock()
	defer l.mu.RUnlock()
	return l.Index.Elements()
}

func (l *Layer) DeleteStrict(rect rtree.Rectangle) int {
	l.mu.Lock()
	defer l.mu.Unlock()
	n := l.Index.DeleteStrict(rect)
	if n > 0 {
		l.changed = true
	}
	return n
}

func (l *Layer) IsChanged() bool {
	l.mu.RLock()
	defer l.mu.RUnlock()
	return l.changed
}

func (l *Layer) ResetChanged() {
	l.mu.Lock()
	defer l.mu.Unlock()
	l.changed = false
}
