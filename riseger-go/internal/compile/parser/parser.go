package parser

import (
	"fmt"

	"github.com/riseger/riseger-go/internal/compile/lexer"
)

// ============================================================================
// RisegerDB 查询语言解析器
// ============================================================================
//
// 本文件实现了一个递归下降解析器（Recursive Descent Parser），将 SQL-like 查询
// 文本转换为抽象语法树（AST）。每个 parse* 方法对应一条语法规则。
//
// ┌─────────────────────────────────────────────────────────────────────┐
// │                        完整语法规则参考                              │
// │                                                                     │
// │  下方列出了本解析器支持的全部语法。                                    │
// │  "→" 左边是规则名，右边是该规则的展开方式。                            │
// │  "|" 表示"或"，"?" 表示可选，"*" 表示零次或多次重复。                  │
// │                                                                     │
// │  如果你要修改语法，找到对应的规则名，然后定位到同名的                    │
// │  parse 方法（如 "sql → ..." 对应 ParseSQL 方法）进行修改。            │
// └─────────────────────────────────────────────────────────────────────┘
//
// ── 顶层语句 ──────────────────────────────────────────────────────────
//
//	sql             →  use_stmt            -- 以 USE 开头的语句
//	                 | search_stmt         -- 以 SEARCH 开头的查询
//	                 | get_stmt            -- 以 GET 开头的元数据查询
//	                 | preload_stmt        -- 以 PRELOAD 开头的数据加载
//
// ── USE 语句 ──────────────────────────────────────────────────────────
//
//	use_stmt        →  USE use_clause ( search_stmt | update_stmt | get_stmt )?
//	use_clause      →  use_item ( '|' use_item )*
//	use_item        →  DATABASE  string_value       -- 选择数据库
//	                 |  MAP      string_value       -- 选择地图
//	                 |  SCOPE    rect_expr          -- 设定查询范围（矩形）
//	                 |  MODEL    strings_expr       -- 选择数据模型
//
// ── SEARCH 查询 ───────────────────────────────────────────────────────
//
//	search_stmt     →  SEARCH  strings_expr  ( WHERE where_clause )?
//
// ── GET 元数据 ────────────────────────────────────────────────────────
//
//	get_stmt        →  GET DATABASES        -- 列出所有数据库
//	                 | GET MAPS             -- 列出当前数据库的地图
//	                 | GET MODELS           -- 列出当前数据库的模型
//
// ── UPDATE 语句 ───────────────────────────────────────────────────────
//
//	update_stmt     →  UPDATE update_clauses WHERE where_clause
//	update_clauses  →  update_clause ( ',' update_clause )*
//	update_clause   →  attr_expr '=' all_entity
//
// ── PRELOAD 语句 ──────────────────────────────────────────────────────
//
//	preload_stmt    →  PRELOAD string_value
//
// ── WHERE 条件表达式（按优先级从低到高）─────────────────────────────────
//
//	where_clause    →  bool_or
//	bool_or         →  bool_and  ( OR   bool_and )*        -- 逻辑或（最低优先级）
//	bool_and        →  bool_not  ( AND  bool_not )*        -- 逻辑与
//	bool_not        →  ( '!' | NOT )?  bool_cmp            -- 逻辑非
//	bool_cmp        →  IN  graphic_expr                    -- 空间包含
//	                 |  OUT graphic_expr                    -- 空间排除
//	                 |  '(' bool_or ')'                     -- 括号分组
//	                 |  num_expr  ( cmp_op num_expr )?      -- 比较运算
//	cmp_op          →  '>' | '>=' | '<' | '<=' | '='
//
// ── 算术表达式（按优先级从低到高）─────────────────────────────────────
//
//	num_expr        →  term ( ( '+' | '-' ) term )*        -- 加减法
//	term            →  factor ( ( '*' | '/' ) factor )*    -- 乘除法
//	factor          →  '-' factor                          -- 取负
//	                 |  '(' bool_or ')'                     -- 括号
//	                 |  atom                                -- 原子值
//	atom            →  数字 | 坐标 | 矩形 | 字符串 | 属性引用
//
// ── 空间表达式 ────────────────────────────────────────────────────────
//
//	graphic_expr    →  rect_expr | coord_expr → CoordToRect | attr_expr
//	rect_expr       →  RECT '(' coord_expr ',' num_expr ')'
//	coord_expr      →  '[' num_expr ',' num_expr ']'
//	coords_expr     →  coord_expr ( ',' coord_expr )*
//
// ── 字符串 / 属性 ────────────────────────────────────────────────────
//
//	strings_expr    →  dot_string ( ',' dot_string )*      -- 逗号分隔的名称列表
//	dot_string      →  ident ( '.' ident )*                -- 点分隔的名称路径
//	attr_expr       →  dot_string                          -- 属性引用
//	string_value    →  'xxx' | ident                       -- 引号字符串或标识符
//	all_entity      →  num_expr | rect_expr | coord_expr | attr_expr | string
//
// ============================================================================

