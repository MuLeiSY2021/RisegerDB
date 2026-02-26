package wal

import (
	"context"
	"log/slog"
	"time"
)

// Manager orchestrates WAL operations: writing log entries,
// replaying logs for crash recovery, and running the background
// flush daemon.
type Manager struct {
	fs     *FileSystem
	daemon *Daemon
	logger *slog.Logger
	cancel context.CancelFunc
}

func NewManager(rootPath string, flushThreshold int, flushInterval time.Duration, onFlush func() error, logger *slog.Logger) *Manager {
	if logger == nil {
		logger = slog.Default()
	}
	fs := NewFileSystem(rootPath)
	daemon := NewDaemon(flushThreshold, flushInterval, onFlush, logger)
	return &Manager{
		fs:     fs,
		daemon: daemon,
		logger: logger,
	}
}

// Start launches the background daemon.
func (m *Manager) Start() {
	ctx, cancel := context.WithCancel(context.Background())
	m.cancel = cancel
	go m.daemon.Run(ctx)
	m.logger.Info("WAL daemon started")
}

// Stop gracefully shuts down the daemon.
func (m *Manager) Stop() {
	if m.cancel != nil {
		m.cancel()
	}
	m.logger.Info("WAL daemon stopped")
}

// WriteLog writes a log entry and notifies the daemon.
func (m *Manager) WriteLog(entry LogEntry) error {
	if entry.Timestamp.IsZero() {
		entry.Timestamp = Now()
	}
	if err := m.fs.Write(entry); err != nil {
		return err
	}
	m.daemon.CountDown(len(entry.Operations))
	return nil
}

// Recover reads and returns all log entries for a database.
// The caller is responsible for replaying them.
func (m *Manager) Recover(dbName string) ([]LogEntry, error) {
	return m.fs.ReadAll(dbName)
}

// DeleteLogs removes all log files for a database.
func (m *Manager) DeleteLogs(dbName string) error {
	return m.fs.DeleteAll(dbName)
}

// DeleteAllLogs removes all log files across all databases.
func (m *Manager) DeleteAllLogs() error {
	return m.fs.DeleteAllDatabases()
}
