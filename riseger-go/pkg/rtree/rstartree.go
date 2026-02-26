package rtree

import (
	"math"
	"sort"
)

type axis int

const (
	axisX axis = iota
	axisY
)

// RStarTree extends RTree with the R*-tree insertion strategy:
// overlap-minimizing subtree choice, forced reinsert, and
// axis-based split algorithm.
type RStarTree[R Rectangle] struct {
	RTree[R]
	m int // min entries ≈ M * 0.4
	p int // reinsert count ≈ M * 0.3
}

func NewRStarTree[R Rectangle](maxEntries int, threshold float64) *RStarTree[R] {
	t := &RStarTree[R]{
		RTree: *NewRTree[R](maxEntries, threshold),
		m:     int(float64(maxEntries) * 0.4),
		p:     int(float64(maxEntries) * 0.3),
	}
	return t
}

func (t *RStarTree[R]) Insert(rect R) {
	roundTable := make(map[*node[R]]bool)
	leaf := t.chooseSubtree(rect)
	leaf.addElement(rect)
	if leaf.isOverflow(t.MaxEntries) {
		t.overflowTreatment(leaf, roundTable)
	}
}

func (t *RStarTree[R]) InsertAll(rects []R) {
	for _, r := range rects {
		t.Insert(r)
	}
}

// ---------- subtree selection ----------

func (t *RStarTree[R]) chooseSubtree(rect R) *node[R] {
	cur := t.root
	for !cur.isLeaf {
		if t.isLeafParent(cur) {
			best := t.bestByOverlap(rect, cur.children)
			cur = best
		} else {
			best := t.bestByAreaEnlargement(rect, cur.children)
			cur = best
		}
	}
	return cur
}

func (t *RStarTree[R]) isLeafParent(n *node[R]) bool {
	if len(n.children) == 0 {
		return true
	}
	return n.children[0].isLeaf
}

func (t *RStarTree[R]) bestByOverlap(rect Rectangle, candidates []*node[R]) *node[R] {
	best := candidates[0]
	bestOverlap := t.computeOverlap(rect, best, candidates)
	for _, c := range candidates[1:] {
		ov := t.computeOverlap(rect, c, candidates)
		if ov < bestOverlap {
			bestOverlap = ov
			best = c
		} else if ov == bestOverlap {
			ae1 := t.areaEnlargement(rect, best)
			ae2 := t.areaEnlargement(rect, c)
			if ae2 < ae1 {
				best = c
			}
		}
	}
	return best
}

func (t *RStarTree[R]) bestByAreaEnlargement(rect Rectangle, candidates []*node[R]) *node[R] {
	best := candidates[0]
	bestAE := t.areaEnlargement(rect, best)
	bestArea := t.expandedArea(rect, best)
	for _, c := range candidates[1:] {
		ae := t.areaEnlargement(rect, c)
		if ae < bestAE {
			bestAE = ae
			bestArea = t.expandedArea(rect, c)
			best = c
		} else if ae == bestAE {
			a := t.expandedArea(rect, c)
			if a < bestArea {
				bestArea = a
				best = c
			}
		}
	}
	return best
}

func (t *RStarTree[R]) computeOverlap(rect Rectangle, candidate *node[R], all []*node[R]) float64 {
	expanded := NewEmptyRect(t.Threshold)
	expanded.Expand(candidate)
	expanded.Expand(rect)
	total := 0.0
	for _, other := range all {
		ov := expanded.Overlap(other)
		if ov > 0 {
			total += ov
		}
	}
	return total
}

func (t *RStarTree[R]) areaEnlargement(rect Rectangle, n *node[R]) float64 {
	return t.expandedArea(rect, n) - n.Area()
}

func (t *RStarTree[R]) expandedArea(rect Rectangle, n *node[R]) float64 {
	expanded := NewEmptyRect(t.Threshold)
	expanded.Expand(n)
	expanded.Expand(rect)
	return expanded.Area()
}

// ---------- overflow treatment ----------

func (t *RStarTree[R]) overflowTreatment(n *node[R], roundTable map[*node[R]]bool) {
	if n == nil || !n.isOverflow(t.MaxEntries) {
		return
	}
	parent := n.parent
	if n != t.root && roundTable[n] {
		t.reInsert(n)
	} else {
		roundTable[n] = true
		t.split(n)
		t.overflowTreatment(parent, roundTable)
	}
}

// ---------- split ----------

func (t *RStarTree[R]) split(n *node[R]) {
	parent := n.parent
	if parent == nil {
		t.root = newSubTree[R](t.Threshold)
		parent = t.root
	} else {
		parent.removeChild(n)
	}

	if n.isLeaf {
		group1, group2 := t.splitElements(n.elements)
		leaf1 := newLeafWithElements(group1, t.Threshold)
		leaf2 := newLeafWithElements(group2, t.Threshold)
		parent.addChild(leaf1)
		parent.addChild(leaf2)
	} else {
		group1, group2 := t.splitNodes(n.children)
		sub1 := newSubTreeWithChildren(group1, t.Threshold)
		sub2 := newSubTreeWithChildren(group2, t.Threshold)
		parent.addChild(sub1)
		parent.addChild(sub2)
	}
}

type rectEntry struct {
	rect Rectangle
	idx  int
}

