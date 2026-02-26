# RisegerDB Java → Go 迁移方案

## 一、项目概述

**RisegerDB**（Rapid Search of Geographic Database）是一个基于 R-tree/R\*-tree 空间索引的地理数据库系统。当前使用 Java 编写，采用 Maven 多模块项目结构，包含以下核心能力：

- 空间数据存储与检索（R-tree/R\*-tree 索引）
- 自定义 SQL-like 查询语言（词法分析 → 语法分析 → 语义分析 → 函数编译）
- 层级数据模型（Database → Map → Layer → Element）
- 基于 Netty 的网络服务
- WAL 预写日志保证持久性
- 多线程并发查询处理
- JDBC 连接驱动与命令行客户端

---

## 二、现有架构分析

### 2.1 模块清单

| Java 模块 | 功能 | 代码量（估计） | 迁移优先级 |
|---|---|---|---|
| `module-util-rtree` | R-tree/R\*-tree 空间索引核心 | ~1300 行 | P0（最高） |
| `riseger-core` | 数据库引擎核心 | ~3000+ 行 | P0 |
| `riseger-core/protocol` | 协议层、序列化、编解码 | ~800 行 | P0 |
| `module-connectivity-jrdbc` | JDBC 驱动 | ~500 行 | P2（Go 不需要 JDBC） |
| `module-client-shell` | 命令行客户端 | ~400 行 | P1 |
| `module-util-preload` | 数据预加载工具 | ~200 行 | P2 |
| `module-jmeter-tesing` | 压力测试 | ~100 行 | P3（用 Go 原生测试替代） |

### 2.2 核心组件依赖关系

```
┌──────────────────────────────────────────────────────────────┐
│                      NettyServer (网络层)                      │
│                     HandlerManager / Codec                    │
└─────────────────────────┬────────────────────────────────────┘
                          │ Protocol (BasicPacket/Request/Response)
                          ▼
┌──────────────────────────────────────────────────────────────┐
│                    WorkflowSystem (工作流)                      │
│              WorkerPool / JobStack / Adapter                  │
└────────┬────────────────┬──────────────────┬─────────────────┘
         │                │                  │
         ▼                ▼                  ▼
┌────────────┐  ┌──────────────┐  ┌──────────────────┐
│CompileSystem│  │ CacheSystem  │  │   LogSystem       │
│ Lexicator   │  │DatabasesMgr  │  │  LogDaemon        │
│ Parser      │  │ MapManager   │  │  LogFileSystem    │
│ SemanticTree│  │LayerManager  │  │  WAL Recovery     │
│ Functions   │  │ElementManager│  │                    │
└────────────┘  └──────┬───────┘  └────────────────────┘
                       │
                       ▼
              ┌────────────────┐
              │ StorageSystem  │
              │ File-based I/O │
              │ R-tree序列化    │
              └────────┬───────┘
                       │
                       ▼
              ┌────────────────┐
              │  R-tree Module │
              │ RTree/RStarTree│
              │ MBRectangle    │
              └────────────────┘
```

### 2.3 Java 特有模式分析

迁移时需要特别注意以下 Java 特有模式在 Go 中的对应实现：

| Java 模式 | 使用场景 | Go 对应方案 |
|---|---|---|
| 泛型 (`R extends MBRectangle`) | R-tree 核心 | Go 1.18+ 泛型 / interface |
| 抽象类 + 继承 | `RTree`, `MBRectangle`, `TransponderHandler` | 组合 + 接口 (Composition over Inheritance) |
| 内部类 | `RTree.SubTree`, `RTree.Leaf`, `CommandTree.Node` | 同包内独立结构体 |
| Lombok 注解 | `@Getter`, `@Data` | Go 结构体导出字段 (天然支持) |
| 反射 (`Class.forName()`) | R-tree 反序列化 | `reflect` 包 / type registry 模式 |
| `ConcurrentHashMap` | Element 属性存储 | `sync.Map` 或 `map` + `sync.RWMutex` |
| `ReentrantLock` + `Condition` | Handler 同步 | `sync.Mutex` + `sync.Cond` / channel |
| Stream API | R\*-tree 排序 | `sort.Slice` / 自定义排序 |
| Netty EventLoop | 异步网络 I/O | `net` 标准库 / gnet / gRPC |
| Gson JSON 序列化 | 协议编解码 | `encoding/json` / `json-iterator` |
| dom4j XML 解析 | 配置文件解析 | `encoding/xml` 标准库 |
| Log4j | 日志 | `log/slog`（Go 1.21+）/ `zap` / `zerolog` |
| Maven 多模块 | 构建系统 | Go Modules + 内部包结构 |
| JUnit | 测试 | `testing` 标准库 |

