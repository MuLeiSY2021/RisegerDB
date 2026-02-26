package engine

import (
	"fmt"
	"math"
	"strconv"

	"github.com/riseger/riseger-go/internal/cache"
	"github.com/riseger/riseger-go/pkg/geodata"
	gpb "github.com/riseger/riseger-go/pkg/geodata/pb"
)

// ImportGeoData reads a .geodata file and imports all databases/maps/elements
// into the engine. Returns the count of imported elements.
func (e *Engine) ImportGeoData(path string) (int, error) {
	data, err := geodata.ReadFile(path)
	if err != nil {
		return 0, fmt.Errorf("read geodata: %w", err)
	}
	return e.ImportGeoDataProto(data)
}

// ImportGeoDataProto imports from an in-memory GeoDataFile proto.
func (e *Engine) ImportGeoDataProto(data *gpb.GeoDataFile) (int, error) {
	totalElements := 0

	for _, gdb := range data.Databases {
		db, err := e.getOrCreateDatabase(gdb.Name)
		if err != nil {
			return totalElements, err
		}

		for _, gm := range gdb.Models {
			model := protoToModel(gm)
			db.AddModel(model)
		}

		for _, gmap := range gdb.Maps {
			nodeSize := int(gmap.NodeSize)
			if nodeSize <= 0 {
				nodeSize = 4
			}
			threshold := gmap.Threshold
			if threshold <= 0 {
				threshold = 0.5
			}

			m := cache.NewGeoMap(gmap.Name, nodeSize, threshold, db)

			for _, elem := range gmap.Elements {
				e := protoToElement(elem, threshold)
				m.AddElement(e)
				totalElements++
			}

			for _, sm := range gmap.Submaps {
				n := importSubmap(m, sm, threshold)
				totalElements += n
			}

			db.AddMap(m)
		}

		if err := e.Storage.WriteDatabase(db); err != nil {
			e.Logger.Error("persist imported database failed", "name", db.Name, "error", err)
		}
		db.ResetChanged()

		e.Logger.Info("geodata imported", "database", db.Name, "elements", totalElements)
	}

	return totalElements, nil
}

func (e *Engine) getOrCreateDatabase(name string) (*cache.Database, error) {
	if db, ok := e.Cache.GetDatabase(name); ok {
		return db, nil
	}
	db := cache.NewDatabase(name)
	db.Activate()
	e.Cache.AddDatabase(db)
	return db, nil
}

func importSubmap(m *cache.GeoMap, sm *gpb.GeoSubmap, threshold float64) int {
	count := 0
	for _, elem := range sm.Elements {
		e := protoToElement(elem, threshold)
		m.AddElement(e)
		count++
	}
	for _, child := range sm.Submaps {
		count += importSubmap(m, child, threshold)
	}
	return count
}

func protoToModel(gm *gpb.GeoModel) *cache.Model {
	params := make(map[string]cache.FieldType, len(gm.Parameters))
	for k, v := range gm.Parameters {
		params[k] = cache.ParseFieldType(v)
	}
	return cache.NewModel(gm.Name, gm.Parent, params)
}

func protoToElement(ge *gpb.GeoElement, threshold float64) *cache.Element {
	minX, minY := math.MaxFloat64, math.MaxFloat64
	maxX, maxY := -math.MaxFloat64, -math.MaxFloat64

	for _, c := range ge.Coords {
		if c.X < minX {
			minX = c.X
		}
		if c.X > maxX {
			maxX = c.X
		}
		if c.Y < minY {
			minY = c.Y
		}
		if c.Y > maxY {
			maxY = c.Y
		}
	}

	if len(ge.Coords) == 0 {
		minX, minY, maxX, maxY = 0, 0, 0, 0
	}

	elem := cache.NewElement(minX, minY, maxX, maxY, threshold, ge.ParentModel, ge.ModelName)
	for k, v := range ge.Attributes {
		if f, err := strconv.ParseFloat(v, 64); err == nil {
			elem.SetAttribute(k, f)
		} else {
			elem.SetAttribute(k, v)
		}
	}
	return elem
}