// Parser 持有 token 列表和当前读取位置。
type Parser struct {
	tokens []lexer.Token
	pos    int
}

func New(tokens []lexer.Token) *Parser {
	return &Parser{tokens: tokens, pos: 0}
}

// Parse 是最常用的入口：输入查询字符串，输出 AST 根节点。
func Parse(input string) (*Node, error) {
	tokens, err := lexer.Tokenize(input)
	if err != nil {
		return nil, err
	}
	p := New(tokens)
	return p.ParseSQL()
}

// ============================================================================
// 规则: sql → use_stmt | search_stmt | get_stmt | preload_stmt
//
// 根据第一个关键字判断进入哪个分支。
// ============================================================================

func (p *Parser) ParseSQL() (*Node, error) {
	tok := p.peek()
	switch {
	case tok.IsKeyword("USE"):
		return p.parseUseStmt()
	case tok.IsKeyword("SEARCH"):
		return p.parseSearchStmt()
	case tok.IsKeyword("GET"):
		return p.parseGetStmt()
	case tok.IsKeyword("PRELOAD"):
		return p.parsePreload()
	case tok.IsKeyword("CREATE"):
		return p.parseCreateStmt()
	case tok.IsKeyword("DELETE"):
		return p.parseDeleteStmt()
	default:
		return nil, p.errorf("expected USE, SEARCH, GET, PRELOAD, CREATE, or DELETE, got %s", tok)
	}
}

// ============================================================================
// 规则: use_stmt → USE use_clause ( search_stmt | update_stmt | get_stmt )?
//
// USE 后面跟一组子句（DATABASE/MAP/SCOPE/MODEL），然后可以可选地
// 跟一条 SEARCH、UPDATE 或 GET 语句。
//
// 示例:
//
//	USE DATABASE 'test_db' | MAP 'china' SEARCH name, area WHERE area > 100
//	USE DATABASE 'test_db' GET MAPS
//	USE DATABASE 'test_db' | MAP 'china' UPDATE name = 'x' WHERE area > 10
//
// ============================================================================

func (p *Parser) parseUseStmt() (*Node, error) {
	p.expect("USE")

	// 解析 use_clause: 一个或多个由 '|' 分隔的 use_item
	clauses, err := p.parseUseClauses()
	if err != nil {
		return nil, err
	}
	root := newNode(NodeUse, clauses...)

	// 检查 USE 后面是否跟了 SEARCH / UPDATE / GET
	next := p.peek()
	switch {
	case next.IsKeyword("SEARCH"):
		child, err := p.parseSearchStmt()
		if err != nil {
			return nil, err
		}
		root.Children = append(root.Children, child)

	case next.IsKeyword("UPDATE"):
		child, err := p.parseUpdateStmt()
		if err != nil {
			return nil, err
		}
		root.Children = append(root.Children, child)

	case next.IsKeyword("GET"):
		child, err := p.parseGetStmt()
		if err != nil {
			return nil, err
		}
		root.Children = append(root.Children, child)

	case next.IsKeyword("CREATE"):
		child, err := p.parseCreateStmt()
		if err != nil {
			return nil, err
		}
		root.Children = append(root.Children, child)

	case next.IsKeyword("DELETE"):
		child, err := p.parseDeleteStmt()
		if err != nil {
			return nil, err
		}
		root.Children = append(root.Children, child)
	}

	return root, nil
}

// ============================================================================
// 规则: use_clause → use_item ( '|' use_item )*
//
// 多个 use_item 之间用 '|' 分隔。
// 示例: DATABASE 'test_db' | MAP 'china' | MODEL building
// ============================================================================

func (p *Parser) parseUseClauses() ([]*Node, error) {
	first, err := p.parseUseItem()
	if err != nil {
		return nil, err
	}
	clauses := []*Node{first}

	for p.peek().IsOperator("|") {
		p.advance() // 跳过 '|'
		item, err := p.parseUseItem()
		if err != nil {
			return nil, err
		}
		clauses = append(clauses, item)
	}
	return clauses, nil
}