---

## 三、Go 项目架构设计

### 3.1 整体项目结构

```
riseger-go/
├── go.mod
├── go.sum
├── cmd/
│   ├── riseger-server/          # 数据库服务端入口
│   │   └── main.go
│   └── riseger-cli/             # 命令行客户端入口
│       └── main.go
│
├── internal/                    # 内部包（不对外暴露）
│   ├── engine/                  # 数据库引擎核心
│   │   ├── bootstrap.go         # 初始化流程
│   │   ├── config.go            # 配置管理
│   │   └── engine.go            # 引擎主结构
│   │
│   ├── storage/                 # 存储系统
│   │   ├── storage.go           # 存储管理器
│   │   ├── fileio.go            # 文件I/O操作
│   │   └── serializer.go        # 序列化/反序列化
│   │
│   ├── cache/                   # 缓存系统
│   │   ├── cache.go             # 缓存管理器
│   │   ├── database.go          # Database 实体
│   │   ├── geomap.go            # GeoMap 实体
│   │   ├── layer.go             # Layer 实体
│   │   ├── element.go           # Element 实体
│   │   ├── model.go             # Model/Schema 定义
│   │   └── manager/             # 各层级管理器
│   │       ├── databases.go
│   │       ├── maps.go
│   │       ├── layers.go
│   │       ├── elements.go
│   │       ├── models.go
│   │       └── sessions.go
│   │
│   ├── compile/                 # 查询编译系统
│   │   ├── compiler.go          # 编译器主入口
│   │   ├── lexer/               # 词法分析
│   │   │   ├── lexer.go
│   │   │   ├── token.go
│   │   │   └── keyword.go
│   │   ├── parser/              # 语法分析
│   │   │   ├── parser.go
│   │   │   ├── syntax_forest.go
│   │   │   ├── syntax_node.go
│   │   │   └── syntax_rule.go
│   │   ├── semantic/            # 语义分析
│   │   │   ├── tree.go
│   │   │   └── iterator.go
│   │   └── function/            # 可执行函数
│   │       ├── function.go      # Function 接口
│   │       ├── search.go
│   │       ├── where.go
│   │       ├── use.go
│   │       ├── update.go
│   │       ├── math.go          # 数学运算函数
│   │       ├── logic.go         # 逻辑运算函数
│   │       ├── spatial.go       # 空间运算函数 (In/Out)
│   │       └── entity.go        # 实体相关函数
│   │
│   ├── wal/                     # 预写日志系统
│   │   ├── log.go               # 日志管理器
│   │   ├── daemon.go            # 后台写入协程
│   │   ├── filesystem.go        # 日志文件管理
│   │   └── recovery.go          # 崩溃恢复
│   │
│   ├── workflow/                # 工作流系统
│   │   ├── workflow.go          # 工作流管理
│   │   ├── workerpool.go        # goroutine 工作池
│   │   ├── jobstack.go          # 任务队列
│   │   └── adapter.go           # 请求适配器
│   │
│   └── server/                  # 网络服务层
│       ├── server.go            # TCP 服务器
│       ├── handler.go           # 请求处理器
│       └── shell.go             # Shell 服务器
│
├── pkg/                         # 可对外暴露的公共包
│   ├── rtree/                   # R-tree 空间索引库
│   │   ├── rtree.go             # R-tree 基础实现
│   │   ├── rstartree.go         # R*-tree 实现
│   │   ├── strtree.go           # STR R-tree 实现
│   │   ├── rectangle.go         # Rectangle 接口与实现
│   │   ├── mbrectangle.go       # MBR 最小包围矩形
│   │   ├── node.go              # 节点定义 (SubTree/Leaf)
│   │   ├── serialize.go         # 序列化
│   │   └── rtree_test.go        # 测试
│   │
│   └── protocol/                # 通信协议定义
│       ├── packet.go            # 数据包定义
│       ├── request.go           # 请求类型
│       ├── response.go          # 响应类型
│       ├── command.go           # CommandTree 定义
│       ├── codec.go             # 编解码器
│       ├── function.go          # 协议层函数定义
│       └── serializer.go        # JSON 序列化器
│
├── configs/                     # 配置文件
│   └── config.yaml              # YAML 格式配置（替代 XML）
│
└── deploy/                      # 部署相关
    ├── Dockerfile
    ├── docker-compose.yml
    └── scripts/
        └── start.sh
```