func (t *RStarTree[R]) splitElements(elements []R) ([]R, []R) {
	rects := make([]Rectangle, len(elements))
	for i, e := range elements {
		rects[i] = e
	}
	ax := t.chooseSplitAxis(rects)
	sorted := t.sortByAxis(rects, ax, false)
	idx := t.chooseSplitIndex(sorted)

	group1 := make([]R, idx)
	group2 := make([]R, len(sorted)-idx)
	for i, r := range sorted[:idx] {
		group1[i] = r.(R)
	}
	for i, r := range sorted[idx:] {
		group2[i] = r.(R)
	}
	return group1, group2
}

func (t *RStarTree[R]) splitNodes(children []*node[R]) ([]*node[R], []*node[R]) {
	rects := make([]Rectangle, len(children))
	for i, c := range children {
		rects[i] = c
	}
	ax := t.chooseSplitAxis(rects)
	sorted := t.sortByAxis(rects, ax, false)
	idx := t.chooseSplitIndex(sorted)

	group1 := make([]*node[R], idx)
	group2 := make([]*node[R], len(sorted)-idx)
	for i, r := range sorted[:idx] {
		group1[i] = r.(*node[R])
	}
	for i, r := range sorted[idx:] {
		group2[i] = r.(*node[R])
	}
	return group1, group2
}

func (t *RStarTree[R]) chooseSplitAxis(rects []Rectangle) axis {
	M := t.MaxEntries
	m := t.m

	sortedX := t.sortByAxis(rects, axisX, false)
	xMargin := 0.0
	for k := 1; k <= M-2*m+2; k++ {
		xMargin += t.marginValue(sortedX[:m-1+k], sortedX[m-1+k:])
	}

	sortedY := t.sortByAxis(rects, axisY, false)
	yMargin := 0.0
	for k := 1; k <= M-2*m+2; k++ {
		yMargin += t.marginValue(sortedY[:m-1+k], sortedY[m-1+k:])
	}

	if xMargin < yMargin {
		return axisX
	}
	return axisY
}

func (t *RStarTree[R]) chooseSplitIndex(sorted []Rectangle) int {
	bestIdx := 1
	bestOverlap := math.MaxFloat64
	bestArea := math.MaxFloat64

	for k := 1; k < len(sorted); k++ {
		ov := t.overlapValue(sorted[:k], sorted[k:])
		av := t.areaValue(sorted[:k], sorted[k:])
		if ov < bestOverlap || (ov == bestOverlap && av < bestArea) {
			bestOverlap = ov
			bestArea = av
			bestIdx = k
		}
	}
	return bestIdx
}

func (t *RStarTree[R]) marginValue(g1, g2 []Rectangle) float64 {
	r1 := NewRectFromRects(g1, t.Threshold)
	r2 := NewRectFromRects(g2, t.Threshold)
	return r1.Margin() + r2.Margin()
}

func (t *RStarTree[R]) areaValue(g1, g2 []Rectangle) float64 {
	r1 := NewRectFromRects(g1, t.Threshold)
	r2 := NewRectFromRects(g2, t.Threshold)
	return r1.Area() + r2.Area()
}

func (t *RStarTree[R]) overlapValue(g1, g2 []Rectangle) float64 {
	r1 := NewRectFromRects(g1, t.Threshold)
	r2 := NewRectFromRects(g2, t.Threshold)
	ov := r1.Overlap(r2)
	if ov < 0 {
		return 0
	}
	return ov
}

// SortByAxis sorts rectangles by axis; exported for STR tree.
func (t *RStarTree[R]) SortByAxis(rects []Rectangle, ax axis, reversed bool) []Rectangle {
	return t.sortByAxis(rects, ax, reversed)
}

func (t *RStarTree[R]) sortByAxis(rects []Rectangle, ax axis, reversed bool) []Rectangle {
	sorted := make([]Rectangle, len(rects))
	copy(sorted, rects)
	sort.SliceStable(sorted, func(i, j int) bool {
		var cmp int
		if ax == axisX {
			cmp = floatCmp(sorted[i].MinX(), sorted[j].MinX())
			if cmp == 0 {
				cmp = floatCmp(sorted[i].MaxX(), sorted[j].MaxX())
			}
		} else {
			cmp = floatCmp(sorted[i].MinY(), sorted[j].MinY())
			if cmp == 0 {
				cmp = floatCmp(sorted[i].MaxY(), sorted[j].MaxY())
			}
		}
		if reversed {
			return cmp > 0
		}
		return cmp < 0
	})
	return sorted
}

func floatCmp(a, b float64) int {
	if a < b {
		return -1
	}
	if a > b {
		return 1
	}
	return 0
}

// ---------- reinsert ----------

func (t *RStarTree[R]) reInsert(n *node[R]) {
	if n.isLeaf {
		elems := make([]R, len(n.elements))
		copy(elems, n.elements)
		sort.SliceStable(elems, func(i, j int) bool {
			return elems[i].DistanceSquared(n) > elems[j].DistanceSquared(n)
		})
		p := t.p
		if p > len(elems) {
			p = len(elems)
		}
		toReinsert := elems[:p]
		keep := elems[p:]
		n.elements = keep
		n.adjustFromElements()
		for _, e := range toReinsert {
			t.Insert(e)
		}
	} else {
		children := make([]*node[R], len(n.children))
		copy(children, n.children)
		sort.SliceStable(children, func(i, j int) bool {
			return children[i].DistanceSquared(n) > children[j].DistanceSquared(n)
		})
		p := t.p
		if p > len(children) {
			p = len(children)
		}
		keep := children[p:]
		n.setChildren(keep)
		for _, child := range children[:p] {
			for _, e := range child.collectAllElements() {
				t.Insert(e)
			}
		}
	}
}
