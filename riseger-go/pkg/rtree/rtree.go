package rtree

// RTree is the base R-tree implementation. It provides insert, delete,
// and search operations on spatial data organized in a balanced tree
// structure with minimum bounding rectangles.
type RTree[R Rectangle] struct {
	root       *node[R]
	MaxEntries int
	Threshold  float64
}

// NewRTree creates a basic R-tree.
func NewRTree[R Rectangle](maxEntries int, threshold float64) *RTree[R] {
	t := &RTree[R]{
		MaxEntries: maxEntries,
		Threshold:  threshold,
	}
	t.root = newLeaf[R](threshold)
	return t
}

func (t *RTree[R]) Root() *node[R] { return t.root }

// Insert adds a rectangle to the tree. The base implementation
// just sets the threshold; subtype (RStarTree) overrides for
// actual tree insertion.
func (t *RTree[R]) Insert(rect R) {
	// base: just a hook for threshold setting (mirroring Java)
}

func (t *RTree[R]) InsertAll(rects []R) {
	// base: hook
}

// Delete removes all elements intersecting with rect.
func (t *RTree[R]) Delete(rect Rectangle) {
	leaves := t.findLeaves(rect)
	if len(leaves) == 0 {
		return
	}
	for _, leaf := range leaves {
		leaf.removeElement(rect)
	}
	t.condenseTreeMulti(leaves)
	if !t.root.isLeaf && len(t.root.children) == 1 {
		t.root = t.root.children[0]
		t.root.parent = nil
	}
}

// DeleteStrict removes exactly one element matching rect (strict equality).
// Returns 1 if deleted, 0 otherwise.
func (t *RTree[R]) DeleteStrict(rect Rectangle) int {
	if t.root.isEmpty() {
		return 0
	}
	leaf := t.strictFindLeaf(rect)
	if leaf == nil {
		return 0
	}
	leaf.removeElement(rect)
	t.condenseTreeSingle(leaf)
	if !t.root.isLeaf && len(t.root.children) == 1 {
		t.root = t.root.children[0]
		t.root.parent = nil
	}
	return 1
}

// Search returns all elements whose bounding box intersects with rect.
func (t *RTree[R]) Search(rect Rectangle) []R {
	var result []R
	for _, leaf := range t.findLeaves(rect) {
		result = append(result, leaf.getIntersecting(rect)...)
	}
	return result
}

// SelectStrict returns the first element exactly matching rect.
func (t *RTree[R]) SelectStrict(rect Rectangle) (R, bool) {
	leaf := t.strictFindLeaf(rect)
	if leaf == nil {
		var zero R
		return zero, false
	}
	return leaf.getExactMatch(rect)
}

// Elements returns all elements in the tree via BFS.
func (t *RTree[R]) Elements() []R {
	return t.root.collectAllElements()
}

// Depth returns the tree depth.
func (t *RTree[R]) Depth() int {
	if t.root.isLeaf && len(t.root.elements) == 0 {
		return 0
	}
	depth := 1
	cur := t.root
	for !cur.isLeaf {
		if len(cur.children) == 0 {
			break
		}
		cur = cur.children[0]
		depth++
	}
	return depth
}

// MapSize returns [maxX, maxY] of the root bounding box.
func (t *RTree[R]) MapSize() [2]int {
	return [2]int{int(t.root.MaxX()), int(t.root.MaxY())}
}

// ---------- internal ----------

func (t *RTree[R]) findLeaves(rect Rectangle) []*node[R] {
	var result []*node[R]
	queue := []*node[R]{t.root}
	for len(queue) > 0 {
		var next []*node[R]
		for _, n := range queue {
			if n.isLeaf {
				if n.Intersects(rect) {
					result = append(result, n)
				}
			} else {
				for _, child := range n.children {
					if child.isLeaf {
						if child.Intersects(rect) {
							result = append(result, child)
						}
					} else if child.Intersects(rect) {
						next = append(next, child)
					}
				}
			}
		}
		queue = next
	}
	return result
}

func (t *RTree[R]) strictFindLeaf(rect Rectangle) *node[R] {
	queue := []*node[R]{t.root}
	for len(queue) > 0 {
		var next []*node[R]
		for _, n := range queue {
			if n.isLeaf {
				if _, ok := n.getExactMatch(rect); ok {
					return n
				}
			} else {
				for _, child := range n.children {
					if child.isLeaf {
						if child.Contains(rect) {
							if _, ok := child.getExactMatch(rect); ok {
								return child
							}
						}
					} else if child.Contains(rect) {
						next = append(next, child)
					}
				}
			}
		}
		queue = next
	}
	return nil
}

func (t *RTree[R]) condenseTreeSingle(leaf *node[R]) {
	if leaf == nil || !leaf.isTooFew(t.MaxEntries) {
		return
	}
	var eliminated []R
	cur := leaf
	for cur != nil && cur != t.root {
		parent := cur.parent
		if cur.isTooFew(t.MaxEntries) {
			if parent != nil {
				parent.removeChild(cur)
			}
			for _, l := range cur.collectLeaves() {
				eliminated = append(eliminated, l.elements...)
			}
		} else {
			cur.condense()
		}
		cur = parent
	}
	t.InsertAll(eliminated)
}

func (t *RTree[R]) condenseTreeMulti(leaves []*node[R]) {
	var eliminated []R
	level := make([]*node[R], len(leaves))
	copy(level, leaves)

	for len(level) > 0 && level[0].parent != nil {
		parentSet := make(map[*node[R]]struct{})
		for _, n := range level {
			parent := n.parent
			if parent == nil {
				continue
			}
			if n.isTooFew(t.MaxEntries) {
				parent.removeChild(n)
				for _, l := range n.collectLeaves() {
					eliminated = append(eliminated, l.elements...)
				}
			} else {
				n.condense()
			}
			parentSet[parent] = struct{}{}
		}
		level = level[:0]
		for p := range parentSet {
			level = append(level, p)
		}
	}
	t.InsertAll(eliminated)
}