### 3.2 Go 模块依赖规划

```go
// go.mod
module github.com/riseger/riseger-go

go 1.22

require (
    go.uber.org/zap      v1.27.0   // 高性能结构化日志
    gopkg.in/yaml.v3     v3.0.1    // YAML 配置解析
    github.com/stretchr/testify v1.9.0 // 测试断言
)
```

> 设计理念：**最小化外部依赖**，充分利用 Go 标准库。网络用 `net`，JSON 用 `encoding/json`，并发用 goroutine + channel。

---

## 四、分阶段迁移计划

### 阶段总览

```
阶段1 (基础层)     阶段2 (核心引擎)      阶段3 (查询系统)     阶段4 (网络/客户端)    阶段5 (集成/优化)
  2-3周               3-4周                3-4周                2-3周                 2-3周
┌───────────┐   ┌──────────────┐   ┌──────────────┐   ┌──────────────┐   ┌──────────────┐
│ R-tree    │   │ 存储系统      │   │ 词法分析器    │   │ TCP 服务器    │   │ 端到端测试    │
│ Rectangle │──▶│ 缓存系统      │──▶│ 语法分析器    │──▶│ 协议编解码    │──▶│ 性能调优      │
│ MBR       │   │ 数据模型      │   │ 语义分析器    │   │ 请求处理      │   │ 客户端 CLI    │
│ 序列化     │   │ WAL日志系统   │   │ 函数编译执行   │   │ 工作流系统    │   │ 压力测试      │
└───────────┘   └──────────────┘   └──────────────┘   └──────────────┘   └──────────────┘
```

---

### 阶段 1：基础数据结构层（预计 2-3 周）

**目标**：迁移 R-tree 模块，这是整个系统的空间索引基石。

#### 1.1 Rectangle 接口

```go
// pkg/rtree/rectangle.go

type Rectangle interface {
    MaxX() float64
    MaxY() float64
    MinX() float64
    MinY() float64
    Area() float64
    Margin() float64
    Width() float64
    Height() float64
    Expand(other Rectangle) Rectangle
    Copy() Rectangle
    Intersects(other Rectangle) bool
    Contains(other Rectangle) bool
    Distance(other Rectangle) float64
    Overlap(other Rectangle) float64
    IsValid() bool
}
```

**迁移要点**：
- Java 的 `interface default method` → Go 中使用嵌入结构体实现默认行为
- `expand(Rectangle... rects)` 可变参数 → Go 的 `...Rectangle`

#### 1.2 MBRectangle

```go
// pkg/rtree/mbrectangle.go

type MBRectangle struct {
    minX, maxX, minY, maxY float64
    threshold              float64
}

func NewMBRectangle(minX, minY, maxX, maxY, threshold float64) *MBRectangle { ... }
func (m *MBRectangle) Serialize(w io.Writer) error { ... }
func DeserializeMBR(r io.Reader) (*MBRectangle, error) { ... }
```

**迁移要点**：
- Java 的 `Double` 包装类型 → Go 的 `float64`
- Netty `ByteBuf` 序列化 → Go `io.Reader`/`io.Writer` 或 `encoding/binary`
- Java 抽象类 → Go 结构体嵌入

#### 1.3 R-tree / R\*-tree

```go
// pkg/rtree/rtree.go

type RTree[R Rectangle] struct {
    root      *node[R]
    size      int
    maxEntries int
    minEntries int
}

type node[R Rectangle] struct {
    mbr      *MBRectangle
    children []*node[R]
    elements []R
    parent   *node[R]
    isLeaf   bool
}

func NewRTree[R Rectangle](maxEntries int) *RTree[R] { ... }
func (t *RTree[R]) Insert(rect R) { ... }
func (t *RTree[R]) Delete(rect Rectangle) bool { ... }
func (t *RTree[R]) Search(scope Rectangle) []R { ... }
func (t *RTree[R]) Serialize(w io.Writer) error { ... }
func DeserializeRTree[R Rectangle](r io.Reader) (*RTree[R], error) { ... }
```

