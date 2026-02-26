package wal

import (
	"encoding/json"
	"fmt"
	"os"
	"path/filepath"
	"sort"
	"strings"
	"time"
)

// FileSystem handles WAL file creation, writing, reading, and deletion.
type FileSystem struct {
	rootPath string
}

func NewFileSystem(rootPath string) *FileSystem {
	return &FileSystem{rootPath: rootPath}
}

func (fs *FileSystem) logDir(dbName string) string {
	return filepath.Join(fs.rootPath, "data", "databases", dbName+".db", "logs")
}

// Write appends a log entry to a file named {timestamp}_{sessionId}.log.
func (fs *FileSystem) Write(entry LogEntry) error {
	dir := fs.logDir(entry.DatabaseName)
	if err := os.MkdirAll(dir, 0755); err != nil {
		return fmt.Errorf("create log dir: %w", err)
	}

	filename := fmt.Sprintf("%d_%d.log", entry.Timestamp.UnixMilli(), entry.SessionID)
	path := filepath.Join(dir, filename)

	data, err := json.Marshal(entry)
	if err != nil {
		return fmt.Errorf("marshal log entry: %w", err)
	}
	data = append(data, '\n')

	f, err := os.OpenFile(path, os.O_CREATE|os.O_APPEND|os.O_WRONLY, 0644)
	if err != nil {
		return fmt.Errorf("open log file: %w", err)
	}
	defer f.Close()

	if _, err := f.Write(data); err != nil {
		return fmt.Errorf("write log entry: %w", err)
	}
	return nil
}

// ReadAll reads all log entries from a database's log directory, sorted by filename.
func (fs *FileSystem) ReadAll(dbName string) ([]LogEntry, error) {
	dir := fs.logDir(dbName)
	if _, err := os.Stat(dir); os.IsNotExist(err) {
		return nil, nil
	}

	entries, err := os.ReadDir(dir)
	if err != nil {
		return nil, fmt.Errorf("read log dir: %w", err)
	}

	sort.Slice(entries, func(i, j int) bool {
		return entries[i].Name() < entries[j].Name()
	})

	var result []LogEntry
	for _, e := range entries {
		if e.IsDir() || !strings.HasSuffix(e.Name(), ".log") {
			continue
		}
		path := filepath.Join(dir, e.Name())
		data, err := os.ReadFile(path)
		if err != nil {
			return nil, fmt.Errorf("read log file %s: %w", path, err)
		}
		lines := strings.Split(strings.TrimSpace(string(data)), "\n")
		for _, line := range lines {
			if line == "" {
				continue
			}
			var entry LogEntry
			if err := json.Unmarshal([]byte(line), &entry); err != nil {
				return nil, fmt.Errorf("parse log entry in %s: %w", path, err)
			}
			result = append(result, entry)
		}
	}
	return result, nil
}

// DeleteAll removes all log files for a given database.
func (fs *FileSystem) DeleteAll(dbName string) error {
	dir := fs.logDir(dbName)
	if _, err := os.Stat(dir); os.IsNotExist(err) {
		return nil
	}
	entries, err := os.ReadDir(dir)
	if err != nil {
		return fmt.Errorf("read log dir for delete: %w", err)
	}
	for _, e := range entries {
		if !e.IsDir() && strings.HasSuffix(e.Name(), ".log") {
			os.Remove(filepath.Join(dir, e.Name()))
		}
	}
	return nil
}

// DeleteAllDatabases removes all log files across all databases.
func (fs *FileSystem) DeleteAllDatabases() error {
	dbRoot := filepath.Join(fs.rootPath, "data", "databases")
	entries, err := os.ReadDir(dbRoot)
	if err != nil {
		if os.IsNotExist(err) {
			return nil
		}
		return err
	}
	for _, e := range entries {
		if e.IsDir() && strings.HasSuffix(e.Name(), ".db") {
			dbName := strings.TrimSuffix(e.Name(), ".db")
			fs.DeleteAll(dbName)
		}
	}
	return nil
}

// Now returns the current time. Exposed for testing.
var Now = time.Now
