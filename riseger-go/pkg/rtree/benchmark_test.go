package rtree

import (
	"encoding/json"
	"testing"
)

func BenchmarkRStarTreeInsert(b *testing.B) {
	rects := generateRects(b.N, 10000, 20)
	tree := NewRStarTree[*Rect](8, 0.5)
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		tree.Insert(rects[i])
	}
}

func BenchmarkRStarTreeSearch(b *testing.B) {
	rects := generateRects(10000, 10000, 20)
	tree := NewRStarTree[*Rect](8, 0.5)
	for _, r := range rects {
		tree.Insert(r)
	}
	scope := NewRect(2000, 2000, 5000, 5000, 0.5)
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		tree.Search(scope)
	}
}

func BenchmarkSTRRTreeBulkLoad(b *testing.B) {
	rects := generateRects(10000, 10000, 20)
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		tree := NewSTRRTree[*Rect](8, 0.5)
		tree.InsertAll(rects)
	}
}

func BenchmarkSTRRTreeSearch(b *testing.B) {
	rects := generateRects(10000, 10000, 20)
	tree := NewSTRRTree[*Rect](8, 0.5)
	tree.InsertAll(rects)
	scope := NewRect(2000, 2000, 5000, 5000, 0.5)
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		tree.Search(scope)
	}
}

func BenchmarkSerialize(b *testing.B) {
	tree := NewRStarTree[*testElement](8, 0.5)
	for i := 0; i < 1000; i++ {
		x := float64(i * 3)
		tree.Insert(newTestElement(x, x, x+2, x+2, "elem"))
	}
	ser := &testElementSerializer{}
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		SerializeToBytes[*testElement](&tree.RTree, "rstar", ser)
	}
}

func BenchmarkDeserialize(b *testing.B) {
	tree := NewRStarTree[*testElement](8, 0.5)
	for i := 0; i < 1000; i++ {
		x := float64(i * 3)
		tree.Insert(newTestElement(x, x, x+2, x+2, "elem"))
	}
	ser := &testElementSerializer{}
	data, _ := SerializeToBytes[*testElement](&tree.RTree, "rstar", ser)
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		DeserializeRStarTree[*testElement](data, ser)
	}
}

func BenchmarkJSONSerializerMarshal(b *testing.B) {
	e := newTestElement(1, 2, 3, 4, "bench")
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		json.Marshal(e)
	}
}