// ============================================================================
// 规则: use_item → DATABASE string_value
//                 | MAP string_value
//                 | SCOPE rect_expr
//                 | MODEL strings_expr
//
// 每个 use_item 以一个关键字开头，后面跟对应的值。
// ============================================================================

func (p *Parser) parseUseItem() (*Node, error) {
	tok := p.peek()
	switch {
	case tok.IsKeyword("DATABASE"):
		p.advance()
		s, err := p.parseStringValue()
		if err != nil {
			return nil, err
		}
		return newNode(NodeUseDatabase, s), nil

	case tok.IsKeyword("MAP"):
		p.advance()
		s, err := p.parseStringValue()
		if err != nil {
			return nil, err
		}
		return newNode(NodeUseMap, s), nil

	case tok.IsKeyword("SCOPE"):
		p.advance()
		r, err := p.parseRectExpr()
		if err != nil {
			return nil, err
		}
		return newNode(NodeUseScope, r), nil

	case tok.IsKeyword("MODEL"):
		p.advance()
		s, err := p.parseStringsExpr()
		if err != nil {
			return nil, err
		}
		return newNode(NodeUseModel, s), nil

	default:
		return nil, p.errorf("expected DATABASE, MAP, SCOPE, or MODEL in USE clause, got %s", tok)
	}
}

// ============================================================================
// 规则: search_stmt → SEARCH strings_expr ( WHERE where_clause )?
//
// SEARCH 后跟要查询的列名列表，可选地带 WHERE 过滤条件。
//
// 示例:
//
//	SEARCH name, area
//	SEARCH name, area WHERE area > 100
//
// ============================================================================

func (p *Parser) parseSearchStmt() (*Node, error) {
	p.expect("SEARCH")

	// 解析要查询的列: strings_expr（逗号分隔的标识符列表）
	attrs, err := p.parseStringsExpr()
	if err != nil {
		return nil, err
	}
	node := newNode(NodeSearch, attrs)

	// 可选的 WHERE 子句
	if p.peek().IsKeyword("WHERE") {
		p.advance() // 跳过 WHERE
		where, err := p.parseWhereClause()
		if err != nil {
			return nil, err
		}
		node.Children = append(node.Children, where)
	}
	return node, nil
}

// ============================================================================
// 规则: get_stmt → GET DATABASES | GET MAPS | GET MODELS
//
// 用于查询数据库的元数据信息。
// ============================================================================

func (p *Parser) parseGetStmt() (*Node, error) {
	p.expect("GET")
	tok := p.peek()
	switch {
	case tok.IsKeyword("DATABASES"):
		p.advance()
		return newNode(NodeGetDatabases), nil
	case tok.IsKeyword("MAPS"):
		p.advance()
		return newNode(NodeGetMaps), nil
	case tok.IsKeyword("MODELS"):
		p.advance()
		return newNode(NodeGetModels), nil
	default:
		return nil, p.errorf("expected DATABASES, MAPS, or MODELS after GET, got %s", tok)
	}
}

// ============================================================================
// 规则: update_stmt → UPDATE update_clauses WHERE where_clause
//
// 示例: UPDATE name = 'new_name', area = 200 WHERE area > 10
// ============================================================================

func (p *Parser) parseUpdateStmt() (*Node, error) {
	p.expect("UPDATE")

	clauses, err := p.parseUpdateClauses()
	if err != nil {
		return nil, err
	}
	if err := p.expectKeyword("WHERE"); err != nil {
		return nil, err
	}
	where, err := p.parseWhereClause()
	if err != nil {
		return nil, err
	}

	node := newNode(NodeUpdate)
	node.Children = append(node.Children, clauses...)
	node.Children = append(node.Children, where)
	return node, nil
}

// ============================================================================
// 规则: update_clauses → update_clause ( ',' update_clause )*
//       update_clause  → attr_expr '=' all_entity
//
// 逗号分隔的赋值列表: name = 'xxx', area = 100
// ============================================================================

func (p *Parser) parseUpdateClauses() ([]*Node, error) {
	first, err := p.parseUpdateClause()
	if err != nil {
		return nil, err
	}
	clauses := []*Node{first}

	for p.peek().IsOperator(",") {
		p.advance() // 跳过 ','
		c, err := p.parseUpdateClause()
		if err != nil {
			return nil, err
		}
		clauses = append(clauses, c)
	}
	return clauses, nil
}

