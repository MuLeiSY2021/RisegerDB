package server

import (
	"context"
	"encoding/json"
	"fmt"
	"log/slog"
	"net"
	"net/http"
	"time"

	"github.com/riseger/riseger-go/internal/compile"
	"github.com/riseger/riseger-go/internal/compile/function"
)

// Server is the HTTP server that accepts client requests and
// dispatches queries to the compiler/executor.
type Server struct {
	addr     string
	compiler *compile.Compiler
	logger   *slog.Logger
	httpSrv  *http.Server
}

func New(addr string, compiler *compile.Compiler, logger *slog.Logger) *Server {
	if logger == nil {
		logger = slog.Default()
	}
	return &Server{
		addr:     addr,
		compiler: compiler,
		logger:   logger,
	}
}

// QueryRequest is the JSON body for POST /query.
type QueryRequest struct {
	SQL string `json:"sql"`
}

// QueryResponse is the JSON response for POST /query.
type QueryResponse struct {
	Success  bool                     `json:"success"`
	Error    string                   `json:"error,omitempty"`
	Columns  []string                 `json:"columns,omitempty"`
	Rows     []map[string]interface{} `json:"rows,omitempty"`
	RowCount int                      `json:"rowCount"`
	Time     string                   `json:"time,omitempty"`
}

// Start begins listening on HTTP. It blocks until ctx is cancelled.
func (s *Server) Start(ctx context.Context) error {
	mux := http.NewServeMux()
	mux.HandleFunc("/query", s.handleQuery)
	mux.HandleFunc("/health", s.handleHealth)

	s.httpSrv = &http.Server{
		Addr:    s.addr,
		Handler: mux,
	}

	ln, err := net.Listen("tcp", s.addr)
	if err != nil {
		return fmt.Errorf("listen %s: %w", s.addr, err)
	}
	s.addr = ln.Addr().String()
	s.logger.Info("HTTP server listening", "addr", s.addr)

	go func() {
		<-ctx.Done()
		shutCtx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
		defer cancel()
		s.httpSrv.Shutdown(shutCtx)
	}()

	err = s.httpSrv.Serve(ln)
	if err == http.ErrServerClosed {
		return nil
	}
	return err
}

// Stop gracefully shuts down the server.
func (s *Server) Stop() {
	if s.httpSrv != nil {
		shutCtx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
		defer cancel()
		s.httpSrv.Shutdown(shutCtx)
	}
	s.logger.Info("HTTP server stopped")
}

// Addr returns the actual listening address (useful when port is 0).
func (s *Server) Addr() string {
	return s.addr
}

// POST /query — execute a SQL query
func (s *Server) handleQuery(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "method not allowed", http.StatusMethodNotAllowed)
		return
	}

	var req QueryRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		writeJSON(w, http.StatusBadRequest, QueryResponse{
			Success: false, Error: "invalid JSON: " + err.Error(),
		})
		return
	}

	if req.SQL == "" {
		writeJSON(w, http.StatusBadRequest, QueryResponse{
			Success: false, Error: "empty SQL",
		})
		return
	}

	start := time.Now()
	s.logger.Info("query", "remote", r.RemoteAddr, "sql", req.SQL)

	rs, err := s.compiler.Execute(req.SQL)
	elapsed := time.Since(start)

	if err != nil {
		writeJSON(w, http.StatusOK, QueryResponse{
			Success: false, Error: err.Error(), Time: elapsed.String(),
		})
		return
	}

	writeJSON(w, http.StatusOK, resultSetToResponse(rs, elapsed))
}

// GET /health — health check
func (s *Server) handleHealth(w http.ResponseWriter, r *http.Request) {
	writeJSON(w, http.StatusOK, map[string]string{"status": "ok"})
}

func resultSetToResponse(rs *function.ResultSet, elapsed time.Duration) QueryResponse {
	if rs == nil {
		return QueryResponse{Success: true, RowCount: 0, Time: elapsed.String()}
	}
	return QueryResponse{
		Success:  true,
		Columns:  rs.Columns,
		Rows:     rs.Rows,
		RowCount: len(rs.Rows),
		Time:     elapsed.String(),
	}
}

func writeJSON(w http.ResponseWriter, status int, v interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(v)
}
