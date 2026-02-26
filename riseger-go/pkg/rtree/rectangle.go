package rtree

import "math"

// Rectangle defines the spatial interface for all bounding-box types.
type Rectangle interface {
	MinX() float64
	MaxX() float64
	MinY() float64
	MaxY() float64
	Area() float64
	Margin() float64
	Width() float64
	Height() float64
	Overlap(other Rectangle) float64
	IsLegal() bool
	Distance(other Rectangle) float64
	DistanceSquared(other Rectangle) float64
	Match(other Rectangle) bool
	Contains(other Rectangle) bool
	Intersects(other Rectangle) bool
	Expand(other Rectangle)
	ExpandAll(rects []Rectangle) bool
	CopyFrom(other Rectangle)
}

// Rect is a concrete MBR (Minimum Bounding Rectangle) with optional
// coordinate truncation controlled by Threshold.
type Rect struct {
	minX, maxX, minY, maxY float64
	Threshold              float64
}

func NewRect(minX, minY, maxX, maxY, threshold float64) *Rect {
	r := &Rect{Threshold: threshold}
	r.SetMinX(minX)
	r.SetMinY(minY)
	r.SetMaxX(maxX)
	r.SetMaxY(maxY)
	return r
}

func NewEmptyRect(threshold float64) *Rect {
	return &Rect{
		minX:      math.MaxFloat64,
		maxX:      -math.MaxFloat64,
		minY:      math.MaxFloat64,
		maxY:      -math.MaxFloat64,
		Threshold: threshold,
	}
}

func NewRectFromRects(rects []Rectangle, threshold float64) *Rect {
	r := NewEmptyRect(threshold)
	r.ExpandAll(rects)
	return r
}

func (r *Rect) MinX() float64 { return r.minX }
func (r *Rect) MaxX() float64 { return r.maxX }
func (r *Rect) MinY() float64 { return r.minY }
func (r *Rect) MaxY() float64 { return r.maxY }

func (r *Rect) SetMinX(v float64) { r.minX = r.truncate(v) }
func (r *Rect) SetMaxX(v float64) { r.maxX = r.truncate(v) }
func (r *Rect) SetMinY(v float64) { r.minY = r.truncate(v) }
func (r *Rect) SetMaxY(v float64) { r.maxY = r.truncate(v) }

func (r *Rect) SetAll(other Rectangle) {
	r.SetMinX(other.MinX())
	r.SetMinY(other.MinY())
	r.SetMaxX(other.MaxX())
	r.SetMaxY(other.MaxY())
}

func (r *Rect) Width() float64  { return r.maxX - r.minX }
func (r *Rect) Height() float64 { return r.maxY - r.minY }
func (r *Rect) Area() float64   { return r.Width() * r.Height() }
func (r *Rect) Margin() float64 { return r.Width() + r.Height() }

func (r *Rect) IsLegal() bool {
	return r.minX <= r.maxX && r.minY <= r.maxY
}

func (r *Rect) Overlap(other Rectangle) float64 {
	h := math.Min(r.maxY, other.MaxY()) - math.Max(r.minY, other.MinY())
	w := math.Min(r.maxX, other.MaxX()) - math.Max(r.minX, other.MinX())
	if h < 0 || w < 0 {
		return -1
	}
	return h * w
}

func (r *Rect) Intersects(other Rectangle) bool {
	return r.Overlap(other) > 0
}

func (r *Rect) Match(other Rectangle) bool {
	return r.minX == other.MinX() && r.maxX == other.MaxX() &&
		r.minY == other.MinY() && r.maxY == other.MaxY()
}

func (r *Rect) Contains(other Rectangle) bool {
	return other.MaxX() <= r.maxX && other.MaxY() <= r.maxY &&
		other.MinX() >= r.minX && other.MinY() >= r.minY
}

func (r *Rect) Distance(other Rectangle) float64 {
	return math.Sqrt(r.DistanceSquared(other))
}

func (r *Rect) DistanceSquared(other Rectangle) float64 {
	dx := r.Width() - other.Width()
	dy := r.Height() - other.Height()
	return dx*dx + dy*dy
}

func (r *Rect) Expand(other Rectangle) {
	r.SetMinX(math.Min(other.MinX(), r.minX))
	r.SetMinY(math.Min(other.MinY(), r.minY))
	r.SetMaxX(math.Max(other.MaxX(), r.maxX))
	r.SetMaxY(math.Max(other.MaxY(), r.maxY))
}

func (r *Rect) WillExpand(other Rectangle) bool {
	return math.IsNaN(r.minX) || other.MinX() < r.minX ||
		math.IsNaN(r.minY) || other.MinY() < r.minY ||
		math.IsNaN(r.maxX) || other.MaxX() > r.maxX ||
		math.IsNaN(r.maxY) || other.MaxY() > r.maxY
}

func (r *Rect) ExpandAll(rects []Rectangle) bool {
	changed := false
	for _, other := range rects {
		if r.WillExpand(other) {
			changed = true
			r.Expand(other)
		}
	}
	return changed
}

func (r *Rect) CopyFrom(other Rectangle) {
	r.SetMinX(other.MinX())
	r.SetMinY(other.MinY())
	r.SetMaxX(other.MaxX())
	r.SetMaxY(other.MaxY())
}

func (r *Rect) Move(x, y float64) {
	w, h := r.Width(), r.Height()
	r.SetMinX(x)
	r.SetMaxX(x + w)
	r.SetMinY(y)
	r.SetMaxY(y + h)
}

// AdjustAll resets bounds to empty then expands to cover all rects.
func (r *Rect) AdjustAll(rects []Rectangle) {
	r.minX = math.MaxFloat64
	r.maxX = -math.MaxFloat64
	r.minY = math.MaxFloat64
	r.maxY = -math.MaxFloat64
	r.ExpandAll(rects)
}

func (r *Rect) truncate(v float64) float64 {
	if r.Threshold == 0 {
		return v
	}
	return math.Floor(v/r.Threshold) * r.Threshold
}

func (r *Rect) String() string {
	return "Rect{" +
		"minX=" + floatStr(r.minX) +
		", maxX=" + floatStr(r.maxX) +
		", minY=" + floatStr(r.minY) +
		", maxY=" + floatStr(r.maxY) + "}"
}