func (p *Parser) parseUpdateClause() (*Node, error) {
	attr, err := p.parseAttrExpr()
	if err != nil {
		return nil, err
	}
	if err := p.expectOp("="); err != nil {
		return nil, err
	}
	val, err := p.parseAllEntity()
	if err != nil {
		return nil, err
	}
	return newNode(NodeUpdateClause, attr, val), nil
}

// ============================================================================
// 规则: preload_stmt → PRELOAD string_value
//
// 加载外部数据到数据库。
// 示例: PRELOAD 'test_data.json'
// ============================================================================

func (p *Parser) parsePreload() (*Node, error) {
	p.expect("PRELOAD")
	s, err := p.parseStringValue()
	if err != nil {
		return nil, err
	}
	return newNode(NodePreload, s), nil
}

// ============================================================================
// 规则: where_clause → bool_or
//
// WHERE 后面的所有内容都是一个布尔表达式。
// 优先级从低到高: OR → AND → NOT → 比较/空间 → 算术
// ============================================================================

func (p *Parser) parseWhereClause() (*Node, error) {
	cond, err := p.parseBoolOr()
	if err != nil {
		return nil, err
	}
	return newNode(NodeWhere, cond), nil
}

// ============================================================================
// 规则: bool_or → bool_and ( OR bool_and )*
//
// 逻辑或运算，优先级最低。
// 示例: area > 100 OR name = 'test'
// ============================================================================

func (p *Parser) parseBoolOr() (*Node, error) {
	left, err := p.parseBoolAnd()
	if err != nil {
		return nil, err
	}
	for p.peek().IsKeyword("OR") {
		p.advance() // 跳过 OR
		right, err := p.parseBoolAnd()
		if err != nil {
			return nil, err
		}
		left = newNode(NodeOr, left, right)
	}
	return left, nil
}

// ============================================================================
// 规则: bool_and → bool_not ( AND bool_not )*
//
// 逻辑与运算。
// 示例: area > 100 AND area < 200
// ============================================================================

func (p *Parser) parseBoolAnd() (*Node, error) {
	left, err := p.parseBoolNot()
	if err != nil {
		return nil, err
	}
	for p.peek().IsKeyword("AND") {
		p.advance() // 跳过 AND
		right, err := p.parseBoolNot()
		if err != nil {
			return nil, err
		}
		left = newNode(NodeAnd, left, right)
	}
	return left, nil
}

// ============================================================================
// 规则: bool_not → ( '!' | NOT )? bool_cmp
//
// 逻辑非运算（可选前缀）。
// 示例: ! area > 100   或   NOT area > 100
// ============================================================================

func (p *Parser) parseBoolNot() (*Node, error) {
	if p.peek().IsOperator("!") || p.peek().IsKeyword("NOT") {
		p.advance() // 跳过 '!' 或 NOT
		child, err := p.parseBoolCmp()
		if err != nil {
			return nil, err
		}
		return newNode(NodeNot, child), nil
	}
	return p.parseBoolCmp()
}

// ============================================================================
// 规则: bool_cmp → IN graphic_expr              -- 空间包含判断
//                | OUT graphic_expr             -- 空间排除判断
//                | '(' bool_or ')'              -- 括号分组
//                | num_expr ( cmp_op num_expr )?  -- 数值比较
//
// cmp_op → '>' | '>=' | '<' | '<=' | '='
//
// 这是比较运算和空间运算的入口。
// 示例:
//
//	area > 100
//	IN RECT([10, 20], 50)
//	(area > 100 AND area < 200)
//
// ============================================================================

func (p *Parser) parseBoolCmp() (*Node, error) {
	tok := p.peek()

	// 空间包含: IN graphic_expr
	if tok.IsKeyword("IN") {
		p.advance()
		g, err := p.parseGraphicExpr()
		if err != nil {
			return nil, err
		}
		return newNode(NodeIn, g), nil
	}

	// 空间排除: OUT graphic_expr
	if tok.IsKeyword("OUT") {
		p.advance()
		g, err := p.parseGraphicExpr()
		if err != nil {
			return nil, err
		}
		return newNode(NodeOut, g), nil
	}

	// 括号分组: '(' bool_or ')'
	if tok.IsOperator("(") {
		p.advance()
		inner, err := p.parseBoolOr()
		if err != nil {
			return nil, err
		}
		if err := p.expectOp(")"); err != nil {
			return nil, err
		}
		return inner, nil
	}

	// 数值比较: num_expr ( cmp_op num_expr )?
	left, err := p.parseNumExpr()
	if err != nil {
		return nil, err
	}
	return p.tryParseComparison(left)
}

