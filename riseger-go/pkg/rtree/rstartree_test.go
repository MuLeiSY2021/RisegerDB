package rtree

import (
	"math/rand"
	"testing"

	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func generateRects(n int, maxCoord float64, size float64) []*Rect {
	rng := rand.New(rand.NewSource(42))
	rects := make([]*Rect, n)
	for i := 0; i < n; i++ {
		x := rng.Float64() * (maxCoord - size)
		y := rng.Float64() * (maxCoord - size)
		w := rng.Float64()*size + 1
		h := rng.Float64()*size + 1
		rects[i] = NewRect(x, y, x+w, y+h, 0.5)
	}
	return rects
}

func TestRStarTreeInsertAndSearch(t *testing.T) {
	tree := NewRStarTree[*Rect](4, 0.5)
	rects := generateRects(100, 1000, 20)

	for _, r := range rects {
		tree.Insert(r)
	}

	scope := NewRect(100, 100, 300, 300, 0.5)
	results := tree.Search(scope)

	for _, r := range results {
		assert.True(t, r.Intersects(scope), "result should intersect with search scope")
	}

	var expected int
	for _, r := range rects {
		if r.Intersects(scope) {
			expected++
		}
	}
	assert.Equal(t, expected, len(results), "search should find all intersecting rectangles")
}

func TestRStarTreeInsertAllAndSearch(t *testing.T) {
	tree := NewRStarTree[*Rect](4, 0.5)
	rects := generateRects(50, 500, 15)
	tree.InsertAll(rects)

	all := tree.Elements()
	assert.Equal(t, len(rects), len(all), "tree should contain all inserted elements")
}

func TestRStarTreeDeleteStrict(t *testing.T) {
	tree := NewRStarTree[*Rect](4, 0.5)
	r1 := NewRect(10, 10, 20, 20, 0.5)
	r2 := NewRect(30, 30, 40, 40, 0.5)
	r3 := NewRect(50, 50, 60, 60, 0.5)

	tree.Insert(r1)
	tree.Insert(r2)
	tree.Insert(r3)

	assert.Equal(t, 3, len(tree.Elements()))

	deleted := tree.DeleteStrict(r2)
	assert.Equal(t, 1, deleted)
	assert.Equal(t, 2, len(tree.Elements()))

	_, found := tree.SelectStrict(r2)
	assert.False(t, found, "deleted element should not be found")

	_, found = tree.SelectStrict(r1)
	assert.True(t, found, "r1 should still exist")

	_, found = tree.SelectStrict(r3)
	assert.True(t, found, "r3 should still exist")
}

func TestRStarTreeDeleteNonExistent(t *testing.T) {
	tree := NewRStarTree[*Rect](4, 0.5)
	tree.Insert(NewRect(10, 10, 20, 20, 0.5))

	deleted := tree.DeleteStrict(NewRect(99, 99, 100, 100, 0.5))
	assert.Equal(t, 0, deleted)
	assert.Equal(t, 1, len(tree.Elements()))
}

func TestRStarTreeEmptyTree(t *testing.T) {
	tree := NewRStarTree[*Rect](4, 0.5)

	results := tree.Search(NewRect(0, 0, 100, 100, 0))
	assert.Empty(t, results)

	assert.Equal(t, 0, tree.Depth())
	assert.Empty(t, tree.Elements())
}

func TestRStarTreeDepth(t *testing.T) {
	tree := NewRStarTree[*Rect](4, 0.5)

	assert.Equal(t, 0, tree.Depth())

	tree.Insert(NewRect(0, 0, 10, 10, 0.5))
	assert.GreaterOrEqual(t, tree.Depth(), 1)

	rects := generateRects(100, 1000, 20)
	for _, r := range rects {
		tree.Insert(r)
	}
	assert.Greater(t, tree.Depth(), 1)
}

func TestRStarTreeSelectStrict(t *testing.T) {
	tree := NewRStarTree[*Rect](4, 0.5)
	target := NewRect(50, 50, 60, 60, 0.5)
	tree.Insert(NewRect(10, 10, 20, 20, 0.5))
	tree.Insert(target)
	tree.Insert(NewRect(100, 100, 110, 110, 0.5))

	found, ok := tree.SelectStrict(target)
	assert.True(t, ok)
	assert.True(t, found.Match(target))
}

func TestRStarTreeLargeInsert(t *testing.T) {
	tree := NewRStarTree[*Rect](8, 0.5)
	rects := generateRects(1600, 1600, 20)

	for _, r := range rects {
		tree.Insert(r)
	}

	all := tree.Elements()
	assert.Equal(t, len(rects), len(all))

	scope := NewRect(400, 400, 800, 800, 0.5)
	results := tree.Search(scope)

	var expected int
	for _, r := range rects {
		if r.Intersects(scope) {
			expected++
		}
	}
	assert.Equal(t, expected, len(results))
}

func TestRStarTreeDelete(t *testing.T) {
	tree := NewRStarTree[*Rect](4, 0.5)
	rects := generateRects(20, 200, 10)
	for _, r := range rects {
		tree.Insert(r)
	}
	require.Equal(t, 20, len(tree.Elements()))

	tree.Delete(rects[0])

	remaining := tree.Elements()
	for _, r := range remaining {
		assert.False(t, r.Match(rects[0]), "deleted element should not remain")
	}
}
