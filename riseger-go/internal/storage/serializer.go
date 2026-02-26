package storage

import (
	"encoding/json"

	"github.com/riseger/riseger-go/pkg/rtree"
)

// elementSerializer implements rtree.ElementSerializer for generic Rectangles.
// Elements are stored as JSON within the protobuf payload field.
type elementSerializer struct{}

func (s *elementSerializer) Marshal(r rtree.Rectangle) ([]byte, error) {
	wrapper := rectWrapper{
		MinX: r.MinX(),
		MaxX: r.MaxX(),
		MinY: r.MinY(),
		MaxY: r.MaxY(),
	}
	return json.Marshal(wrapper)
}

func (s *elementSerializer) Unmarshal(data []byte) (rtree.Rectangle, error) {
	var w rectWrapper
	if err := json.Unmarshal(data, &w); err != nil {
		return nil, err
	}
	return rtree.NewRect(w.MinX, w.MinY, w.MaxX, w.MaxY, 0), nil
}

type rectWrapper struct {
	MinX float64 `json:"minX"`
	MaxX float64 `json:"maxX"`
	MinY float64 `json:"minY"`
	MaxY float64 `json:"maxY"`
}
