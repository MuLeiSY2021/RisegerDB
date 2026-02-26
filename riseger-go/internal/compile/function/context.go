package function

import (
	"github.com/riseger/riseger-go/internal/cache"
	"github.com/riseger/riseger-go/pkg/rtree"
)

// PreloadFunc is a callback that the engine registers so the executor
// can trigger an async geodata import without depending on the engine package.
type PreloadFunc func(path string) (taskID string, err error)

// CreateDatabaseFunc creates a new empty database.
type CreateDatabaseFunc func(name string) error

// CreateMapFunc creates a new map in the current database.
type CreateMapFunc func(db *cache.Database, name string, nodeSize int, threshold float64) error

// Context holds the execution state during query processing.
// It replaces the Java SearchMemory + CommandList + SearchSession.
type Context struct {
	Database  *cache.Database
	Map       *cache.GeoMap
	Models    []string
	Scope     rtree.Rectangle
	Threshold float64

	Stack          []interface{}
	Result         *ResultSet
	Preload        PreloadFunc
	CreateDatabase CreateDatabaseFunc
	CreateMap      CreateMapFunc
}

func NewContext() *Context {
	return &Context{
		Stack: make([]interface{}, 0, 64),
	}
}

func (c *Context) Push(v interface{}) {
	c.Stack = append(c.Stack, v)
}

func (c *Context) Pop() interface{} {
	if len(c.Stack) == 0 {
		return nil
	}
	v := c.Stack[len(c.Stack)-1]
	c.Stack = c.Stack[:len(c.Stack)-1]
	return v
}

func (c *Context) PopFloat() float64 {
	v := c.Pop()
	switch x := v.(type) {
	case float64:
		return x
	case int:
		return float64(x)
	case int64:
		return float64(x)
	default:
		return 0
	}
}

func (c *Context) PopBool() bool {
	v := c.Pop()
	if b, ok := v.(bool); ok {
		return b
	}
	return false
}

func (c *Context) PopString() string {
	v := c.Pop()
	if s, ok := v.(string); ok {
		return s
	}
	return ""
}

// ResultSet collects query results.
type ResultSet struct {
	Columns []string
	Rows    []map[string]interface{}
}

func NewResultSet() *ResultSet {
	return &ResultSet{}
}

func (r *ResultSet) AddRow(row map[string]interface{}) {
	r.Rows = append(r.Rows, row)
}

func EmptyResultSet() *ResultSet {
	return &ResultSet{}
}
