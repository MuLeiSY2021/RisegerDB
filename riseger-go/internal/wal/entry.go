package wal

import "time"

// LogEntry represents a single WAL record.
type LogEntry struct {
	Timestamp  time.Time `json:"timestamp"`
	SessionID  int       `json:"sessionId"`
	DatabaseName string  `json:"dbName"`
	Operations []Operation `json:"operations"`
}

// Operation is a single mutation operation in the WAL.
type Operation struct {
	Type       string                 `json:"type"`
	LayerName  string                 `json:"layerName,omitempty"`
	MapName    string                 `json:"mapName,omitempty"`
	ModelName  string                 `json:"modelName,omitempty"`
	Attributes map[string]interface{} `json:"attributes,omitempty"`
}
