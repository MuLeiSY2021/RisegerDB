package rtree

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestSTRRTreeInsertAll(t *testing.T) {
	tree := NewSTRRTree[*Rect](4, 0.5)
	rects := generateRects(100, 1000, 20)
	tree.InsertAll(rects)

	all := tree.Elements()
	assert.Equal(t, len(rects), len(all), "bulk-loaded tree should contain all elements")
}

func TestSTRRTreeSearch(t *testing.T) {
	tree := NewSTRRTree[*Rect](4, 0.5)
	rects := generateRects(200, 1000, 20)
	tree.InsertAll(rects)

	scope := NewRect(200, 200, 500, 500, 0.5)
	results := tree.Search(scope)

	var expected int
	for _, r := range rects {
		if r.Intersects(scope) {
			expected++
		}
	}
	assert.Equal(t, expected, len(results))
}

func TestSTRRTreeLargeBulkLoad(t *testing.T) {
	tree := NewSTRRTree[*Rect](8, 0.5)
	rects := generateRects(1600, 1600, 20)
	tree.InsertAll(rects)

	all := tree.Elements()
	assert.Equal(t, len(rects), len(all))

	assert.Greater(t, tree.Depth(), 1)
}

func TestSTRRTreeEmpty(t *testing.T) {
	tree := NewSTRRTree[*Rect](4, 0.5)
	tree.InsertAll([]*Rect{})

	assert.Empty(t, tree.Elements())
}

func TestSTRRTreeSingleElement(t *testing.T) {
	tree := NewSTRRTree[*Rect](4, 0.5)
	r := NewRect(5, 5, 10, 10, 0.5)
	tree.InsertAll([]*Rect{r})

	all := tree.Elements()
	assert.Equal(t, 1, len(all))
	assert.True(t, all[0].Match(r))
}

func TestSTRRTreeSubsequentInsert(t *testing.T) {
	tree := NewSTRRTree[*Rect](4, 0.5)
	rects := generateRects(50, 500, 15)
	tree.InsertAll(rects)

	extra := NewRect(250, 250, 260, 260, 0.5)
	tree.Insert(extra)

	all := tree.Elements()
	assert.Equal(t, len(rects)+1, len(all))

	found, ok := tree.SelectStrict(extra)
	assert.True(t, ok)
	assert.True(t, found.Match(extra))
}
