package storage

import (
	"os"
	"path/filepath"
	"testing"

	"github.com/riseger/riseger-go/internal/cache"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func setupTestDir(t *testing.T) string {
	t.Helper()
	dir := t.TempDir()
	return dir
}

func TestWriteAndLoadDatabase(t *testing.T) {
	root := setupTestDir(t)
	sm := NewStorageManager(root, nil)

	db := cache.NewDatabase("test_db")
	db.Config.Set("threshold", "0.5")
	db.Config.Set("node_size", "4")

	m := cache.NewModel("building", "field", map[string]cache.FieldType{
		"name": cache.FieldString,
		"area": cache.FieldDouble,
	})
	db.AddModel(m)

	geoMap := cache.NewGeoMap("china", 4, 0.5, db)
	elem := cache.NewElement(10, 10, 20, 20, 0.5, "point", "building")
	elem.SetAttribute("name", "Tower")
	geoMap.AddElement(elem)
	db.AddMap(geoMap)

	err := sm.WriteDatabase(db)
	require.NoError(t, err)

	dbDir := filepath.Join(root, "data", "databases", "test_db.db")
	assert.DirExists(t, dbDir)
	assert.FileExists(t, filepath.Join(dbDir, "config.json"))
	assert.FileExists(t, filepath.Join(dbDir, "model.json"))
	assert.DirExists(t, filepath.Join(dbDir, "china.mp"))

	loaded, err := sm.LoadDatabases()
	require.NoError(t, err)
	require.Equal(t, 1, len(loaded))

	ldb := loaded[0]
	assert.Equal(t, "test_db", ldb.Name)

	lm, ok := ldb.GetModel("building")
	require.True(t, ok)
	assert.Equal(t, "building", lm.Name)
	assert.Equal(t, "field", lm.Parent)

	maps := ldb.ListMaps()
	assert.Equal(t, 1, len(maps))
	assert.Equal(t, "china", maps[0].Name)
}

func TestWriteAndLoadConfig(t *testing.T) {
	root := setupTestDir(t)
	sm := NewStorageManager(root, nil)

	db := cache.NewDatabase("cfg_test")
	db.Config.Set("port", "12000")
	db.Config.Set("threshold", "0.5")

	err := sm.WriteDatabase(db)
	require.NoError(t, err)

	loaded, err := sm.LoadDatabases()
	require.NoError(t, err)
	require.Equal(t, 1, len(loaded))

	v, ok := loaded[0].Config.Get("port")
	assert.True(t, ok)
	assert.Equal(t, "12000", v)
}

func TestWriteAndLoadModels(t *testing.T) {
	root := setupTestDir(t)
	sm := NewStorageManager(root, nil)

	db := cache.NewDatabase("model_test")
	db.AddModel(cache.NewModel("m1", "point", map[string]cache.FieldType{"x": cache.FieldDouble}))
	db.AddModel(cache.NewModel("m2", "field", map[string]cache.FieldType{"name": cache.FieldString}))

	err := sm.WriteDatabase(db)
	require.NoError(t, err)

	loaded, err := sm.LoadDatabases()
	require.NoError(t, err)
	require.Equal(t, 1, len(loaded))

	m1, ok := loaded[0].GetModel("m1")
	require.True(t, ok)
	assert.Equal(t, "point", m1.Parent)

	m2, ok := loaded[0].GetModel("m2")
	require.True(t, ok)
	assert.Equal(t, "field", m2.Parent)
}

func TestLoadEmptyDir(t *testing.T) {
	root := setupTestDir(t)
	sm := NewStorageManager(root, nil)

	loaded, err := sm.LoadDatabases()
	require.NoError(t, err)
	assert.Empty(t, loaded)
}

func TestOrganizeDatabases(t *testing.T) {
	root := setupTestDir(t)
	sm := NewStorageManager(root, nil)

	db := cache.NewDatabase("org_test")
	db.AddModel(cache.NewModel("m1", "", map[string]cache.FieldType{}))

	err := sm.WriteDatabase(db)
	require.NoError(t, err)
	db.ResetChanged()

	db.AddModel(cache.NewModel("m2", "", map[string]cache.FieldType{}))
	assert.True(t, db.IsChanged())

	err = sm.OrganizeDatabases([]*cache.Database{db})
	require.NoError(t, err)
	assert.False(t, db.IsChanged())
}

func TestGetLogFiles(t *testing.T) {
	root := setupTestDir(t)
	sm := NewStorageManager(root, nil)

	db := cache.NewDatabase("log_test")
	err := sm.WriteDatabase(db)
	require.NoError(t, err)

	logDir := filepath.Join(root, "data", "databases", "log_test.db", "logs")
	os.MkdirAll(logDir, 0755)
	os.WriteFile(filepath.Join(logDir, "1000_1.log"), []byte("test"), 0644)
	os.WriteFile(filepath.Join(logDir, "2000_2.log"), []byte("test"), 0644)

	files, err := sm.GetLogFiles("log_test")
	require.NoError(t, err)
	assert.Equal(t, 2, len(files))
}
