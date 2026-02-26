package protocol

import (
	"encoding/binary"
	"encoding/json"
	"fmt"
	"io"
)

// PacketType identifies the kind of protocol message.
type PacketType byte

const (
	PacketTextSQL         PacketType = 0
	PacketTextSQLResponse PacketType = 1
	PacketCommandSQL      PacketType = 2
)

// RequestType classifies the intended operation.
type RequestType string

const (
	ReqSearch  RequestType = "SEARCH"
	ReqUpdate  RequestType = "UPDATE"
	ReqPreload RequestType = "PRELOAD"
	ReqShell   RequestType = "SHELL"
)

// --- Request ---

type Request struct {
	Type    RequestType `json:"type"`
	Query   string      `json:"query"`
	Address string      `json:"address,omitempty"`
}

// --- Response ---

type Response struct {
	Success   bool                     `json:"success"`
	Error     string                   `json:"error,omitempty"`
	Columns   []string                 `json:"columns,omitempty"`
	Rows      []map[string]interface{} `json:"rows,omitempty"`
	RowCount  int                      `json:"rowCount"`
}

func SuccessResponse(columns []string, rows []map[string]interface{}) *Response {
	return &Response{
		Success:  true,
		Columns:  columns,
		Rows:     rows,
		RowCount: len(rows),
	}
}

func ErrorResponse(err error) *Response {
	return &Response{
		Success: false,
		Error:   err.Error(),
	}
}

func EmptySuccess() *Response {
	return &Response{Success: true, RowCount: 0}
}

// --- Wire Protocol ---
//
// Each message on the wire has the format:
//   [1 byte: PacketType] [4 bytes: payload length (big-endian)] [N bytes: JSON payload]

// WritePacket encodes and writes a packet to the writer.
func WritePacket(w io.Writer, pktType PacketType, payload interface{}) error {
	data, err := json.Marshal(payload)
	if err != nil {
		return fmt.Errorf("marshal payload: %w", err)
	}
	header := make([]byte, 5)
	header[0] = byte(pktType)
	binary.BigEndian.PutUint32(header[1:5], uint32(len(data)))
	if _, err := w.Write(header); err != nil {
		return fmt.Errorf("write header: %w", err)
	}
	if _, err := w.Write(data); err != nil {
		return fmt.Errorf("write payload: %w", err)
	}
	return nil
}

// ReadPacket reads a single packet from the reader.
func ReadPacket(r io.Reader) (PacketType, []byte, error) {
	header := make([]byte, 5)
	if _, err := io.ReadFull(r, header); err != nil {
		return 0, nil, err
	}
	pktType := PacketType(header[0])
	length := binary.BigEndian.Uint32(header[1:5])
	if length > 16*1024*1024 {
		return 0, nil, fmt.Errorf("payload too large: %d bytes", length)
	}
	data := make([]byte, length)
	if _, err := io.ReadFull(r, data); err != nil {
		return 0, nil, fmt.Errorf("read payload: %w", err)
	}
	return pktType, data, nil
}
