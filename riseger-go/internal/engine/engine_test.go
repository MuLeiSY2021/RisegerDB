package engine

import (
	"testing"

	"github.com/riseger/riseger-go/internal/cache"
	cfgpkg "github.com/riseger/riseger-go/internal/config"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func testConfig(dir string) *cfgpkg.ServerConfig {
	cfg := cfgpkg.DefaultConfig()
	cfg.DataDir = dir
	cfg.FlushThreshold = 1000
	cfg.FlushIntervalSec = 3600
	return cfg
}

func TestEngineStartStop(t *testing.T) {
	root := t.TempDir()
	cfg := testConfig(root)

	e, err := New(cfg)
	require.NoError(t, err)

	err = e.Start()
	require.NoError(t, err)
	assert.Equal(t, 0, e.Cache.Size())

	err = e.Stop()
	require.NoError(t, err)
}

func TestEngineCreateDatabase(t *testing.T) {
	root := t.TempDir()
	cfg := testConfig(root)

	e, err := New(cfg)
	require.NoError(t, err)
	require.NoError(t, e.Start())
	defer e.Stop()

	db, err := e.CreateDatabase("my_db")
	require.NoError(t, err)
	assert.Equal(t, "my_db", db.Name)
	assert.Equal(t, cache.StatusActive, db.Status)

	got, ok := e.GetDatabase("my_db")
	require.True(t, ok)
	assert.Equal(t, "my_db", got.Name)

	_, err = e.CreateDatabase("my_db")
	assert.Error(t, err)
}

func TestEngineLoadPersistedDatabase(t *testing.T) {
	root := t.TempDir()
	cfg := testConfig(root)

	e1, err := New(cfg)
	require.NoError(t, err)
	require.NoError(t, e1.Start())

	db, err := e1.CreateDatabase("persist_test")
	require.NoError(t, err)
	db.AddModel(cache.NewModel("building", "field", map[string]cache.FieldType{
		"name": cache.FieldString,
	}))
	require.NoError(t, e1.Storage.WriteDatabase(db))
	db.ResetChanged()
	require.NoError(t, e1.Stop())

	e2, err := New(cfg)
	require.NoError(t, err)
	require.NoError(t, e2.Start())

	got, ok := e2.GetDatabase("persist_test")
	require.True(t, ok)
	assert.Equal(t, cache.StatusActive, got.Status)

	m, ok := got.GetModel("building")
	require.True(t, ok)
	assert.Equal(t, "field", m.Parent)

	require.NoError(t, e2.Stop())
}

func TestEngineWithMapsAndElements(t *testing.T) {
	root := t.TempDir()
	cfg := testConfig(root)

	e, err := New(cfg)
	require.NoError(t, err)
	require.NoError(t, e.Start())

	db, err := e.CreateDatabase("geo_test")
	require.NoError(t, err)

	geoMap := cache.NewGeoMap("world", 4, 0.5, db)
	for i := 0; i < 10; i++ {
		x := float64(i * 10)
		elem := cache.NewElement(x, x, x+5, x+5, 0.5, "point", "city")
		elem.SetAttribute("name", "city")
		geoMap.AddElement(elem)
	}
	db.AddMap(geoMap)

	require.NoError(t, e.Storage.WriteDatabase(db))
	db.ResetChanged()
	require.NoError(t, e.Stop())

	e2, err := New(cfg)
	require.NoError(t, err)
	require.NoError(t, e2.Start())

	got, ok := e2.GetDatabase("geo_test")
	require.True(t, ok)
	maps := got.ListMaps()
	assert.Equal(t, 1, len(maps))
	assert.Equal(t, "world", maps[0].Name)

	layers := maps[0].ListLayers()
	assert.GreaterOrEqual(t, len(layers), 1)

	require.NoError(t, e2.Stop())
}
