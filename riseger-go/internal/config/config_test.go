package config

import (
	"path/filepath"
	"testing"

	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func TestDefaultConfig(t *testing.T) {
	cfg := DefaultConfig()
	assert.Equal(t, 12000, cfg.Port)
	assert.Equal(t, 125, cfg.BlockingLogQueue)
	assert.Equal(t, 5, cfg.CorePoolSize)
	assert.Equal(t, 10, cfg.MaxPoolSize)
	assert.Equal(t, 60, cfg.KeepAliveTimeSec)
}

func TestSaveAndLoad(t *testing.T) {
	dir := t.TempDir()
	path := filepath.Join(dir, "config.json")

	cfg := DefaultConfig()
	cfg.Port = 9999
	cfg.DataDir = "/data/test"

	err := cfg.SaveToFile(path)
	require.NoError(t, err)

	loaded, err := LoadFromFile(path)
	require.NoError(t, err)
	assert.Equal(t, 9999, loaded.Port)
	assert.Equal(t, "/data/test", loaded.DataDir)
	assert.Equal(t, 125, loaded.BlockingLogQueue)
}

func TestLoadNonExistent(t *testing.T) {
	_, err := LoadFromFile("/nonexistent/config.json")
	assert.Error(t, err)
}
