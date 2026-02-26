package rtree

// node represents both internal (SubTree) and leaf nodes.
// When isLeaf is true, elements holds the actual data rectangles
// and children is unused; otherwise children holds child nodes.
type node[R Rectangle] struct {
	Rect
	children []*node[R]
	elements []R
	parent   *node[R]
	isLeaf   bool
}

func newSubTree[R Rectangle](threshold float64) *node[R] {
	return &node[R]{
		Rect:   *NewEmptyRect(threshold),
		isLeaf: false,
	}
}

func newLeaf[R Rectangle](threshold float64) *node[R] {
	return &node[R]{
		Rect:   *NewEmptyRect(threshold),
		isLeaf: true,
	}
}

func newLeafWithElements[R Rectangle](elements []R, threshold float64) *node[R] {
	n := newLeaf[R](threshold)
	n.elements = make([]R, len(elements))
	copy(n.elements, elements)
	n.adjustFromElements()
	return n
}

func newSubTreeWithChildren[R Rectangle](children []*node[R], threshold float64) *node[R] {
	n := newSubTree[R](threshold)
	for _, c := range children {
		n.addChild(c)
	}
	return n
}

// ---------- child operations (internal node) ----------

func (n *node[R]) addChild(child *node[R]) {
	n.children = append(n.children, child)
	child.parent = n
	n.adjustUp(child)
}

func (n *node[R]) addChildren(children []*node[R]) {
	for _, c := range children {
		n.addChild(c)
	}
}

func (n *node[R]) setChildren(children []*node[R]) {
	n.children = children
	for _, c := range children {
		c.parent = n
	}
	n.adjustFromChildren()
}

func (n *node[R]) removeChild(child *node[R]) {
	for i, c := range n.children {
		if c == child {
			n.children = append(n.children[:i], n.children[i+1:]...)
			break
		}
	}
	child.parent = nil
	n.condense()
}

// ---------- element operations (leaf node) ----------

func (n *node[R]) addElement(e R) {
	n.elements = append(n.elements, e)
	n.adjustUp(e)
}

func (n *node[R]) addElements(elems []R) {
	for _, e := range elems {
		n.addElement(e)
	}
}

func (n *node[R]) removeElement(rect Rectangle) {
	for i := 0; i < len(n.elements); i++ {
		if n.elements[i].Match(rect) {
			n.elements = append(n.elements[:i], n.elements[i+1:]...)
			i--
		}
	}
	n.adjustFromElements()
	if n.parent != nil {
		n.parent.condense()
	}
}

func (n *node[R]) getIntersecting(rect Rectangle) []R {
	var result []R
	for _, e := range n.elements {
		if e.Intersects(rect) {
			result = append(result, e)
		}
	}
	return result
}

func (n *node[R]) getExactMatch(rect Rectangle) (R, bool) {
	for _, e := range n.elements {
		if e.Match(rect) {
			return e, true
		}
	}
	var zero R
	return zero, false
}

// ---------- capacity ----------

func (n *node[R]) isTooFew(maxEntries int) bool {
	minEntries := maxEntries >> 1
	if n.isLeaf {
		return len(n.elements) < minEntries
	}
	return len(n.children) < minEntries
}

func (n *node[R]) isOverflow(maxEntries int) bool {
	if n.isLeaf {
		return len(n.elements) > maxEntries
	}
	return len(n.children) > maxEntries
}

func (n *node[R]) isEmpty() bool {
	if n.isLeaf {
		return len(n.elements) == 0
	}
	return len(n.children) == 0
}

func (n *node[R]) size() int {
	if n.isLeaf {
		return len(n.elements)
	}
	return len(n.children)
}

// ---------- bounding box maintenance ----------

func (n *node[R]) adjustUp(r Rectangle) {
	changed := n.WillExpand(r)
	if changed {
		n.Expand(r)
	}
	if n.parent != nil && changed {
		n.parent.adjustUp(n)
	}
}

func (n *node[R]) condense() {
	if n.isLeaf {
		n.AdjustAll(toRectSlice(n.elements))
	} else {
		rects := make([]Rectangle, len(n.children))
		for i, c := range n.children {
			rects[i] = c
		}
		n.AdjustAll(rects)
	}
	if n.parent != nil {
		n.parent.condense()
	}
}

func (n *node[R]) adjustFromChildren() {
	rects := make([]Rectangle, len(n.children))
	for i, c := range n.children {
		rects[i] = c
	}
	n.AdjustAll(rects)
}

func (n *node[R]) adjustFromElements() {
	n.AdjustAll(toRectSlice(n.elements))
}

// ---------- traversal ----------

func (n *node[R]) collectLeaves() []*node[R] {
	if n.isLeaf {
		return []*node[R]{n}
	}
	var leaves []*node[R]
	queue := []*node[R]{n}
	for len(queue) > 0 {
		cur := queue[0]
		queue = queue[1:]
		if cur.isLeaf {
			leaves = append(leaves, cur)
		} else {
			queue = append(queue, cur.children...)
		}
	}
	return leaves
}

func (n *node[R]) collectAllElements() []R {
	if n.isLeaf {
		result := make([]R, len(n.elements))
		copy(result, n.elements)
		return result
	}
	var result []R
	for _, leaf := range n.collectLeaves() {
		result = append(result, leaf.elements...)
	}
	return result
}
