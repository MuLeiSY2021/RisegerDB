package cache

import (
	"testing"

	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func TestConfigManager(t *testing.T) {
	cm := NewConfigManager()
	cm.Set("threshold", "0.5")
	cm.Set("node_size", "4")

	assert.Equal(t, 0.5, cm.Threshold())
	assert.Equal(t, 4, cm.NodeSize())

	v, ok := cm.Get("threshold")
	assert.True(t, ok)
	assert.Equal(t, "0.5", v)

	_, ok = cm.Get("nonexistent")
	assert.False(t, ok)
}

func TestConfigManagerMerge(t *testing.T) {
	cm1 := NewConfigManager()
	cm1.Set("a", "1")

	cm2 := NewConfigManager()
	cm2.Set("b", "2")

	cm1.Merge(cm2)
	v, ok := cm1.Get("b")
	assert.True(t, ok)
	assert.Equal(t, "2", v)
}

func TestModel(t *testing.T) {
	m := NewModel("building", "field", map[string]FieldType{
		"name":      FieldString,
		"area":      FieldDouble,
		"KEY_CORD":  FieldCoord,
	})

	assert.Equal(t, "building", m.Name)
	assert.Equal(t, "field", m.Parent)

	typ, ok := m.GetType("name")
	assert.True(t, ok)
	assert.Equal(t, FieldString, typ)

	assert.True(t, m.IsKey("KEY_CORD"))
	assert.False(t, m.IsKey("name"))
}

func TestModelJSON(t *testing.T) {
	m := NewModel("test", "point", map[string]FieldType{
		"name": FieldString,
		"x":    FieldDouble,
	})

	j := m.ToJSON()
	assert.Equal(t, "test", j.Name)
	assert.Equal(t, "STRING", j.Parameters["name"])
	assert.Equal(t, "DOUBLE", j.Parameters["x"])

	restored := ModelFromJSON(j)
	assert.Equal(t, m.Name, restored.Name)
	typ, ok := restored.GetType("name")
	assert.True(t, ok)
	assert.Equal(t, FieldString, typ)
}

func TestFieldType(t *testing.T) {
	assert.Equal(t, "STRING", FieldString.String())
	assert.Equal(t, FieldString, ParseFieldType("STRING"))
	assert.Equal(t, FieldDouble, ParseFieldType("DOUBLE"))
	assert.True(t, FieldCoord.IsKey())
	assert.False(t, FieldString.IsKey())
}

func TestDatabase(t *testing.T) {
	db := NewDatabase("test_db")
	assert.Equal(t, StatusLoading, db.Status)

	db.Activate()
	assert.Equal(t, StatusActive, db.Status)

	m := NewModel("model1", "", map[string]FieldType{"a": FieldString})
	db.AddModel(m)

	got, ok := db.GetModel("model1")
	require.True(t, ok)
	assert.Equal(t, "model1", got.Name)

	models := db.ListModels()
	assert.Equal(t, 1, len(models))
}

func TestDatabaseMaps(t *testing.T) {
	db := NewDatabase("test")
	gm := NewGeoMap("china", 4, 0.5, db)
	db.AddMap(gm)

	got, ok := db.GetMap("china")
	require.True(t, ok)
	assert.Equal(t, "china", got.Name)

	maps := db.ListMaps()
	assert.Equal(t, 1, len(maps))
}

func TestGeoMap(t *testing.T) {
	db := NewDatabase("test")
	gm := NewGeoMap("map1", 8, 0.5, db)
	assert.Equal(t, 8, gm.NodeSize())
	assert.Equal(t, 0.5, gm.Threshold())

	elem := NewElement(10, 10, 20, 20, 0.5, "point", "building")
	gm.AddElement(elem)

	layer, ok := gm.GetLayer(gm.ElementLayerName("building"))
	require.True(t, ok)
	assert.Equal(t, 1, len(layer.Elements()))
}

func TestLayer(t *testing.T) {
	layer := NewLayer("mdl_building", 4, 0.5)
	assert.True(t, layer.IsModel())
	assert.False(t, layer.IsSubMap())
	assert.False(t, layer.IsChanged())

	elem := NewElement(5, 5, 15, 15, 0.5, "point", "building")
	layer.AddElement(elem)
	assert.True(t, layer.IsChanged())
	assert.Equal(t, 1, len(layer.Elements()))

	layer.ResetChanged()
	assert.False(t, layer.IsChanged())
}

func TestElement(t *testing.T) {
	elem := NewElement(1, 2, 3, 4, 0, "point", "building")
	elem.SetAttribute("name", "Tower")
	elem.SetAttribute("area", 100.5)

	v, ok := elem.GetAttribute("name")
	assert.True(t, ok)
	assert.Equal(t, "Tower", v)

	v, ok = elem.GetAttribute("area")
	assert.True(t, ok)
	assert.Equal(t, 100.5, v)

	_, ok = elem.GetAttribute("missing")
	assert.False(t, ok)
}

func TestCacheManager(t *testing.T) {
	cm := NewCacheManager()
	assert.Equal(t, 0, cm.Size())

	db1 := NewDatabase("db1")
	db2 := NewDatabase("db2")
	cm.AddDatabase(db1)
	cm.AddDatabase(db2)
	assert.Equal(t, 2, cm.Size())

	got, ok := cm.GetDatabase("db1")
	require.True(t, ok)
	assert.Equal(t, "db1", got.Name)

	names := cm.ListDatabaseNames()
	assert.Equal(t, 2, len(names))

	cm.RemoveDatabase("db1")
	assert.Equal(t, 1, cm.Size())
	_, ok = cm.GetDatabase("db1")
	assert.False(t, ok)
}
