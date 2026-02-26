package engine

import (
	"fmt"
	"math/rand"
	"testing"
	"time"

	"github.com/riseger/riseger-go/internal/cache"
	"github.com/riseger/riseger-go/internal/compile"
	cfgpkg "github.com/riseger/riseger-go/internal/config"
	"github.com/riseger/riseger-go/internal/wal"
	geodatapkg "github.com/riseger/riseger-go/pkg/geodata"
	gpb "github.com/riseger/riseger-go/pkg/geodata/pb"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

// ============================================================================
// 功能测试 1: geodata 生成 → 导入 → 查询 → 持久化 → 重启 → 再查询
// ============================================================================

func TestFunctional_ImportAndPersistence(t *testing.T) {
	root := t.TempDir()
	cfg := &cfgpkg.ServerConfig{
		DataDir:          root,
		Port:             0,
		FlushThreshold:   9999,
		FlushIntervalSec: 3600,
	}

	// ---- 第一次启动：导入 geodata ----
	e1, err := New(cfg)
	require.NoError(t, err)
	require.NoError(t, e1.Start())

	geoData := generateTestGeoData()
	count, err := e1.ImportGeoDataProto(geoData)
	require.NoError(t, err)
	assert.Equal(t, 75, count) // 5 省 × 5 区 × 3 建筑 = 75

	// 查询验证
	compiler1 := compile.NewCompiler(e1.Cache)
	rs, err := compiler1.Execute("USE DATABASE test_db | MAP china_mp SEARCH name, floorArea")
	require.NoError(t, err)
	assert.Equal(t, 75, len(rs.Rows))

	// 条件查询
	rs2, err := compiler1.Execute("USE DATABASE test_db | MAP china_mp SEARCH name WHERE floorArea > 3000")
	require.NoError(t, err)
	assert.Greater(t, len(rs2.Rows), 0)
	assert.Less(t, len(rs2.Rows), 75)
	for _, row := range rs2.Rows {
		area, ok := row["floorArea"].(float64)
		if ok {
			assert.Greater(t, area, 3000.0)
		}
	}

	// GET 查询
	rs3, err := compiler1.Execute("USE DATABASE test_db GET MODELS")
	require.NoError(t, err)
	assert.Equal(t, 1, len(rs3.Rows))
	assert.Equal(t, "building_model", rs3.Rows[0]["model"])

	rs4, err := compiler1.Execute("USE DATABASE test_db GET MAPS")
	require.NoError(t, err)
	assert.Equal(t, 1, len(rs4.Rows))
	assert.Equal(t, "china_mp", rs4.Rows[0]["map"])

	require.NoError(t, e1.Stop())

	// ---- 第二次启动：从磁盘加载，验证持久化 ----
	e2, err := New(cfg)
	require.NoError(t, err)
	require.NoError(t, e2.Start())

	db2, ok := e2.GetDatabase("test_db")
	require.True(t, ok, "database should persist after restart")
	assert.Equal(t, cache.StatusActive, db2.Status)

	model, ok := db2.GetModel("building_model")
	require.True(t, ok, "model should persist after restart")
	assert.Equal(t, "field", model.Parent)
	_, hasName := model.GetType("name")
	assert.True(t, hasName)

	maps := db2.ListMaps()
	assert.Equal(t, 1, len(maps), "map should persist after restart")
	assert.Equal(t, "china_mp", maps[0].Name)

	layers := maps[0].ListLayers()
	assert.Greater(t, len(layers), 0, "layers should persist after restart")

	totalElems := 0
	for _, layer := range layers {
		totalElems += len(layer.Elements())
	}
	assert.Equal(t, 75, totalElems, "all elements should persist after restart")

	compiler2 := compile.NewCompiler(e2.Cache)
	rs5, err := compiler2.Execute("USE DATABASE test_db | MAP china_mp SEARCH name, floorArea")
	require.NoError(t, err)
	assert.Equal(t, 75, len(rs5.Rows), "query after restart should return same results")

	require.NoError(t, e2.Stop())
}

// ============================================================================
// 功能测试 2: WAL 日志重放
//
// 流程:
//   1. 启动引擎，导入数据
//   2. 通过 WAL 写入额外的操作日志
//   3. 不调用 flush（模拟突然停止）
//   4. 重启引擎，验证 WAL 条目被正确恢复
// ============================================================================

func TestFunctional_WALRecovery(t *testing.T) {
	root := t.TempDir()
	cfg := &cfgpkg.ServerConfig{
		DataDir:          root,
		Port:             0,
		FlushThreshold:   9999,
		FlushIntervalSec: 3600,
	}

	// ---- 第一次启动：导入基础数据 ----
	e1, err := New(cfg)
	require.NoError(t, err)
	require.NoError(t, e1.Start())

	geoData := generateSmallGeoData()
	count, err := e1.ImportGeoDataProto(geoData)
	require.NoError(t, err)
	assert.Equal(t, 6, count) // 2 省 × 1 区 × 3 建筑 = 6

	compiler1 := compile.NewCompiler(e1.Cache)
	rs, err := compiler1.Execute("USE DATABASE wal_test_db | MAP test_map SEARCH name")
	require.NoError(t, err)
	assert.Equal(t, 6, len(rs.Rows))

	require.NoError(t, e1.Stop())

	// ---- 模拟崩溃：直接向 WAL 目录写入日志文件（绕过 daemon flush） ----
	// 这模拟了进程写入 WAL 后被 kill -9 或突然断电的场景
	walFS := wal.NewFileSystem(cfg.DataDir)
	baseTime := time.Date(2099, 1, 1, 0, 0, 0, 0, time.UTC)
	for i := 0; i < 3; i++ {
		entry := wal.LogEntry{
			Timestamp:    baseTime.Add(time.Duration(i) * time.Second),
			SessionID:    100 + i,
			DatabaseName: "wal_test_db",
			Operations: []wal.Operation{
				{
					Type:      "INSERT",
					MapName:   "test_map",
					ModelName: "building_model",
					Attributes: map[string]interface{}{
						"name":      fmt.Sprintf("WAL_Building_%d", i),
						"floorArea": float64(9000 + i),
					},
				},
			},
		}
		require.NoError(t, walFS.Write(entry))
	}
	logEntries, err := walFS.ReadAll("wal_test_db")
	require.NoError(t, err)
	assert.Equal(t, 3, len(logEntries), "3 WAL log entries should exist")

	// ---- 第二次启动：验证 WAL 恢复 ----
	e2, err := New(cfg)
	require.NoError(t, err)
	require.NoError(t, e2.Start())

	db2, ok := e2.GetDatabase("wal_test_db")
	require.True(t, ok)

	// WAL 中的 INSERT 应该被重放，现在应该有 6 + 3 = 9 个元素
	totalAfterRecovery := countAllElements(db2)
	assert.Equal(t, 9, totalAfterRecovery, "WAL recovery should replay 3 inserts")

	// WAL 日志应该被清理了
	logEntriesAfter, err := walFS.ReadAll("wal_test_db")
	require.NoError(t, err)
	assert.Empty(t, logEntriesAfter, "WAL logs should be cleaned after recovery")

	compiler2 := compile.NewCompiler(e2.Cache)
	rs2, err := compiler2.Execute("USE DATABASE wal_test_db | MAP test_map SEARCH name")
	require.NoError(t, err)
	assert.Equal(t, 9, len(rs2.Rows), "query should return 9 rows after WAL recovery")

	// 验证 WAL 恢复的元素能被查到
	rs3, err := compiler2.Execute("USE DATABASE wal_test_db | MAP test_map SEARCH name WHERE floorArea > 8999")
	require.NoError(t, err)
	assert.Equal(t, 3, len(rs3.Rows), "WAL-recovered elements should be queryable")

	require.NoError(t, e2.Stop())
}

// ============================================================================
// 功能测试 3: 多次重启稳定性
// ============================================================================

func TestFunctional_MultipleRestarts(t *testing.T) {
	root := t.TempDir()
	cfg := &cfgpkg.ServerConfig{
		DataDir:          root,
		Port:             0,
		FlushThreshold:   9999,
		FlushIntervalSec: 3600,
	}

	// 第一次: 创建并导入
	e1, err := New(cfg)
	require.NoError(t, err)
	require.NoError(t, e1.Start())
	_, err = e1.ImportGeoDataProto(generateSmallGeoData())
	require.NoError(t, err)
	require.NoError(t, e1.Stop())

	// 连续重启 5 次，每次验证数据完整
	for round := 0; round < 5; round++ {
		e, err := New(cfg)
		require.NoError(t, err)
		require.NoError(t, e.Start())

		db, ok := e.GetDatabase("wal_test_db")
		require.True(t, ok, "round %d: database should exist", round)
		assert.Equal(t, 6, countAllElements(db), "round %d: should have 6 elements", round)

		require.NoError(t, e.Stop())
	}
}

// ============================================================================
// 辅助函数
// ============================================================================

func generateTestGeoData() *gpb.GeoDataFile {
	provinces := map[string][]string{
		"ShangHai":  {"Huangpu", "Jingan", "Xuhui", "Pudong", "Minhang"},
		"Beijing":   {"Dongcheng", "Xicheng", "Chaoyang", "Haidian", "Fengtai"},
		"Tianjin":   {"Hexi", "Heping", "Hedong", "Nankai", "Hebei"},
		"Chongqing": {"Yuzhong", "Jiangbei", "Shapingba", "Nanan", "Yubei"},
		"Guangzhou":  {"Tianhe", "Yuexiu", "Haizhu", "Panyu", "Baiyun"},
	}
	return buildGeoData("test_db", "china_mp", provinces, 3)
}

func generateSmallGeoData() *gpb.GeoDataFile {
	provinces := map[string][]string{
		"CityA": {"Area1"},
		"CityB": {"Area1"},
	}
	return buildGeoData("wal_test_db", "test_map", provinces, 3)
}

func buildGeoData(dbName, mapName string, provinces map[string][]string, buildingsPerArea int) *gpb.GeoDataFile {
	rng := rand.New(rand.NewSource(42))
	var submaps []*gpb.GeoSubmap
	for province, areas := range provinces {
		ps := &gpb.GeoSubmap{Name: province, ScopePath: "province_scope"}
		for _, area := range areas {
			as := &gpb.GeoSubmap{Name: area, ScopePath: "province_scope.area_scope"}
			for i := 0; i < buildingsPerArea; i++ {
				x := rng.Float64() * 10000
				y := rng.Float64() * 10000
				as.Elements = append(as.Elements, &gpb.GeoElement{
					ModelName:   "building_model",
					ParentModel: "field",
					Coords:      []*gpb.GeoCoord{{X: x, Y: y}, {X: x + 50, Y: y + 50}},
					Attributes: map[string]string{
						"name":      fmt.Sprintf("%s-%s-B%d", province, area, i+1),
						"address":   fmt.Sprintf("%s %s", province, area),
						"floorArea": fmt.Sprintf("%.2f", rng.Float64()*5000+500),
					},
				})
			}
			ps.Submaps = append(ps.Submaps, as)
		}
		submaps = append(submaps, ps)
	}

	return &gpb.GeoDataFile{
		Databases: []*gpb.GeoDatabase{{
			Name: dbName,
			Models: []*gpb.GeoModel{{
				Name: "building_model", Parent: "field",
				Parameters: map[string]string{"name": "STRING", "address": "STRING", "floorArea": "DOUBLE"},
			}},
			Maps: []*gpb.GeoMap{{
				Name: mapName, NodeSize: 4, Threshold: 0.5, Submaps: submaps,
			}},
		}},
	}
}

// ============================================================================
// 功能测试 4: PRELOAD SQL 命令端到端
//
// 流程:
//   1. 用 geodata-gen 生成 .geodata 文件
//   2. 启动引擎 + 编译器（注入 Preload handler）
//   3. 执行 PRELOAD 'path.geodata' SQL
//   4. 等待异步导入完成
//   5. 验证数据已导入并可查询
// ============================================================================

func TestFunctional_PreloadSQL(t *testing.T) {
	root := t.TempDir()
	cfg := &cfgpkg.ServerConfig{
		DataDir:          root,
		Port:             0,
		FlushThreshold:   9999,
		FlushIntervalSec: 3600,
	}

	geodataPath := root + "/test.geodata"
	geoData := generateSmallGeoData()
	require.NoError(t, geodataWriteFile(geodataPath, geoData))

	e, err := New(cfg)
	require.NoError(t, err)
	require.NoError(t, e.Start())
	defer e.Stop()

	compiler := compile.NewCompiler(e.Cache)
	compiler.SetPreloadHandler(e.Preload)

	rs, err := compiler.Execute(fmt.Sprintf("PRELOAD '%s'", geodataPath))
	require.NoError(t, err)
	require.NotNil(t, rs)
	assert.Equal(t, 1, len(rs.Rows))
	assert.Equal(t, "accepted", rs.Rows[0]["status"])
	taskID := rs.Rows[0]["task_id"].(string)
	assert.NotEmpty(t, taskID)

	// 等待异步导入完成（最多 5 秒）
	for i := 0; i < 50; i++ {
		time.Sleep(100 * time.Millisecond)
		task, ok := e.GetPreloadTask(taskID)
		if ok && (task.Status == "done" || task.Status == "failed") {
			break
		}
	}

	task, ok := e.GetPreloadTask(taskID)
	require.True(t, ok)
	assert.Equal(t, "done", task.Status, "preload task should succeed")
	assert.Equal(t, 6, task.Elements)

	db, ok := e.GetDatabase("wal_test_db")
	require.True(t, ok, "database should exist after PRELOAD")
	assert.Equal(t, 6, countAllElements(db))

	rs2, err := compiler.Execute("USE DATABASE wal_test_db | MAP test_map SEARCH name, floorArea")
	require.NoError(t, err)
	assert.Equal(t, 6, len(rs2.Rows))
}

func geodataWriteFile(path string, data *gpb.GeoDataFile) error {
	return geodatapkg.WriteFile(path, data)
}

func countAllElements(db *cache.Database) int {
	total := 0
	for _, m := range db.ListMaps() {
		for _, l := range m.ListLayers() {
			total += len(l.Elements())
		}
	}
	return total
}
