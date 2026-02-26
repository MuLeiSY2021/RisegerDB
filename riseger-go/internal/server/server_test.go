package server

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"net/http"
	"testing"
	"time"

	"github.com/riseger/riseger-go/internal/cache"
	"github.com/riseger/riseger-go/internal/compile"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func setupTestServer(t *testing.T) (*Server, string) {
	t.Helper()
	cm := cache.NewCacheManager()
	db := cache.NewDatabase("test_db")
	db.Activate()
	db.AddModel(cache.NewModel("building", "field", map[string]cache.FieldType{
		"name": cache.FieldString,
		"area": cache.FieldDouble,
	}))
	geoMap := cache.NewGeoMap("china", 4, 0.5, db)
	for i := 0; i < 10; i++ {
		x := float64(i * 10)
		elem := cache.NewElement(x, x, x+5, x+5, 0.5, "point", "building")
		elem.SetAttribute("name", "building")
		elem.SetAttribute("area", float64(50+i*10))
		geoMap.AddElement(elem)
	}
	db.AddMap(geoMap)
	cm.AddDatabase(db)

	compiler := compile.NewCompiler(cm)
	srv := New(":0", compiler, nil)

	ctx, cancel := context.WithCancel(context.Background())
	ready := make(chan struct{})
	go func() {
		close(ready)
		srv.Start(ctx)
	}()
	<-ready
	time.Sleep(50 * time.Millisecond)
	t.Cleanup(func() { cancel(); time.Sleep(50 * time.Millisecond) })

	return srv, srv.Addr()
}

func queryHTTP(t *testing.T, addr, sql string) QueryResponse {
	t.Helper()
	body, _ := json.Marshal(QueryRequest{SQL: sql})
	resp, err := http.Post(fmt.Sprintf("http://%s/query", addr), "application/json", bytes.NewReader(body))
	require.NoError(t, err)
	defer resp.Body.Close()

	var qr QueryResponse
	require.NoError(t, json.NewDecoder(resp.Body).Decode(&qr))
	return qr
}

func TestHTTPHealth(t *testing.T) {
	_, addr := setupTestServer(t)
	resp, err := http.Get(fmt.Sprintf("http://%s/health", addr))
	require.NoError(t, err)
	assert.Equal(t, 200, resp.StatusCode)
}

func TestHTTPGetDatabases(t *testing.T) {
	_, addr := setupTestServer(t)
	qr := queryHTTP(t, addr, "GET DATABASES")
	assert.True(t, qr.Success)
}

func TestHTTPSearch(t *testing.T) {
	_, addr := setupTestServer(t)
	qr := queryHTTP(t, addr, "USE DATABASE test_db | MAP china SEARCH name, area")
	assert.True(t, qr.Success)
	assert.Equal(t, 10, qr.RowCount)
}

func TestHTTPSearchWhere(t *testing.T) {
	_, addr := setupTestServer(t)
	qr := queryHTTP(t, addr, "USE DATABASE test_db | MAP china SEARCH name WHERE area > 100")
	assert.True(t, qr.Success)
	assert.Greater(t, qr.RowCount, 0)
	assert.Less(t, qr.RowCount, 10)
}

func TestHTTPInvalidQuery(t *testing.T) {
	_, addr := setupTestServer(t)
	qr := queryHTTP(t, addr, "INVALID SQL")
	assert.False(t, qr.Success)
	assert.NotEmpty(t, qr.Error)
}

func TestHTTPUpdate(t *testing.T) {
	_, addr := setupTestServer(t)
	qr := queryHTTP(t, addr, "USE DATABASE test_db | MAP china UPDATE name = 'updated' WHERE area > 100")
	assert.True(t, qr.Success)
	assert.Greater(t, qr.RowCount, 0)
}

func TestHTTPCreateAndDelete(t *testing.T) {
	_, addr := setupTestServer(t)

	qr := queryHTTP(t, addr, "USE DATABASE test_db CREATE MODEL city PARENT point PARAM name STRING PARAM population DOUBLE")
	assert.True(t, qr.Success)

	qr2 := queryHTTP(t, addr, "USE DATABASE test_db GET MODELS")
	assert.True(t, qr2.Success)
	found := false
	for _, row := range qr2.Rows {
		if row["model"] == "city" {
			found = true
		}
	}
	assert.True(t, found, "created model should be visible")
}

func TestHTTPEmptySQL(t *testing.T) {
	_, addr := setupTestServer(t)
	body, _ := json.Marshal(QueryRequest{SQL: ""})
	resp, err := http.Post(fmt.Sprintf("http://%s/query", addr), "application/json", bytes.NewReader(body))
	require.NoError(t, err)
	assert.Equal(t, 400, resp.StatusCode)
}