// tryParseComparison 尝试在 num_expr 后读取一个比较运算符。
// 如果没有比较符，就直接返回 left。
func (p *Parser) tryParseComparison(left *Node) (*Node, error) {
	op := p.peek()

	type cmpDef struct {
		match func(lexer.Token) bool
		node  NodeType
	}
	comparisons := []cmpDef{
		{func(t lexer.Token) bool { return t.Value == ">=" }, NodeGreaterEq},
		{func(t lexer.Token) bool { return t.Value == "<=" }, NodeLessEq},
		{func(t lexer.Token) bool { return t.IsOperator(">") }, NodeGreater},
		{func(t lexer.Token) bool { return t.IsOperator("<") }, NodeLess},
		{func(t lexer.Token) bool { return t.IsOperator("=") }, NodeEqual},
	}

	for _, c := range comparisons {
		if c.match(op) {
			p.advance()
			// 处理 ">" 后面紧跟 "=" 被 lexer 拆成两个 token 的情况
			if op.IsOperator(">") && p.peek().IsOperator("=") {
				p.advance()
				right, err := p.parseNumExpr()
				if err != nil {
					return nil, err
				}
				return newNode(NodeGreaterEq, left, right), nil
			}
			if op.IsOperator("<") && p.peek().IsOperator("=") {
				p.advance()
				right, err := p.parseNumExpr()
				if err != nil {
					return nil, err
				}
				return newNode(NodeLessEq, left, right), nil
			}
			right, err := p.parseNumExpr()
			if err != nil {
				return nil, err
			}
			return newNode(c.node, left, right), nil
		}
	}

	// 没有比较运算符，直接返回左侧表达式
	return left, nil
}

// ============================================================================
// 规则: num_expr → term ( ( '+' | '-' ) term )*
//
// 加减法运算。
// 示例: area + 10, a - b + c
// ============================================================================

func (p *Parser) parseNumExpr() (*Node, error) {
	left, err := p.parseTerm()
	if err != nil {
		return nil, err
	}
	for p.peek().IsOperator("+") || p.peek().IsOperator("-") {
		op := p.peek().Value
		p.advance()
		right, err := p.parseTerm()
		if err != nil {
			return nil, err
		}
		if op == "+" {
			left = newNode(NodeAdd, left, right)
		} else {
			left = newNode(NodeSub, left, right)
		}
	}
	return left, nil
}

// ============================================================================
// 规则: term → factor ( ( '*' | '/' ) factor )*
//
// 乘除法运算（优先级高于加减法）。
// 示例: area * 2, a / b
// ============================================================================

func (p *Parser) parseTerm() (*Node, error) {
	left, err := p.parseFactor()
	if err != nil {
		return nil, err
	}
	for p.peek().IsOperator("*") || p.peek().IsOperator("/") {
		op := p.peek().Value
		p.advance()
		right, err := p.parseFactor()
		if err != nil {
			return nil, err
		}
		if op == "*" {
			left = newNode(NodeMul, left, right)
		} else {
			left = newNode(NodeDiv, left, right)
		}
	}
	return left, nil
}

// ============================================================================
// 规则: factor → '-' factor       -- 取负: -100, -(a + b)
//              | '(' bool_or ')'  -- 括号分组
//              | atom             -- 原子值
// ============================================================================

func (p *Parser) parseFactor() (*Node, error) {
	// 取负运算
	if p.peek().IsOperator("-") {
		p.advance()
		child, err := p.parseFactor()
		if err != nil {
			return nil, err
		}
		return newNode(NodeNegate, child), nil
	}

	// 括号分组
	if p.peek().IsOperator("(") {
		p.advance()
		inner, err := p.parseBoolOr()
		if err != nil {
			return nil, err
		}
		if err := p.expectOp(")"); err != nil {
			return nil, err
		}
		return inner, nil
	}

	// 原子值
	return p.parseAtom()
}