**迁移要点**：
- Java 泛型 `R extends MBRectangle` → Go 1.18+ 泛型 `R Rectangle`（类型约束）
- 内部类 `SubTree`/`Leaf` → 统一为 `node` 结构体，通过 `isLeaf` 标志区分
- Java 的 `Class.forName()` 反射反序列化 → Go 类型注册表（type registry）

```go
// pkg/rtree/rstartree.go

type RStarTree[R Rectangle] struct {
    RTree[R]                    // 嵌入基础 R-tree
    reinsertPercentage float64  // R*-tree 特有参数
}

func NewRStarTree[R Rectangle](maxEntries int) *RStarTree[R] { ... }
func (t *RStarTree[R]) Insert(rect R) { ... }  // 覆盖，加入溢出处理
```

**验收标准**：
- [ ] 通过 R-tree 插入、删除、搜索的单元测试
- [ ] R\*-tree 溢出处理和重新插入通过测试
- [ ] 序列化/反序列化与 Java 版本兼容（可选，或定义新格式）
- [ ] 基准测试对比 Java 版本性能

---

### 阶段 2：存储与缓存层（预计 3-4 周）

#### 2.1 数据模型迁移

```go
// internal/cache/element.go

type Element struct {
    GeoRectangle                            // 嵌入空间矩形
    Attributes map[string]interface{}       // 属性存储
    Model      *Model                       // 关联模型
    mu         sync.RWMutex                 // 并发保护
}

// internal/cache/database.go
type Database struct {
    Name       string
    Maps       map[string]*GeoMap
    Models     map[string]*Model
    Config     *DatabaseConfig
    mu         sync.RWMutex
}

// internal/cache/geomap.go
type GeoMap struct {
    Name       string
    Layers     map[string]*Layer
    Config     *MapConfig
}

// internal/cache/layer.go
type Layer struct {
    Name       string
    Elements   *ElementManager  // R-tree 管理
    SubMaps    map[string]*GeoMap
}
```

**迁移要点**：
- Java `ConcurrentHashMap` → Go `sync.RWMutex` + `map`（性能更可控）
- Java `transient` 字段 → Go 中使用 `json:"-"` tag 或独立管理
- Lombok `@Getter`/`@Data` → Go 导出字段，天然支持

#### 2.2 存储系统

```go
// internal/storage/storage.go

type StorageManager struct {
    basePath string
    logger   *zap.Logger
}

func (s *StorageManager) LoadDatabases() ([]*cache.Database, error) { ... }
func (s *StorageManager) WriteDatabase(db *cache.Database) error { ... }
func (s *StorageManager) WriteMap(m *cache.GeoMap, path string) error { ... }
func (s *StorageManager) WriteLayer(l *cache.Layer, path string) error { ... }
func (s *StorageManager) SerializeRTree(tree *rtree.RTree, path string) error { ... }
```

**迁移要点**：
- Java `File` 操作 → Go `os` / `filepath` 包
- 递归目录遍历 → `filepath.WalkDir`
- JSON 序列化 → `encoding/json`

#### 2.3 缓存系统

```go
// internal/cache/cache.go

type CacheManager struct {
    databases map[string]*Database
    mu        sync.RWMutex
}

func (c *CacheManager) GetDatabase(name string) (*Database, bool) { ... }
func (c *CacheManager) ListDatabases() []string { ... }
```

#### 2.4 WAL 日志系统

```go
// internal/wal/log.go

type WALManager struct {
    logDir     string
    daemon     *LogDaemon
    fs         *LogFileSystem
    logger     *zap.Logger
}

// internal/wal/daemon.go
type LogDaemon struct {
    queue  chan LogEntry
    done   chan struct{}
    writer io.Writer
}

func (d *LogDaemon) Start(ctx context.Context) { ... }  // 使用 context 控制生命周期
func (d *LogDaemon) Write(entry LogEntry) { ... }

// internal/wal/recovery.go
func (w *WALManager) Recover() error { ... }  // 崩溃恢复
```

