package server

import (
	"context"
	"encoding/json"
	"fmt"
	"log/slog"
	"net"
	"sync"

	"github.com/riseger/riseger-go/internal/compile"
	"github.com/riseger/riseger-go/internal/compile/function"
	"github.com/riseger/riseger-go/pkg/protocol"
)

// Server is the TCP server that accepts client connections and
// dispatches queries to the compiler/executor.
type Server struct {
	addr     string
	listener net.Listener
	compiler *compile.Compiler
	logger   *slog.Logger
	wg       sync.WaitGroup
	quit     chan struct{}
}

func New(addr string, compiler *compile.Compiler, logger *slog.Logger) *Server {
	if logger == nil {
		logger = slog.Default()
	}
	return &Server{
		addr:     addr,
		compiler: compiler,
		logger:   logger,
		quit:     make(chan struct{}),
	}
}

// Start begins listening and accepting connections. It blocks until
// the context is cancelled or Stop is called.
func (s *Server) Start(ctx context.Context) error {
	ln, err := net.Listen("tcp", s.addr)
	if err != nil {
		return fmt.Errorf("listen %s: %w", s.addr, err)
	}
	s.listener = ln
	s.logger.Info("server listening", "addr", s.addr)

	go func() {
		select {
		case <-ctx.Done():
			s.Stop()
		case <-s.quit:
		}
	}()

	for {
		conn, err := ln.Accept()
		if err != nil {
			select {
			case <-s.quit:
				return nil
			default:
				s.logger.Error("accept error", "error", err)
				continue
			}
		}
		s.wg.Add(1)
		go s.handleConnection(conn)
	}
}

// Stop gracefully shuts down the server.
func (s *Server) Stop() {
	close(s.quit)
	if s.listener != nil {
		s.listener.Close()
	}
	s.wg.Wait()
	s.logger.Info("server stopped")
}

// Addr returns the listener address (useful for tests with port 0).
func (s *Server) Addr() string {
	if s.listener != nil {
		return s.listener.Addr().String()
	}
	return s.addr
}

func (s *Server) handleConnection(conn net.Conn) {
	defer s.wg.Done()
	defer conn.Close()

	remote := conn.RemoteAddr().String()
	s.logger.Info("client connected", "remote", remote)

	for {
		pktType, data, err := protocol.ReadPacket(conn)
		if err != nil {
			s.logger.Debug("client disconnected", "remote", remote, "reason", err)
			return
		}

		var resp *protocol.Response
		switch pktType {
		case protocol.PacketTextSQL:
			resp = s.handleTextSQL(data, remote)
		default:
			resp = protocol.ErrorResponse(fmt.Errorf("unsupported packet type: %d", pktType))
		}

		if err := protocol.WritePacket(conn, protocol.PacketTextSQLResponse, resp); err != nil {
			s.logger.Error("write response failed", "remote", remote, "error", err)
			return
		}
	}
}

func (s *Server) handleTextSQL(data []byte, remote string) *protocol.Response {
	var req protocol.Request
	if err := json.Unmarshal(data, &req); err != nil {
		return protocol.ErrorResponse(fmt.Errorf("invalid request: %w", err))
	}

	s.logger.Info("query", "remote", remote, "sql", req.Query)

	rs, err := s.compiler.Execute(req.Query)
	if err != nil {
		return protocol.ErrorResponse(err)
	}

	return resultSetToResponse(rs)
}

func resultSetToResponse(rs *function.ResultSet) *protocol.Response {
	if rs == nil {
		return protocol.EmptySuccess()
	}
	return protocol.SuccessResponse(rs.Columns, rs.Rows)
}