// ============================================================================
// 规则: atom → 数字                      -- 42, 3.14
//            | '[' num ',' num ']'       -- 坐标: [10, 20]
//            | RECT '(' ... ')'          -- 矩形: RECT([10,20], 50)
//            | 'xxx'                     -- 引号字符串
//            | ident                     -- 属性引用: area, map.layer.name
// ============================================================================

func (p *Parser) parseAtom() (*Node, error) {
	tok := p.peek()

	switch {
	case tok.Type == lexer.TokenNumber:
		p.advance()
		return leafNode(NodeNumber, tok.Value), nil

	case tok.IsOperator("["):
		return p.parseCoordExpr()

	case tok.IsKeyword("RECT"):
		return p.parseRectExpr()

	case tok.Type == lexer.TokenString:
		p.advance()
		return leafNode(NodeString, tok.Value), nil

	case tok.Type == lexer.TokenIdent:
		return p.parseAttrExpr()

	default:
		return nil, p.errorf("unexpected token %s in expression", tok)
	}
}

// ============================================================================
// 规则: graphic_expr → rect_expr                     -- 矩形
//                    | coord_expr → CoordToRect      -- 坐标自动转换为点矩形
//                    | attr_expr                     -- 属性引用
//
// 用于 IN / OUT 等空间运算的参数。
// ============================================================================

func (p *Parser) parseGraphicExpr() (*Node, error) {
	tok := p.peek()
	switch {
	case tok.IsKeyword("RECT"):
		return p.parseRectExpr()

	case tok.IsOperator("["):
		coord, err := p.parseCoordExpr()
		if err != nil {
			return nil, err
		}
		return newNode(NodeCoordToRect, coord), nil

	case tok.Type == lexer.TokenIdent:
		return p.parseAttrExpr()

	default:
		return nil, p.errorf("expected RECT, coordinate, or attribute in graphic expression, got %s", tok)
	}
}

// ============================================================================
// 规则: coord_expr → '[' num_expr ',' num_expr ']'
//
// 坐标表达式。
// 示例: [10, 20], [3.14, 2.71]
// ============================================================================

func (p *Parser) parseCoordExpr() (*Node, error) {
	if err := p.expectOp("["); err != nil {
		return nil, err
	}
	x, err := p.parseNumExpr()
	if err != nil {
		return nil, err
	}
	if err := p.expectOp(","); err != nil {
		return nil, err
	}
	y, err := p.parseNumExpr()
	if err != nil {
		return nil, err
	}
	if err := p.expectOp("]"); err != nil {
		return nil, err
	}
	return newNode(NodeCoord, x, y), nil
}

// ============================================================================
// 规则: rect_expr → RECT '(' coord_expr ',' num_expr ')'
//
// 矩形表达式：以一个中心坐标和半径大小构造矩形。
// 示例: RECT([100, 200], 50)
// ============================================================================

func (p *Parser) parseRectExpr() (*Node, error) {
	if err := p.expectKeyword("RECT"); err != nil {
		return nil, err
	}
	if err := p.expectOp("("); err != nil {
		return nil, err
	}
	coord, err := p.parseCoordExpr()
	if err != nil {
		return nil, err
	}
	if err := p.expectOp(","); err != nil {
		return nil, err
	}
	size, err := p.parseNumExpr()
	if err != nil {
		return nil, err
	}
	if err := p.expectOp(")"); err != nil {
		return nil, err
	}
	return newNode(NodeRect, coord, size), nil
}

// ============================================================================
// 规则: coords_expr → coord_expr ( ',' coord_expr )*
//
// 多个坐标组成的列表（用于线/多边形等）。
// 示例: [10,20], [30,40], [50,60]
// ============================================================================

func (p *Parser) parseCoordsExpr() (*Node, error) {
	first, err := p.parseCoordExpr()
	if err != nil {
		return nil, err
	}
	coords := []*Node{first}

	// 继续读取逗号后面的坐标（前瞻判断下一个 ',' 后面是否是 '['）
	for p.peek().IsOperator(",") && p.peekAt(1).IsOperator("[") {
		p.advance() // 跳过 ','
		c, err := p.parseCoordExpr()
		if err != nil {
			return nil, err
		}
		coords = append(coords, c)
	}

	if len(coords) == 1 {
		return first, nil
	}
	return newNode(NodeCoords, coords...), nil
}

// ============================================================================
// 规则: strings_expr → dot_string ( ',' dot_string )*
//
// 逗号分隔的标识符列表，用于 SEARCH 的列名和 MODEL 选择。
// 示例: name, area, building.floorArea
// ============================================================================

