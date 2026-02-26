package rtree

import (
	"encoding/json"
	"fmt"
	"io"

	"github.com/riseger/riseger-go/pkg/rtree/pb"
	"google.golang.org/protobuf/proto"
)

// ElementSerializer handles custom element serialization/deserialization
// within leaf nodes. Users must provide this when working with concrete
// element types beyond plain Rect.
type ElementSerializer[R Rectangle] interface {
	Marshal(r R) ([]byte, error)
	Unmarshal(data []byte) (R, error)
}

// JSONSerializer is a default ElementSerializer using JSON encoding.
// It works for any type that supports json.Marshal/Unmarshal.
type JSONSerializer[R Rectangle] struct {
	factory func() R
}

func NewJSONSerializer[R Rectangle](factory func() R) *JSONSerializer[R] {
	return &JSONSerializer[R]{factory: factory}
}

func (s *JSONSerializer[R]) Marshal(r R) ([]byte, error) {
	return json.Marshal(r)
}

func (s *JSONSerializer[R]) Unmarshal(data []byte) (R, error) {
	v := s.factory()
	if err := json.Unmarshal(data, v); err != nil {
		var zero R
		return zero, err
	}
	return v, nil
}

// ---------- serialize ----------

// SerializeToProto converts the tree into a protobuf message.
func SerializeToProto[R Rectangle](t *RTree[R], treeType string, ser ElementSerializer[R]) (*pb.RTreeProto, error) {
	rootNode, err := serializeNode(t.root, ser)
	if err != nil {
		return nil, err
	}
	return &pb.RTreeProto{
		MaxEntries: int32(t.MaxEntries),
		Threshold:  t.Threshold,
		TreeType:   treeType,
		Root:       rootNode,
	}, nil
}

// SerializeToBytes encodes the tree into a compressed protobuf byte slice.
func SerializeToBytes[R Rectangle](t *RTree[R], treeType string, ser ElementSerializer[R]) ([]byte, error) {
	msg, err := SerializeToProto(t, treeType, ser)
	if err != nil {
		return nil, err
	}
	return proto.Marshal(msg)
}

// SerializeToWriter writes the protobuf-encoded tree to a writer.
func SerializeToWriter[R Rectangle](t *RTree[R], treeType string, ser ElementSerializer[R], w io.Writer) error {
	data, err := SerializeToBytes(t, treeType, ser)
	if err != nil {
		return err
	}
	_, err = w.Write(data)
	return err
}

func serializeNode[R Rectangle](n *node[R], ser ElementSerializer[R]) (*pb.Node, error) {
	pn := &pb.Node{
		Bounds: &pb.Rect{
			MinX: n.MinX(),
			MaxX: n.MaxX(),
			MinY: n.MinY(),
			MaxY: n.MaxY(),
		},
		IsLeaf: n.isLeaf,
	}

	if n.isLeaf {
		pn.Elements = make([]*pb.Element, len(n.elements))
		for i, e := range n.elements {
			payload, err := ser.Marshal(e)
			if err != nil {
				return nil, fmt.Errorf("serialize element %d: %w", i, err)
			}
			pn.Elements[i] = &pb.Element{
				Bounds: &pb.Rect{
					MinX: e.MinX(),
					MaxX: e.MaxX(),
					MinY: e.MinY(),
					MaxY: e.MaxY(),
				},
				Payload: payload,
			}
		}
	} else {
		pn.Children = make([]*pb.Node, len(n.children))
		for i, child := range n.children {
			cn, err := serializeNode(child, ser)
			if err != nil {
				return nil, err
			}
			pn.Children[i] = cn
		}
	}
	return pn, nil
}

// ---------- deserialize ----------

// DeserializeRStarTree reconstructs an RStarTree from protobuf bytes.
func DeserializeRStarTree[R Rectangle](data []byte, ser ElementSerializer[R]) (*RStarTree[R], error) {
	msg := &pb.RTreeProto{}
	if err := proto.Unmarshal(data, msg); err != nil {
		return nil, fmt.Errorf("unmarshal protobuf: %w", err)
	}
	tree := NewRStarTree[R](int(msg.MaxEntries), msg.Threshold)
	root, err := deserializeNode[R](msg.Root, nil, msg.Threshold, ser)
	if err != nil {
		return nil, err
	}
	tree.root = root
	return tree, nil
}

// DeserializeSTRRTree reconstructs an STRRTree from protobuf bytes.
func DeserializeSTRRTree[R Rectangle](data []byte, ser ElementSerializer[R]) (*STRRTree[R], error) {
	msg := &pb.RTreeProto{}
	if err := proto.Unmarshal(data, msg); err != nil {
		return nil, fmt.Errorf("unmarshal protobuf: %w", err)
	}
	tree := NewSTRRTree[R](int(msg.MaxEntries), msg.Threshold)
	root, err := deserializeNode[R](msg.Root, nil, msg.Threshold, ser)
	if err != nil {
		return nil, err
	}
	tree.root = root
	return tree, nil
}

// DeserializeFromReader reads all bytes from reader then deserializes.
func DeserializeFromReader[R Rectangle](r io.Reader, treeType string, ser ElementSerializer[R]) (*RStarTree[R], error) {
	data, err := io.ReadAll(r)
	if err != nil {
		return nil, fmt.Errorf("read data: %w", err)
	}
	return DeserializeRStarTree[R](data, ser)
}

func deserializeNode[R Rectangle](pn *pb.Node, parent *node[R], threshold float64, ser ElementSerializer[R]) (*node[R], error) {
	if pn == nil {
		return newLeaf[R](threshold), nil
	}

	n := &node[R]{
		Rect:   *NewRect(pn.Bounds.MinX, pn.Bounds.MinY, pn.Bounds.MaxX, pn.Bounds.MaxY, threshold),
		isLeaf: pn.IsLeaf,
		parent: parent,
	}

	if pn.IsLeaf {
		n.elements = make([]R, len(pn.Elements))
		for i, pe := range pn.Elements {
			elem, err := ser.Unmarshal(pe.Payload)
			if err != nil {
				return nil, fmt.Errorf("deserialize element %d: %w", i, err)
			}
			n.elements[i] = elem
		}
	} else {
		n.children = make([]*node[R], len(pn.Children))
		for i, pc := range pn.Children {
			child, err := deserializeNode[R](pc, n, threshold, ser)
			if err != nil {
				return nil, err
			}
			n.children[i] = child
		}
	}
	return n, nil
}
