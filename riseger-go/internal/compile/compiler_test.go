package compile

import (
	"testing"

	"github.com/riseger/riseger-go/internal/cache"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func setupTestDB() *cache.CacheManager {
	cm := cache.NewCacheManager()
	db := cache.NewDatabase("test_db")
	db.Activate()

	db.AddModel(cache.NewModel("building", "field", map[string]cache.FieldType{
		"name": cache.FieldString,
		"area": cache.FieldDouble,
	}))

	geoMap := cache.NewGeoMap("china", 4, 0.5, db)
	for i := 0; i < 20; i++ {
		x := float64(i * 10)
		elem := cache.NewElement(x, x, x+5, x+5, 0.5, "point", "building")
		elem.SetAttribute("name", "building_"+string(rune('A'+i)))
		elem.SetAttribute("area", float64(50+i*10))
		geoMap.AddElement(elem)
	}
	db.AddMap(geoMap)
	cm.AddDatabase(db)
	return cm
}

func TestCompilerGetDatabases(t *testing.T) {
	cm := setupTestDB()
	c := NewCompiler(cm)

	rs, err := c.Execute("GET DATABASES")
	require.NoError(t, err)
	require.NotNil(t, rs)
	assert.Contains(t, rs.Columns, "database")
}

func TestCompilerGetMaps(t *testing.T) {
	cm := setupTestDB()
	c := NewCompiler(cm)

	rs, err := c.Execute("USE DATABASE test_db GET MAPS")
	require.NoError(t, err)
	require.NotNil(t, rs)
	assert.Equal(t, 1, len(rs.Rows))
	assert.Equal(t, "china", rs.Rows[0]["map"])
}

func TestCompilerGetModels(t *testing.T) {
	cm := setupTestDB()
	c := NewCompiler(cm)

	rs, err := c.Execute("USE DATABASE test_db GET MODELS")
	require.NoError(t, err)
	require.NotNil(t, rs)
	assert.Equal(t, 1, len(rs.Rows))
	assert.Equal(t, "building", rs.Rows[0]["model"])
}

func TestCompilerSearchAll(t *testing.T) {
	cm := setupTestDB()
	c := NewCompiler(cm)

	rs, err := c.Execute("USE DATABASE test_db | MAP china SEARCH name, area")
	require.NoError(t, err)
	require.NotNil(t, rs)
	assert.Equal(t, 20, len(rs.Rows))
}

func TestCompilerSearchWhere(t *testing.T) {
	cm := setupTestDB()
	c := NewCompiler(cm)

	rs, err := c.Execute("USE DATABASE test_db | MAP china SEARCH name WHERE area > 100")
	require.NoError(t, err)
	require.NotNil(t, rs)
	assert.Greater(t, len(rs.Rows), 0)
	assert.Less(t, len(rs.Rows), 20)
}

func TestCompilerSearchWhereAnd(t *testing.T) {
	cm := setupTestDB()
	c := NewCompiler(cm)

	rs, err := c.Execute("USE DATABASE test_db | MAP china SEARCH name WHERE area > 80 AND area < 150")
	require.NoError(t, err)
	require.NotNil(t, rs)
	for _, row := range rs.Rows {
		area, ok := row["area"].(float64)
		if ok {
			assert.Greater(t, area, 80.0)
			assert.Less(t, area, 150.0)
		}
	}
}

func TestCompilerSearchWhereOr(t *testing.T) {
	cm := setupTestDB()
	c := NewCompiler(cm)

	rs, err := c.Execute("USE DATABASE test_db | MAP china SEARCH name WHERE area > 200 OR area < 60")
	require.NoError(t, err)
	require.NotNil(t, rs)
	assert.Greater(t, len(rs.Rows), 0)
}

func TestCompilerSearchArithmetic(t *testing.T) {
	cm := setupTestDB()
	c := NewCompiler(cm)

	rs, err := c.Execute("USE DATABASE test_db | MAP china SEARCH name WHERE area + 10 > 200")
	require.NoError(t, err)
	require.NotNil(t, rs)
}

func TestCompilerParseError(t *testing.T) {
	cm := setupTestDB()
	c := NewCompiler(cm)

	_, err := c.Execute("INVALID STUFF")
	assert.Error(t, err)
}

func TestCompilerNoDatabase(t *testing.T) {
	cm := cache.NewCacheManager()
	c := NewCompiler(cm)

	rs, err := c.Execute("GET DATABASES")
	require.NoError(t, err)
	assert.NotNil(t, rs)
}
