package cache

import (
	"strconv"
	"sync"
)

// ConfigEntry holds a single configuration key-value pair.
type ConfigEntry struct {
	ConfigName string `json:"configName"`
	Value      string `json:"value"`
}

func (c *ConfigEntry) IntValue() int {
	v, _ := strconv.Atoi(c.Value)
	return v
}

func (c *ConfigEntry) Float64Value() float64 {
	v, _ := strconv.ParseFloat(c.Value, 64)
	return v
}

// ConfigManager holds a set of configuration entries with thread-safe access.
type ConfigManager struct {
	Configs map[string]*ConfigEntry `json:"configs"`
	mu      sync.RWMutex
	changed bool
}

func NewConfigManager() *ConfigManager {
	return &ConfigManager{
		Configs: make(map[string]*ConfigEntry),
	}
}

func (cm *ConfigManager) Set(key, value string) {
	cm.mu.Lock()
	defer cm.mu.Unlock()
	cm.Configs[key] = &ConfigEntry{ConfigName: key, Value: value}
	cm.changed = true
}

func (cm *ConfigManager) Get(key string) (string, bool) {
	cm.mu.RLock()
	defer cm.mu.RUnlock()
	e, ok := cm.Configs[key]
	if !ok {
		return "", false
	}
	return e.Value, true
}

func (cm *ConfigManager) GetInt(key string) int {
	cm.mu.RLock()
	defer cm.mu.RUnlock()
	e, ok := cm.Configs[key]
	if !ok {
		return 0
	}
	return e.IntValue()
}

func (cm *ConfigManager) GetFloat64(key string) float64 {
	cm.mu.RLock()
	defer cm.mu.RUnlock()
	e, ok := cm.Configs[key]
	if !ok {
		return 0
	}
	return e.Float64Value()
}

func (cm *ConfigManager) Threshold() float64 {
	return cm.GetFloat64("threshold")
}

func (cm *ConfigManager) NodeSize() int {
	return cm.GetInt("node_size")
}

func (cm *ConfigManager) Merge(other *ConfigManager) {
	if other == nil {
		return
	}
	cm.mu.Lock()
	defer cm.mu.Unlock()
	other.mu.RLock()
	defer other.mu.RUnlock()
	for k, v := range other.Configs {
		cm.Configs[k] = v
	}
}

func (cm *ConfigManager) IsChanged() bool {
	cm.mu.RLock()
	defer cm.mu.RUnlock()
	return cm.changed
}

func (cm *ConfigManager) ResetChanged() {
	cm.mu.Lock()
	defer cm.mu.Unlock()
	cm.changed = false
}
