package rtree

import (
	"bytes"
	"encoding/json"
	"testing"

	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

type testElement struct {
	Rect
	Name string `json:"name"`
}

func newTestElement(minX, minY, maxX, maxY float64, name string) *testElement {
	return &testElement{
		Rect: *NewRect(minX, minY, maxX, maxY, 0.5),
		Name: name,
	}
}

type testElementSerializer struct{}

func (s *testElementSerializer) Marshal(r *testElement) ([]byte, error) {
	return json.Marshal(r)
}

func (s *testElementSerializer) Unmarshal(data []byte) (*testElement, error) {
	var e testElement
	if err := json.Unmarshal(data, &e); err != nil {
		return nil, err
	}
	return &e, nil
}

func TestSerializeDeserializeRStarTree(t *testing.T) {
	tree := NewRStarTree[*testElement](4, 0.5)
	tree.Insert(newTestElement(10, 10, 20, 20, "a"))
	tree.Insert(newTestElement(30, 30, 40, 40, "b"))
	tree.Insert(newTestElement(50, 50, 60, 60, "c"))
	tree.Insert(newTestElement(70, 70, 80, 80, "d"))
	tree.Insert(newTestElement(90, 90, 100, 100, "e"))

	ser := &testElementSerializer{}
	data, err := SerializeToBytes[*testElement](&tree.RTree, "rstar", ser)
	require.NoError(t, err)
	require.NotEmpty(t, data)

	restored, err := DeserializeRStarTree[*testElement](data, ser)
	require.NoError(t, err)

	origElems := tree.Elements()
	restoredElems := restored.Elements()
	assert.Equal(t, len(origElems), len(restoredElems))

	nameSet := make(map[string]bool)
	for _, e := range restoredElems {
		nameSet[e.Name] = true
	}
	assert.True(t, nameSet["a"])
	assert.True(t, nameSet["b"])
	assert.True(t, nameSet["c"])
	assert.True(t, nameSet["d"])
	assert.True(t, nameSet["e"])
}

func TestSerializeDeserializeSTRRTree(t *testing.T) {
	tree := NewSTRRTree[*testElement](4, 0.5)
	elems := []*testElement{
		newTestElement(10, 10, 20, 20, "x1"),
		newTestElement(30, 30, 40, 40, "x2"),
		newTestElement(50, 50, 60, 60, "x3"),
		newTestElement(70, 70, 80, 80, "x4"),
		newTestElement(90, 90, 100, 100, "x5"),
		newTestElement(110, 110, 120, 120, "x6"),
	}
	tree.InsertAll(elems)

	ser := &testElementSerializer{}
	data, err := SerializeToBytes[*testElement](&tree.RStarTree.RTree, "str", ser)
	require.NoError(t, err)

	restored, err := DeserializeSTRRTree[*testElement](data, ser)
	require.NoError(t, err)

	assert.Equal(t, len(elems), len(restored.Elements()))
}

func TestSerializeToWriter(t *testing.T) {
	tree := NewRStarTree[*testElement](4, 0.5)
	tree.Insert(newTestElement(1, 1, 5, 5, "w"))

	ser := &testElementSerializer{}
	var buf bytes.Buffer
	err := SerializeToWriter[*testElement](&tree.RTree, "rstar", ser, &buf)
	require.NoError(t, err)
	require.Greater(t, buf.Len(), 0)

	restored, err := DeserializeFromReader[*testElement](&buf, "rstar", ser)
	require.NoError(t, err)

	restoredElems := restored.Elements()
	require.Equal(t, 1, len(restoredElems))
	assert.Equal(t, "w", restoredElems[0].Name)
}

func TestSerializeEmptyTree(t *testing.T) {
	tree := NewRStarTree[*testElement](4, 0.5)
	ser := &testElementSerializer{}

	data, err := SerializeToBytes[*testElement](&tree.RTree, "rstar", ser)
	require.NoError(t, err)

	restored, err := DeserializeRStarTree[*testElement](data, ser)
	require.NoError(t, err)
	assert.Empty(t, restored.Elements())
}

func TestSerializeLargeTree(t *testing.T) {
	tree := NewRStarTree[*testElement](8, 0.5)
	for i := 0; i < 500; i++ {
		x := float64(i * 3)
		tree.Insert(newTestElement(x, x, x+2, x+2, ""))
	}

	ser := &testElementSerializer{}
	data, err := SerializeToBytes[*testElement](&tree.RTree, "rstar", ser)
	require.NoError(t, err)

	restored, err := DeserializeRStarTree[*testElement](data, ser)
	require.NoError(t, err)
	assert.Equal(t, 500, len(restored.Elements()))
}
