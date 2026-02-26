package rtree

import (
	"math"
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestNewRect(t *testing.T) {
	r := NewRect(1, 2, 3, 4, 0)
	assert.Equal(t, 1.0, r.MinX())
	assert.Equal(t, 3.0, r.MaxX())
	assert.Equal(t, 2.0, r.MinY())
	assert.Equal(t, 4.0, r.MaxY())
}

func TestNewEmptyRect(t *testing.T) {
	r := NewEmptyRect(0)
	assert.Equal(t, math.MaxFloat64, r.MinX())
	assert.Equal(t, -math.MaxFloat64, r.MaxX())
	assert.False(t, r.IsLegal())
}

func TestRectArea(t *testing.T) {
	r := NewRect(0, 0, 10, 5, 0)
	assert.Equal(t, 50.0, r.Area())
	assert.Equal(t, 10.0, r.Width())
	assert.Equal(t, 5.0, r.Height())
}

func TestRectMargin(t *testing.T) {
	r := NewRect(0, 0, 10, 5, 0)
	assert.Equal(t, 15.0, r.Margin())
}

func TestRectOverlap(t *testing.T) {
	r1 := NewRect(0, 0, 10, 10, 0)
	r2 := NewRect(5, 5, 15, 15, 0)
	assert.Equal(t, 25.0, r1.Overlap(r2))

	r3 := NewRect(20, 20, 30, 30, 0)
	assert.Equal(t, -1.0, r1.Overlap(r3))
}

func TestRectIntersects(t *testing.T) {
	r1 := NewRect(0, 0, 10, 10, 0)
	r2 := NewRect(5, 5, 15, 15, 0)
	assert.True(t, r1.Intersects(r2))

	r3 := NewRect(20, 20, 30, 30, 0)
	assert.False(t, r1.Intersects(r3))

	r4 := NewRect(10, 0, 20, 10, 0)
	assert.False(t, r1.Intersects(r4))
}

func TestRectMatch(t *testing.T) {
	r1 := NewRect(1, 2, 3, 4, 0)
	r2 := NewRect(1, 2, 3, 4, 0)
	r3 := NewRect(1, 2, 3, 5, 0)
	assert.True(t, r1.Match(r2))
	assert.False(t, r1.Match(r3))
}

func TestRectContains(t *testing.T) {
	outer := NewRect(0, 0, 100, 100, 0)
	inner := NewRect(10, 10, 50, 50, 0)
	assert.True(t, outer.Contains(inner))
	assert.False(t, inner.Contains(outer))
}

func TestRectExpand(t *testing.T) {
	r := NewRect(5, 5, 10, 10, 0)
	r.Expand(NewRect(0, 0, 20, 20, 0))
	assert.Equal(t, 0.0, r.MinX())
	assert.Equal(t, 0.0, r.MinY())
	assert.Equal(t, 20.0, r.MaxX())
	assert.Equal(t, 20.0, r.MaxY())
}

func TestRectExpandAll(t *testing.T) {
	r := NewEmptyRect(0)
	changed := r.ExpandAll([]Rectangle{
		NewRect(0, 0, 10, 10, 0),
		NewRect(20, 20, 30, 30, 0),
	})
	assert.True(t, changed)
	assert.Equal(t, 0.0, r.MinX())
	assert.Equal(t, 0.0, r.MinY())
	assert.Equal(t, 30.0, r.MaxX())
	assert.Equal(t, 30.0, r.MaxY())
}

func TestRectDistance(t *testing.T) {
	r1 := NewRect(0, 0, 10, 10, 0)
	r2 := NewRect(0, 0, 10, 10, 0)
	assert.Equal(t, 0.0, r1.Distance(r2))
}

func TestRectCopyFrom(t *testing.T) {
	r1 := NewRect(0, 0, 10, 10, 0)
	r2 := NewRect(5, 5, 15, 15, 0)
	r1.CopyFrom(r2)
	assert.True(t, r1.Match(r2))
}

func TestRectThreshold(t *testing.T) {
	r := NewRect(1.7, 2.3, 4.8, 5.9, 0.5)
	assert.Equal(t, 1.5, r.MinX())
	assert.Equal(t, 2.0, r.MinY())
	assert.Equal(t, 4.5, r.MaxX())
	assert.Equal(t, 5.5, r.MaxY())
}

func TestRectAdjustAll(t *testing.T) {
	r := NewRect(0, 0, 100, 100, 0)
	r.AdjustAll([]Rectangle{
		NewRect(5, 5, 10, 10, 0),
		NewRect(20, 20, 30, 30, 0),
	})
	assert.Equal(t, 5.0, r.MinX())
	assert.Equal(t, 5.0, r.MinY())
	assert.Equal(t, 30.0, r.MaxX())
	assert.Equal(t, 30.0, r.MaxY())
}

func TestRectMove(t *testing.T) {
	r := NewRect(0, 0, 10, 5, 0)
	r.Move(5, 10)
	assert.Equal(t, 5.0, r.MinX())
	assert.Equal(t, 15.0, r.MaxX())
	assert.Equal(t, 10.0, r.MinY())
	assert.Equal(t, 15.0, r.MaxY())
}

func TestRectString(t *testing.T) {
	r := NewRect(1, 2, 3, 4, 0)
	s := r.String()
	assert.Contains(t, s, "minX=1")
	assert.Contains(t, s, "maxX=3")
}

func TestRectFromRects(t *testing.T) {
	r := NewRectFromRects([]Rectangle{
		NewRect(0, 0, 5, 5, 0),
		NewRect(10, 10, 20, 20, 0),
	}, 0)
	assert.Equal(t, 0.0, r.MinX())
	assert.Equal(t, 20.0, r.MaxX())
}
