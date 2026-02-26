package config

import (
	"encoding/json"
	"fmt"
	"os"
)

// ServerConfig holds the top-level server configuration,
// replacing the Java config.xml with JSON for simplicity.
type ServerConfig struct {
	Port              int     `json:"port"`
	BlockingLogQueue  int     `json:"blockingLogQueue"`
	CorePoolSize      int     `json:"corePoolSize"`
	MaxPoolSize       int     `json:"maxPoolSize"`
	KeepAliveTimeSec  int     `json:"keepAliveTimeSec"`
	DefaultMemorySize int     `json:"defaultMemorySize"`
	DataDir           string  `json:"dataDir"`
	FlushThreshold    int     `json:"flushThreshold"`
	FlushIntervalSec  int     `json:"flushIntervalSec"`
}

func DefaultConfig() *ServerConfig {
	return &ServerConfig{
		Port:              12000,
		BlockingLogQueue:  125,
		CorePoolSize:      5,
		MaxPoolSize:       10,
		KeepAliveTimeSec:  60,
		DefaultMemorySize: 1024,
		DataDir:           ".",
		FlushThreshold:    125,
		FlushIntervalSec:  30,
	}
}

// LoadFromFile reads configuration from a JSON file.
func LoadFromFile(path string) (*ServerConfig, error) {
	data, err := os.ReadFile(path)
	if err != nil {
		return nil, fmt.Errorf("read config file %s: %w", path, err)
	}
	cfg := DefaultConfig()
	if err := json.Unmarshal(data, cfg); err != nil {
		return nil, fmt.Errorf("parse config file %s: %w", path, err)
	}
	return cfg, nil
}

// SaveToFile writes configuration to a JSON file.
func (c *ServerConfig) SaveToFile(path string) error {
	data, err := json.MarshalIndent(c, "", "  ")
	if err != nil {
		return err
	}
	return os.WriteFile(path, data, 0644)
}