**迁移要点**：
- Java `BlockingQueue` → Go `chan`（天然适配）
- Java `Thread` + `Daemon` → Go `goroutine` + `context.Context`
- 生命周期管理用 `context` 而非 Java 的中断机制

**验收标准**：
- [ ] 数据库/Map/Layer/Element 层级结构存取正确
- [ ] R-tree 索引能正确序列化到磁盘并恢复
- [ ] WAL 写入和崩溃恢复通过测试
- [ ] 并发读写安全性测试

---

### 阶段 3：查询编译系统（预计 3-4 周）

#### 3.1 词法分析器

```go
// internal/compile/lexer/lexer.go

type Lexer struct {
    keywords *TrieTree  // 关键字前缀树
}

type Token struct {
    Type  TokenType
    Value string
    Line  int
    Col   int
}

type TokenType int
const (
    TokenNumber TokenType = iota
    TokenKeyword
    TokenString
    TokenOperator
    TokenPunctuation
    // ...
)

func (l *Lexer) Tokenize(input string) ([]Token, error) { ... }
```

**迁移要点**：
- Java `MultiBranchesTree<Keyword>` → Go 自实现 Trie 树
- Java `Pattern` 正则 → Go `regexp` 包

#### 3.2 语法分析器

```go
// internal/compile/parser/parser.go

type Parser struct {
    forest *SyntaxForest
}

type SyntaxNode struct {
    Type     string
    Children []*SyntaxNode
    Token    *lexer.Token
}

func (p *Parser) Parse(tokens []lexer.Token) (*SemanticTree, error) { ... }
```

#### 3.3 函数编译与执行

```go
// internal/compile/function/function.go

type Function interface {
    Execute(ctx *ExecutionContext) (interface{}, error)
    Name() string
}

type ExecutionContext struct {
    Session  *Session
    Database *cache.Database
    Map      *cache.GeoMap
    Layer    *cache.Layer
    Variables map[string]interface{}
}

// 示例：Search 函数
type SearchFunc struct {
    Attributes []string
    Where      Function  // 条件函数
}

func (f *SearchFunc) Execute(ctx *ExecutionContext) (interface{}, error) { ... }
```

**迁移要点**：
- Java 的函数类继承体系 → Go 接口 + 结构体组合
- `Function_c` 基类 → `Function` 接口
- Java 的多态 → Go 接口的隐式实现
- `CommandTree.Node` 内部类 → 独立结构体

**验收标准**：
- [ ] 词法分析器能正确分词所有 SQL 语法
- [ ] 语法分析器能生成正确的语法树
- [ ] 基本查询（USE, SEARCH, WHERE, UPDATE）能正确编译和执行
- [ ] 空间查询函数（In, Out, Distance）通过测试

---

### 阶段 4：网络与客户端（预计 2-3 周）

#### 4.1 TCP 服务器

```go
// internal/server/server.go

type Server struct {
    listener net.Listener
    codec    *Codec
    handler  *RequestHandler
    logger   *zap.Logger
}

func (s *Server) Start(ctx context.Context) error {
    for {
        conn, err := s.listener.Accept()
        if err != nil {
            // 处理错误
            continue
        }
        go s.handleConnection(ctx, conn)  // 每连接一个 goroutine
    }
}

func (s *Server) handleConnection(ctx context.Context, conn net.Conn) { ... }
```

**迁移要点**：
- Netty EventLoop → Go `net.Listener` + goroutine-per-connection
  - Go 的 goroutine 模型天然适合高并发连接
  - 无需 Java 中 boss/worker EventLoopGroup 的概念
- Netty `ChannelPipeline` → 函数式中间件链或直接顺序处理
- Netty `ByteToMessageCodec` → 自定义读写协议

#### 4.2 协议编解码

```go
// pkg/protocol/codec.go

type Codec struct{}

func (c *Codec) Encode(packet *Packet) ([]byte, error) {
    // 1 byte: PacketType
    // 4 bytes: payload length (big-endian)
    // N bytes: JSON payload
    ...
}

func (c *Codec) Decode(r io.Reader) (*Packet, error) { ... }
```

**迁移要点**：
- Netty `ByteBuf` → Go `bytes.Buffer` / `encoding/binary`
- Gson 序列化 → `encoding/json`
- 自定义 `TypeAdapter` → Go `json.Marshaler`/`json.Unmarshaler` 接口

