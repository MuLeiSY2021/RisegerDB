package main

import (
	"flag"
	"fmt"
	"math"
	"math/rand"
	"os"

	"github.com/riseger/riseger-go/pkg/geodata"
	"github.com/riseger/riseger-go/pkg/geodata/pb"
)

func main() {
	output := flag.String("o", "sample.geodata", "output file path")
	dbName := flag.String("db", "test_db", "database name")
	mapName := flag.String("map", "china_mp", "map name")
	nodeSize := flag.Int("node-size", 4, "R-tree node size")
	threshold := flag.Float64("threshold", 0.5, "coordinate truncation threshold")
	buildingCount := flag.Int("buildings", 3, "buildings per area")
	seed := flag.Int64("seed", 42, "random seed (0 = random)")
	flag.Parse()

	if *seed != 0 {
		rand.Seed(*seed)
	} else {
		rand.Seed(int64(os.Getpid()))
	}

	data := generateSampleData(*dbName, *mapName, int32(*nodeSize), *threshold, *buildingCount)

	if err := geodata.WriteFile(*output, data); err != nil {
		fmt.Fprintf(os.Stderr, "Error writing geodata: %v\n", err)
		os.Exit(1)
	}

	totalElements := countElements(data)
	fi, _ := os.Stat(*output)
	fmt.Printf("Generated %s (%d bytes, %d elements)\n", *output, fi.Size(), totalElements)
}

func generateSampleData(dbName, mapName string, nodeSize int32, threshold float64, buildingsPerArea int) *pb.GeoDataFile {
	provinces := map[string][]string{
		"ShangHai":  {"Huangpu", "Jingan", "Xuhui", "Pudong", "Minhang"},
		"Beijing":   {"Dongcheng", "Xicheng", "Chaoyang", "Haidian", "Fengtai"},
		"Tianjin":   {"Hexi", "Heping", "Hedong", "Nankai", "Hebei"},
		"Chongqing": {"Yuzhong", "Jiangbei", "Shapingba", "Nanan", "Yubei"},
		"Guangzhou":  {"Tianhe", "Yuexiu", "Haizhu", "Panyu", "Baiyun"},
	}

	var submaps []*pb.GeoSubmap
	for province, areas := range provinces {
		pSubmap := &pb.GeoSubmap{
			Name:      province,
			ScopePath: "province_scope",
		}
		for _, area := range areas {
			aSubmap := &pb.GeoSubmap{
				Name:      area,
				ScopePath: "province_scope.area_scope",
			}
			for i := 0; i < buildingsPerArea; i++ {
				aSubmap.Elements = append(aSubmap.Elements, generateBuilding(province, area, i))
			}
			pSubmap.Submaps = append(pSubmap.Submaps, aSubmap)
		}
		submaps = append(submaps, pSubmap)
	}

	return &pb.GeoDataFile{
		Databases: []*pb.GeoDatabase{
			{
				Name: dbName,
				Models: []*pb.GeoModel{
					{
						Name:   "building_model",
						Parent: "field",
						Parameters: map[string]string{
							"name":      "STRING",
							"address":   "STRING",
							"floorArea": "DOUBLE",
							"floors":    "INT",
							"type":      "STRING",
						},
					},
				},
				Maps: []*pb.GeoMap{
					{
						Name:      mapName,
						NodeSize:  nodeSize,
						Threshold: threshold,
						Submaps:   submaps,
					},
				},
			},
		},
	}
}

func generateBuilding(province, area string, index int) *pb.GeoElement {
	baseX := float64(hashStr(province)%8000) + 1000
	baseY := float64(hashStr(area)%8000) + 1000
	x1 := baseX + rand.Float64()*200
	y1 := baseY + rand.Float64()*200
	x2 := x1 + rand.Float64()*100 + 10
	y2 := y1 + rand.Float64()*100 + 10
	floorArea := math.Round(rand.Float64()*4000*100+100000) / 100
	floors := rand.Intn(50) + 1
	types := []string{"residential", "commercial", "office", "industrial", "public"}

	return &pb.GeoElement{
		ModelName:   "building_model",
		ParentModel: "field",
		Coords: []*pb.GeoCoord{
			{X: x1, Y: y1},
			{X: x2, Y: y2},
		},
		Attributes: map[string]string{
			"name":      fmt.Sprintf("Building %s-%s-%d", province, area, index+1),
			"address":   fmt.Sprintf("%s %s Street No.%d", province, area, index+1),
			"floorArea": fmt.Sprintf("%.2f", floorArea),
			"floors":    fmt.Sprintf("%d", floors),
			"type":      types[rand.Intn(len(types))],
		},
	}
}

func hashStr(s string) int {
	h := 0
	for _, c := range s {
		h = h*31 + int(c)
	}
	if h < 0 {
		h = -h
	}
	return h
}

func countElements(data *pb.GeoDataFile) int {
	total := 0
	for _, db := range data.Databases {
		for _, m := range db.Maps {
			total += len(m.Elements)
			for _, sm := range m.Submaps {
				total += countSubmapElements(sm)
			}
		}
	}
	return total
}

func countSubmapElements(sm *pb.GeoSubmap) int {
	total := len(sm.Elements)
	for _, child := range sm.Submaps {
		total += countSubmapElements(child)
	}
	return total
}