func (p *Parser) parseStringsExpr() (*Node, error) {
	first, err := p.parseDotString()
	if err != nil {
		return nil, err
	}
	parts := []*Node{first}

	// 逗号后面必须跟标识符才继续（避免和 UPDATE 中的逗号冲突）
	for p.peek().IsOperator(",") && p.peekAt(1).Type == lexer.TokenIdent {
		p.advance() // 跳过 ','
		ds, err := p.parseDotString()
		if err != nil {
			return nil, err
		}
		parts = append(parts, ds)
	}
	return newNode(NodeStrings, parts...), nil
}

// ============================================================================
// 规则: dot_string → ident ( '.' ident )*
//
// 点分隔的名称路径，用于属性引用。
// 示例: name, map.layer.area
// ============================================================================

func (p *Parser) parseDotString() (*Node, error) {
	tok := p.peek()
	if tok.Type != lexer.TokenIdent {
		return nil, p.errorf("expected identifier, got %s", tok)
	}
	p.advance()
	parts := []string{tok.Value}

	for p.peek().IsOperator(".") {
		p.advance() // 跳过 '.'
		next := p.peek()
		if next.Type != lexer.TokenIdent {
			return nil, p.errorf("expected identifier after '.', got %s", next)
		}
		p.advance()
		parts = append(parts, next.Value)
	}

	// 单个标识符: 直接作为叶节点
	if len(parts) == 1 {
		return leafNode(NodeDotString, parts[0]), nil
	}

	// 多段路径: 每段作为子节点
	node := &Node{Type: NodeDotString}
	for _, part := range parts {
		node.Children = append(node.Children, leafNode(NodeString, part))
	}
	return node, nil
}

// ============================================================================
// 规则: attr_expr → dot_string
//
// 属性引用，就是把 dot_string 包装一层 NodeAttribute 节点。
// ============================================================================

func (p *Parser) parseAttrExpr() (*Node, error) {
	ds, err := p.parseDotString()
	if err != nil {
		return nil, err
	}
	return newNode(NodeAttribute, ds), nil
}

// ============================================================================
// 规则: string_value → 'xxx' | ident
//
// 读取一个字符串值：可以是引号包裹的字符串，也可以是裸标识符。
// 示例: 'test_db' 或 test_db
// ============================================================================

func (p *Parser) parseStringValue() (*Node, error) {
	tok := p.peek()
	switch {
	case tok.Type == lexer.TokenString:
		p.advance()
		return leafNode(NodeString, tok.Value), nil
	case tok.Type == lexer.TokenIdent:
		p.advance()
		return leafNode(NodeString, tok.Value), nil
	default:
		return nil, p.errorf("expected string or identifier, got %s", tok)
	}
}

// ============================================================================
// 规则: all_entity → num_expr | rect_expr | coord_expr | attr_expr | string
//
// UPDATE 赋值右侧可以是任何类型的值。
// ============================================================================

func (p *Parser) parseAllEntity() (*Node, error) {
	tok := p.peek()
	switch {
	case tok.IsOperator("["):
		return p.parseCoordExpr()
	case tok.IsKeyword("RECT"):
		return p.parseRectExpr()
	case tok.Type == lexer.TokenNumber:
		return p.parseNumExpr()
	case tok.Type == lexer.TokenString:
		p.advance()
		return leafNode(NodeString, tok.Value), nil
	case tok.Type == lexer.TokenIdent:
		return p.parseAttrExpr()
	default:
		return nil, p.errorf("expected value expression, got %s", tok)
	}
}

// ============================================================================
// 规则: create_stmt → CREATE DATABASE string_value
//                   | CREATE MAP string_value NODESIZE number THRESHOLD number
//                   | CREATE MODEL string_value PARENT string_value ( PARAM ident string_value )*
//
// DDL 语句，用于创建数据库、地图和数据模型。
//
// 示例:
//
//	CREATE DATABASE 'my_db'
//	CREATE MAP 'world' NODESIZE 8 THRESHOLD 0.5
//	CREATE MODEL 'building' PARENT 'field' PARAM name STRING PARAM area DOUBLE
//
// ============================================================================