#### 4.3 工作流系统

```go
// internal/workflow/workerpool.go

type WorkerPool struct {
    tasks    chan Task
    workers  int
    wg       sync.WaitGroup
}

func NewWorkerPool(size int, queueSize int) *WorkerPool { ... }
func (p *WorkerPool) Submit(task Task) { ... }
func (p *WorkerPool) Start(ctx context.Context) { ... }
func (p *WorkerPool) Shutdown() { ... }
```

**迁移要点**：
- Java `ThreadPoolExecutor` → Go worker pool 模式（goroutine + channel）
- Java `BlockingQueue` → Go buffered channel
- Java `Future` → Go channel 或 `errgroup`

#### 4.4 命令行客户端

```go
// cmd/riseger-cli/main.go

func main() {
    // 使用 Go 标准库或 cobra 构建 CLI
    conn, _ := net.Dial("tcp", address)
    codec := protocol.NewCodec()
    // 交互式 REPL
    scanner := bufio.NewScanner(os.Stdin)
    for scanner.Scan() {
        // 发送查询，接收结果
    }
}
```

**迁移要点**：
- Java `DatabaseShellClient` → Go 简洁的 REPL
- 终端美化用 `github.com/fatih/color` 或 `github.com/charmbracelet/lipgloss`
- JDBC 模块 **不迁移**，改为提供原生 Go driver（database/sql 接口）

**验收标准**：
- [ ] TCP 服务能接受连接并正确处理请求
- [ ] 协议编解码与 Java 版本兼容（如果需要互操作）
- [ ] Worker pool 能正确分发和执行任务
- [ ] CLI 客户端能交互式执行查询

---

### 阶段 5：集成测试与优化（预计 2-3 周）

#### 5.1 端到端测试

- 启动服务 → 创建数据库 → 插入数据 → 查询 → 更新 → 删除
- 崩溃恢复测试：写入后强制退出，重启验证数据完整性
- 并发压力测试：多 goroutine 并发读写

#### 5.2 性能优化

- 内存池（`sync.Pool`）用于频繁分配的对象
- R-tree 查询性能基准测试
- pprof 性能分析
- 可能的优化方向：
  - 使用 `mmap` 替代文件 I/O
  - 更高效的序列化格式（Protocol Buffers / MessagePack）
  - 连接池管理

#### 5.3 代码质量

- `golangci-lint` 静态分析
- `go vet` 检查
- 代码覆盖率 ≥ 70%

---

## 五、关键技术决策

### 5.1 网络框架选择

| 方案 | 优点 | 缺点 | 推荐 |
|---|---|---|---|
| `net` 标准库 | 零依赖、goroutine 天然适配 | 需手写编解码 | ✅ **推荐** |
| `gnet` | 高性能、事件驱动 | 引入外部依赖、增加复杂度 | 超高并发场景备选 |
| `gRPC` | 类型安全、代码生成、跨语言 | 协议不兼容 Java 版本 | 如果放弃兼容性可选 |

**建议**：初期使用 `net` 标准库，保持与 Java 版本的协议兼容。后续可按需切换。

### 5.2 序列化格式

| 方案 | 场景 | 推荐 |
|---|---|---|
| `encoding/json` | 配置文件、协议消息 | ✅ 初期使用 |
| `encoding/binary` | R-tree 节点序列化 | ✅ 替代 Netty ByteBuf |
| Protocol Buffers | 协议消息（如果重新设计协议） | 🔄 后续优化 |
| `encoding/gob` | Go 内部数据持久化 | ⚠️ 不跨语言 |

### 5.3 配置格式

- **XML → YAML**：Go 社区更常用 YAML，更简洁易读
- 使用 `gopkg.in/yaml.v3` 解析

### 5.4 并发模型

| Java 模式 | Go 替代方案 |
|---|---|
| `ThreadPoolExecutor` | goroutine + buffered channel |
| `synchronized` / `ReentrantLock` | `sync.Mutex` / `sync.RWMutex` |
| `ConcurrentHashMap` | `sync.Map` 或 `map` + `RWMutex` |
| `BlockingQueue` | buffered channel |
| `Condition` | `sync.Cond` 或 channel |
| `AtomicInteger` | `atomic.Int64` |
| `Future` | channel / `errgroup.Group` |

