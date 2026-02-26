package rtree

import "math"

// STRRTree implements the Sort-Tile-Recursive bulk-loading algorithm
// on top of an RStarTree. After bulk loading, the tree uses the
// R*-tree insertion strategy for subsequent single inserts.
type STRRTree[R Rectangle] struct {
	RStarTree[R]
}

func NewSTRRTree[R Rectangle](maxEntries int, threshold float64) *STRRTree[R] {
	return &STRRTree[R]{
		RStarTree: *NewRStarTree[R](maxEntries, threshold),
	}
}

// InsertAll bulk-loads rects using the STR packing algorithm.
func (t *STRRTree[R]) InsertAll(rects []R) {
	if len(rects) == 0 {
		return
	}
	ifaces := make([]Rectangle, len(rects))
	for i, r := range rects {
		ifaces[i] = r
	}
	nodes := t.packLevel(ifaces, true)
	for len(nodes) > 1 {
		asRect := make([]Rectangle, len(nodes))
		for i, n := range nodes {
			asRect[i] = n
		}
		nodes = t.packLevel(asRect, false)
	}
	if len(nodes) == 1 {
		t.root = nodes[0]
		t.root.parent = nil
	}
}

func (t *STRRTree[R]) chunkPieces(size int) int {
	M := t.MaxEntries
	P := size / M
	if size%M != 0 {
		P++
	}
	sq := math.Sqrt(float64(P))
	if sq == math.Floor(sq) {
		return int(sq)
	}
	return int(sq) + 1
}

func (t *STRRTree[R]) packLevel(rects []Rectangle, isElement bool) []*node[R] {
	M := t.MaxEntries
	pieces := t.chunkPieces(len(rects))
	chunkSize := pieces * M

	sorted := t.SortByAxis(rects, axisX, false)

	var strips [][]Rectangle
	for i := 0; i < pieces; i++ {
		from := min(i*chunkSize, len(sorted))
		to := min((i+1)*chunkSize, len(sorted))
		if to-from <= 0 {
			break
		}
		strips = append(strips, sorted[from:to])
	}

	var result []*node[R]
	for _, strip := range strips {
		ySorted := t.SortByAxis(strip, axisY, true)
		for i := 0; i < pieces; i++ {
			from := min(i*M, len(ySorted))
			to := min((i+1)*M, len(ySorted))
			if to-from <= 0 {
				break
			}
			sub := ySorted[from:to]
			if isElement {
				elems := make([]R, len(sub))
				for j, r := range sub {
					elems[j] = r.(R)
				}
				result = append(result, newLeafWithElements(elems, t.Threshold))
			} else {
				children := make([]*node[R], len(sub))
				for j, r := range sub {
					children[j] = r.(*node[R])
				}
				result = append(result, newSubTreeWithChildren(children, t.Threshold))
			}
		}
	}
	return result
}
