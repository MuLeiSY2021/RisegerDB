package server

import (
	"context"
	"encoding/json"
	"net"
	"testing"
	"time"

	"github.com/riseger/riseger-go/internal/cache"
	"github.com/riseger/riseger-go/internal/compile"
	"github.com/riseger/riseger-go/pkg/protocol"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func setupTestServer(t *testing.T) (*Server, *cache.CacheManager) {
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
	return srv, cm
}

func startServer(t *testing.T, srv *Server) string {
	t.Helper()
	ctx, cancel := context.WithCancel(context.Background())
	t.Cleanup(func() {
		cancel()
		srv.Stop()
	})

	ready := make(chan string, 1)
	go func() {
		ln, err := net.Listen("tcp", ":0")
		if err != nil {
			t.Error(err)
			return
		}
		srv.listener = ln
		srv.logger.Info("test server listening", "addr", ln.Addr().String())
		ready <- ln.Addr().String()

		go func() {
			<-ctx.Done()
			ln.Close()
		}()

		for {
			conn, err := ln.Accept()
			if err != nil {
				return
			}
			srv.wg.Add(1)
			go srv.handleConnection(conn)
		}
	}()

	select {
	case addr := <-ready:
		return addr
	case <-time.After(2 * time.Second):
		t.Fatal("server failed to start")
		return ""
	}
}

func sendRequest(t *testing.T, conn net.Conn, query string) *protocol.Response {
	t.Helper()
	req := protocol.Request{Type: protocol.ReqShell, Query: query}
	require.NoError(t, protocol.WritePacket(conn, protocol.PacketTextSQL, &req))

	_, data, err := protocol.ReadPacket(conn)
	require.NoError(t, err)

	var resp protocol.Response
	require.NoError(t, json.Unmarshal(data, &resp))
	return &resp
}

func TestServerGetDatabases(t *testing.T) {
	srv, _ := setupTestServer(t)
	addr := startServer(t, srv)

	conn, err := net.Dial("tcp", addr)
	require.NoError(t, err)
	defer conn.Close()

	resp := sendRequest(t, conn, "GET DATABASES")
	assert.True(t, resp.Success)
}

func TestServerSearchQuery(t *testing.T) {
	srv, _ := setupTestServer(t)
	addr := startServer(t, srv)

	conn, err := net.Dial("tcp", addr)
	require.NoError(t, err)
	defer conn.Close()

	resp := sendRequest(t, conn, "USE DATABASE test_db | MAP china SEARCH name, area")
	assert.True(t, resp.Success)
	assert.Equal(t, 10, resp.RowCount)
}

func TestServerSearchWhere(t *testing.T) {
	srv, _ := setupTestServer(t)
	addr := startServer(t, srv)

	conn, err := net.Dial("tcp", addr)
	require.NoError(t, err)
	defer conn.Close()

	resp := sendRequest(t, conn, "USE DATABASE test_db | MAP china SEARCH name WHERE area > 100")
	assert.True(t, resp.Success)
	assert.Greater(t, resp.RowCount, 0)
	assert.Less(t, resp.RowCount, 10)
}

func TestServerInvalidQuery(t *testing.T) {
	srv, _ := setupTestServer(t)
	addr := startServer(t, srv)

	conn, err := net.Dial("tcp", addr)
	require.NoError(t, err)
	defer conn.Close()

	resp := sendRequest(t, conn, "INVALID QUERY")
	assert.False(t, resp.Success)
	assert.NotEmpty(t, resp.Error)
}

func TestServerMultipleQueries(t *testing.T) {
	srv, _ := setupTestServer(t)
	addr := startServer(t, srv)

	conn, err := net.Dial("tcp", addr)
	require.NoError(t, err)
	defer conn.Close()

	queries := []string{
		"GET DATABASES",
		"USE DATABASE test_db GET MAPS",
		"USE DATABASE test_db GET MODELS",
		"USE DATABASE test_db | MAP china SEARCH name, area",
	}

	for _, q := range queries {
		resp := sendRequest(t, conn, q)
		assert.True(t, resp.Success, "query: %s", q)
	}
}

func TestServerMultipleClients(t *testing.T) {
	srv, _ := setupTestServer(t)
	addr := startServer(t, srv)

	done := make(chan bool, 3)
	for i := 0; i < 3; i++ {
		go func() {
			conn, err := net.Dial("tcp", addr)
			if err != nil {
				done <- false
				return
			}
			defer conn.Close()

			resp := sendRequest(t, conn, "GET DATABASES")
			done <- resp.Success
		}()
	}

	for i := 0; i < 3; i++ {
		select {
		case ok := <-done:
			assert.True(t, ok)
		case <-time.After(5 * time.Second):
			t.Fatal("timeout waiting for client")
		}
	}
}