### 5.5 Go driver（替代 JDBC）

```go
// 未来提供标准 database/sql 接口
import "database/sql/driver"

type RisegerDriver struct{}
func (d *RisegerDriver) Open(dsn string) (driver.Conn, error) { ... }

// 用户使用方式：
// db, _ := sql.Open("riseger", "localhost:12000/mydb")
// rows, _ := db.Query("SEARCH attr1, attr2 WHERE ...")
```

---

## 六、风险评估与应对

| 风险 | 影响 | 概率 | 应对策略 |
|---|---|---|---|
| R-tree 泛型实现复杂度 | 高 | 中 | Go 1.18+ 泛型已支持，可先用 interface 兜底 |
| 查询编译器迁移工作量大 | 高 | 高 | 可考虑用 ANTLR Go target 或 participle 库简化 |
| Java/Go 协议兼容性 | 中 | 中 | 定义清晰的协议文档，写兼容性测试 |
| 性能回归 | 中 | 低 | Go 在 I/O 密集场景通常更优，但需基准测试验证 |
| 并发模型差异导致的 bug | 高 | 中 | 充分使用 `go race detector`，设计简洁的并发模型 |
| 项目周期超出预期 | 中 | 中 | 严格按阶段交付，每阶段可独立验证 |

---

## 七、时间估算

| 阶段 | 内容 | 预计工期 | 人力 |
|---|---|---|---|
| 阶段 1 | R-tree 核心 | 2-3 周 | 1人 |
| 阶段 2 | 存储 + 缓存 + WAL | 3-4 周 | 1人 |
| 阶段 3 | 查询编译系统 | 3-4 周 | 1-2人 |
| 阶段 4 | 网络 + 客户端 | 2-3 周 | 1人 |
| 阶段 5 | 集成测试 + 优化 | 2-3 周 | 1人 |
| **总计** | | **12-17 周** | |

> 注：以上为一人全职开发的估算。如果两人并行，阶段2和阶段3可以同时进行（存储/缓存 与 查询编译 相对独立），总工期可压缩到 **8-12 周**。

---

## 八、迁移收益预期

### 8.1 性能收益
- **内存占用降低**：Go 无 JVM 开销，通常内存使用降低 50-70%
- **启动速度提升**：编译为原生二进制，启动时间从秒级降到毫秒级
- **并发性能提升**：goroutine 比 Java 线程更轻量（2KB vs 1MB 栈空间）
- **GC 压力更低**：Go GC 延迟通常 < 1ms

### 8.2 工程收益
- **部署简化**：单个静态二进制，无需 JRE 环境
- **交叉编译**：轻松构建 Linux/macOS/Windows 多平台版本
- **容器友好**：Docker 镜像从数百 MB 降到数十 MB
- **依赖管理**：Go Modules 比 Maven 更简洁

### 8.3 维护收益
- **代码更简洁**：Go 的设计哲学倾向简单直接
- **无 Lombok 等魔法**：所有代码可见、可追踪
- **标准格式化**：`gofmt` 强制统一代码风格
- **更强的并发安全工具**：race detector 内置

---

## 九、建议的迁移策略

### 推荐：**渐进式重写（Strangler Fig Pattern）**

不建议一次性全部重写，而是采用渐进式策略：

1. **阶段 1 独立验证**：先迁移 R-tree 模块，作为独立的 Go 库发布和验证
2. **阶段 2-3 核心引擎**：迁移存储和查询引擎，此时可以独立运行但不对外服务
3. **阶段 4 网络层**：实现网络层，此时可与 Java 客户端互操作（保持协议兼容）
4. **阶段 5 完全切换**：验证无误后切换到 Go 版本

每个阶段都应该有独立的测试套件和基准测试，确保功能正确性和性能达标。

---

## 十、下一步行动

如果确认采用此方案，建议按以下顺序开始：

1. **初始化 Go 项目骨架**：创建目录结构、go.mod、基础工具链配置
2. **迁移 Rectangle 接口和 MBRectangle**：最基础的数据结构
3. **迁移 RTree 核心**：包含插入、删除、搜索算法
4. **编写全面的测试用例**：从 Java 测试中提取测试数据

> 如果你准备好了，我可以立即开始阶段 1 的代码实现。
