package cache

import (
	"fmt"
	"sync"
)

// Model defines a schema for elements, mapping parameter names to types.
type Model struct {
	Name       string               `json:"name"`
	Parent     string               `json:"parent,omitempty"`
	Parameters map[string]FieldType `json:"parameters"`
	mu         sync.RWMutex
}

func NewModel(name, parent string, params map[string]FieldType) *Model {
	m := &Model{
		Name:       name,
		Parent:     parent,
		Parameters: make(map[string]FieldType),
	}
	for k, v := range params {
		m.Parameters[k] = v
	}
	return m
}

func (m *Model) AddParameter(name string, typ FieldType) {
	m.mu.Lock()
	defer m.mu.Unlock()
	m.Parameters[name] = typ
}

func (m *Model) GetType(name string) (FieldType, bool) {
	m.mu.RLock()
	defer m.mu.RUnlock()
	t, ok := m.Parameters[name]
	return t, ok
}

func (m *Model) IsKey(name string) bool {
	m.mu.RLock()
	defer m.mu.RUnlock()
	t, ok := m.Parameters[name]
	return ok && t.IsKey()
}

func (m *Model) Validate(key string, value interface{}) error {
	m.mu.RLock()
	defer m.mu.RUnlock()
	_, ok := m.Parameters[key]
	if !ok {
		return fmt.Errorf("unknown parameter %q in model %q", key, m.Name)
	}
	return nil
}

// ModelJSON is the JSON-serializable representation matching the Java format.
type ModelJSON struct {
	Name       string            `json:"name"`
	Parent     string            `json:"parent,omitempty"`
	Parameters map[string]string `json:"parameters"`
}

func (m *Model) ToJSON() ModelJSON {
	m.mu.RLock()
	defer m.mu.RUnlock()
	params := make(map[string]string, len(m.Parameters))
	for k, v := range m.Parameters {
		params[k] = v.String()
	}
	return ModelJSON{Name: m.Name, Parent: m.Parent, Parameters: params}
}

func ModelFromJSON(j ModelJSON) *Model {
	params := make(map[string]FieldType, len(j.Parameters))
	for k, v := range j.Parameters {
		params[k] = ParseFieldType(v)
	}
	return NewModel(j.Name, j.Parent, params)
}