func (p *Parser) parseCreateStmt() (*Node, error) {
	p.expect("CREATE")
	tok := p.peek()
	switch {
	case tok.IsKeyword("DATABASE"):
		p.advance()
		name, err := p.parseStringValue()
		if err != nil {
			return nil, err
		}
		return newNode(NodeCreateDatabase, name), nil

	case tok.IsKeyword("MAP"):
		p.advance()
		name, err := p.parseStringValue()
		if err != nil {
			return nil, err
		}
		node := newNode(NodeCreateMap, name)
		if p.peek().IsKeyword("NODESIZE") {
			p.advance()
			ns, err := p.parseAtom()
			if err != nil {
				return nil, err
			}
			node.Children = append(node.Children, ns)
		}
		if p.peek().IsKeyword("THRESHOLD") {
			p.advance()
			th, err := p.parseAtom()
			if err != nil {
				return nil, err
			}
			node.Children = append(node.Children, th)
		}
		return node, nil

	case tok.IsKeyword("MODEL"):
		p.advance()
		name, err := p.parseStringValue()
		if err != nil {
			return nil, err
		}
		node := newNode(NodeCreateModel, name)
		if p.peek().IsKeyword("PARENT") {
			p.advance()
			parent, err := p.parseStringValue()
			if err != nil {
				return nil, err
			}
			node.Children = append(node.Children, parent)
		}
		for p.peek().IsKeyword("PARAM") {
			p.advance()
			paramName, err := p.parseStringValue()
			if err != nil {
				return nil, err
			}
			paramType, err := p.parseStringValue()
			if err != nil {
				return nil, err
			}
			node.Children = append(node.Children, newNode(NodeCreateModelParam, paramName, paramType))
		}
		return node, nil

	default:
		return nil, p.errorf("expected DATABASE, MAP, or MODEL after CREATE, got %s", tok)
	}
}

// ============================================================================
// 规则: delete_stmt → DELETE FROM string_value WHERE where_clause
//
// 删除符合条件的元素。需要在 USE DATABASE | MAP 上下文中使用。
//
// 示例:
//
//	USE DATABASE 'test_db' | MAP 'china' DELETE FROM 'building_model' WHERE area < 100
//	DELETE FROM building_model WHERE name = 'old'
//
// ============================================================================

func (p *Parser) parseDeleteStmt() (*Node, error) {
	p.expect("DELETE")
	if err := p.expectKeyword("FROM"); err != nil {
		return nil, err
	}
	modelName, err := p.parseStringValue()
	if err != nil {
		return nil, err
	}
	if err := p.expectKeyword("WHERE"); err != nil {
		return nil, err
	}
	where, err := p.parseWhereClause()
	if err != nil {
		return nil, err
	}
	return newNode(NodeDelete, modelName, where), nil
}

// ============================================================================
// Token 读取工具方法
// ============================================================================

// peek 查看当前 token 但不消耗它。
func (p *Parser) peek() lexer.Token {
	if p.pos >= len(p.tokens) {
		return lexer.Token{Type: lexer.TokenEOF}
	}
	return p.tokens[p.pos]
}

// peekAt 查看从当前位置偏移 offset 处的 token。
func (p *Parser) peekAt(offset int) lexer.Token {
	idx := p.pos + offset
	if idx >= len(p.tokens) {
		return lexer.Token{Type: lexer.TokenEOF}
	}
	return p.tokens[idx]
}

// advance 消耗当前 token 并前进到下一个。
func (p *Parser) advance() lexer.Token {
	tok := p.peek()
	if p.pos < len(p.tokens) {
		p.pos++
	}
	return tok
}

// expect 消耗一个关键字 token（内部简化版，不做检查）。
func (p *Parser) expect(keyword string) {
	p.pos++
}

// expectKeyword 断言当前 token 是指定的关键字，否则报错。
func (p *Parser) expectKeyword(kw string) error {
	tok := p.peek()
	if !tok.IsKeyword(kw) {
		return p.errorf("expected keyword %q, got %s", kw, tok)
	}
	p.advance()
	return nil
}

// expectOp 断言当前 token 是指定的运算符，否则报错。
func (p *Parser) expectOp(op string) error {
	tok := p.peek()
	if !tok.IsOperator(op) {
		return p.errorf("expected %q, got %s", op, tok)
	}
	p.advance()
	return nil
}

// errorf 生成带有行号列号信息的错误。
func (p *Parser) errorf(format string, args ...interface{}) error {
	tok := p.peek()
	prefix := fmt.Sprintf("parse error at line %d col %d: ", tok.Line, tok.Col)
	return fmt.Errorf(prefix+format, args...)
}
