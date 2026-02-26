package wal

import (
	"context"
	"sync/atomic"
	"testing"
	"time"

	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func TestFileSystemWriteAndRead(t *testing.T) {
	root := t.TempDir()
	fs := NewFileSystem(root)

	entry := LogEntry{
		Timestamp:    time.Date(2025, 1, 1, 0, 0, 0, 0, time.UTC),
		SessionID:    42,
		DatabaseName: "test_db",
		Operations: []Operation{
			{Type: "INSERT", MapName: "china", ModelName: "building"},
		},
	}

	err := fs.Write(entry)
	require.NoError(t, err)

	entries, err := fs.ReadAll("test_db")
	require.NoError(t, err)
	require.Equal(t, 1, len(entries))
	assert.Equal(t, 42, entries[0].SessionID)
	assert.Equal(t, "test_db", entries[0].DatabaseName)
	assert.Equal(t, 1, len(entries[0].Operations))
	assert.Equal(t, "INSERT", entries[0].Operations[0].Type)
}

func TestFileSystemMultipleEntries(t *testing.T) {
	root := t.TempDir()
	fs := NewFileSystem(root)

	for i := 0; i < 5; i++ {
		err := fs.Write(LogEntry{
			Timestamp:    time.Date(2025, 1, 1, 0, 0, i, 0, time.UTC),
			SessionID:    i,
			DatabaseName: "db1",
			Operations:   []Operation{{Type: "INSERT"}},
		})
		require.NoError(t, err)
	}

	entries, err := fs.ReadAll("db1")
	require.NoError(t, err)
	assert.Equal(t, 5, len(entries))
}

func TestFileSystemDeleteAll(t *testing.T) {
	root := t.TempDir()
	fs := NewFileSystem(root)

	fs.Write(LogEntry{
		Timestamp:    time.Now(),
		SessionID:    1,
		DatabaseName: "db_del",
		Operations:   []Operation{{Type: "INSERT"}},
	})

	err := fs.DeleteAll("db_del")
	require.NoError(t, err)

	entries, err := fs.ReadAll("db_del")
	require.NoError(t, err)
	assert.Empty(t, entries)
}

func TestFileSystemReadEmpty(t *testing.T) {
	root := t.TempDir()
	fs := NewFileSystem(root)

	entries, err := fs.ReadAll("nonexistent")
	require.NoError(t, err)
	assert.Empty(t, entries)
}

func TestDaemonCountDown(t *testing.T) {
	var flushCount atomic.Int32
	d := NewDaemon(10, time.Hour, func() error {
		flushCount.Add(1)
		return nil
	}, nil)

	ctx, cancel := context.WithCancel(context.Background())
	go d.Run(ctx)

	d.CountDown(15)
	time.Sleep(100 * time.Millisecond)

	cancel()
	time.Sleep(100 * time.Millisecond)

	assert.GreaterOrEqual(t, flushCount.Load(), int32(1))
}

func TestManagerWriteAndRecover(t *testing.T) {
	root := t.TempDir()
	var flushed atomic.Bool
	mgr := NewManager(root, 100, time.Hour, func() error {
		flushed.Store(true)
		return nil
	}, nil)

	err := mgr.WriteLog(LogEntry{
		SessionID:    1,
		DatabaseName: "test_db",
		Operations: []Operation{
			{Type: "INSERT", MapName: "map1"},
			{Type: "INSERT", MapName: "map2"},
		},
	})
	require.NoError(t, err)

	entries, err := mgr.Recover("test_db")
	require.NoError(t, err)
	require.Equal(t, 1, len(entries))
	assert.Equal(t, 2, len(entries[0].Operations))

	err = mgr.DeleteLogs("test_db")
	require.NoError(t, err)

	entries, err = mgr.Recover("test_db")
	require.NoError(t, err)
	assert.Empty(t, entries)
}
