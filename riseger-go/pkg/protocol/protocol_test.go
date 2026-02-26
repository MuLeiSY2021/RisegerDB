package protocol

import (
	"bytes"
	"encoding/json"
	"testing"

	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func TestWriteAndReadPacket(t *testing.T) {
	var buf bytes.Buffer

	req := Request{Type: ReqShell, Query: "GET DATABASES"}
	err := WritePacket(&buf, PacketTextSQL, &req)
	require.NoError(t, err)

	pktType, data, err := ReadPacket(&buf)
	require.NoError(t, err)
	assert.Equal(t, PacketTextSQL, pktType)

	var decoded Request
	require.NoError(t, json.Unmarshal(data, &decoded))
	assert.Equal(t, "GET DATABASES", decoded.Query)
	assert.Equal(t, ReqShell, decoded.Type)
}

func TestWriteAndReadResponse(t *testing.T) {
	var buf bytes.Buffer

	resp := SuccessResponse([]string{"name"}, []map[string]interface{}{
		{"name": "test_db"},
	})
	err := WritePacket(&buf, PacketTextSQLResponse, resp)
	require.NoError(t, err)

	pktType, data, err := ReadPacket(&buf)
	require.NoError(t, err)
	assert.Equal(t, PacketTextSQLResponse, pktType)

	var decoded Response
	require.NoError(t, json.Unmarshal(data, &decoded))
	assert.True(t, decoded.Success)
	assert.Equal(t, 1, decoded.RowCount)
	assert.Equal(t, "test_db", decoded.Rows[0]["name"])
}

func TestErrorResponse(t *testing.T) {
	resp := ErrorResponse(assert.AnError)
	assert.False(t, resp.Success)
	assert.NotEmpty(t, resp.Error)
}

func TestEmptySuccess(t *testing.T) {
	resp := EmptySuccess()
	assert.True(t, resp.Success)
	assert.Equal(t, 0, resp.RowCount)
}

func TestMultiplePackets(t *testing.T) {
	var buf bytes.Buffer

	for i := 0; i < 5; i++ {
		req := Request{Type: ReqShell, Query: "test"}
		require.NoError(t, WritePacket(&buf, PacketTextSQL, &req))
	}

	for i := 0; i < 5; i++ {
		pktType, data, err := ReadPacket(&buf)
		require.NoError(t, err)
		assert.Equal(t, PacketTextSQL, pktType)
		var decoded Request
		require.NoError(t, json.Unmarshal(data, &decoded))
		assert.Equal(t, "test", decoded.Query)
	}
}
