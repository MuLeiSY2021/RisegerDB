package parser

import (
	"fmt"
	"github.com/riseger/riseger-go/internal/compile/lexer"
)

// Parser is a recursive-descent parser for the RisegerDB SQL-like query language.
// Grammar (simplified, matching the Java syntaxrule.rule):
//
//	sql           → use_stmt | search_stmt | preload_stmt
//	use_stmt      → USE use_clause (search_stmt | update_stmt)?
//	use_clause    → use_item ('|' use_item)*
//	use_item      → DATABASE string | MAP string | SCOPE rect_expr | MODEL strings_expr
//	search_stmt   → SEARCH strings_expr (WHERE where_clause)?
//	              | GET DATABASES | GET MAPS | GET MODELS
//	update_stmt   → UPDATE update_clause WHERE where_clause
//	update_clause → attr_expr '=' all_entity (',' attr_expr '=' all_entity)*
//	preload_stmt  → PRELOAD string
//	where_clause  → bool_or
//	bool_or       → bool_and (OR bool_and)*
//	bool_and      → bool_not (AND bool_not)*
//	bool_not      → NOT? bool_cmp
//	bool_cmp      → spatial_expr | num_expr (cmp_op num_expr)?
//	spatial_expr  → IN graphic_expr | OUT graphic_expr
//	num_expr      → term (('+' | '-') term)*
//	term          → factor (('*' | '/') factor)*
//	factor        → '-' factor | '(' bool_or ')' | atom
//	atom          → number | coord_expr | rect_expr | attr_expr | string
//	coord_expr    → '[' num_expr ',' num_expr ']'
//	rect_expr     → RECT '(' coord_expr ',' num_expr ')'
//	graphic_expr  → rect_expr | coord_expr | attr_expr
//	strings_expr  → dot_string (',' dot_string)*
//	dot_string    → ident ('.' ident)*
//	attr_expr     → dot_string
//	string        → STRING_LITERAL | ident
type Parser struct {
	tokens []lexer.Token
	pos    int
}

func New(tokens []lexer.Token) *Parser {
	return &Parser{tokens: tokens, pos: 0}
}

// Parse parses the full input and returns the root AST node.
func Parse(input string) (*Node, error) {
	tokens, err := lexer.Tokenize(input)
	if err != nil {
		return nil, err
	}
	p := New(tokens)
	return p.ParseSQL()
}

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
	default:
		return nil, p.errorf("expected USE, SEARCH, GET, or PRELOAD, got %s", tok)
	}
}

// ---------- USE ----------

func (p *Parser) parseUseStmt() (*Node, error) {
	p.expect("USE")
	clauses, err := p.parseUseClauses()
	if err != nil {
		return nil, err
	}

	root := newNode(NodeUse, clauses...)

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
	}

	return root, nil
}

func (p *Parser) parseUseClauses() ([]*Node, error) {
	var clauses []*Node
	c, err := p.parseUseItem()
	if err != nil {
		return nil, err
	}
	clauses = append(clauses, c)

	for p.peek().IsOperator("|") {
		p.advance()
		c, err := p.parseUseItem()
		if err != nil {
			return nil, err
		}
		clauses = append(clauses, c)
	}
	return clauses, nil
}

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

// ---------- SEARCH ----------

func (p *Parser) parseSearchStmt() (*Node, error) {
	p.expect("SEARCH")
	attrs, err := p.parseStringsExpr()
	if err != nil {
		return nil, err
	}
	node := newNode(NodeSearch, attrs)

	if p.peek().IsKeyword("WHERE") {
		p.advance()
		where, err := p.parseWhereClause()
		if err != nil {
			return nil, err
		}
		node.Children = append(node.Children, where)
	}
	return node, nil
}

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

// ---------- UPDATE ----------

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

func (p *Parser) parseUpdateClauses() ([]*Node, error) {
	var clauses []*Node
	c, err := p.parseUpdateClause()
	if err != nil {
		return nil, err
	}
	clauses = append(clauses, c)

	for p.peek().IsOperator(",") {
		p.advance()
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

// ---------- PRELOAD ----------

func (p *Parser) parsePreload() (*Node, error) {
	p.expect("PRELOAD")
	s, err := p.parseStringValue()
	if err != nil {
		return nil, err
	}
	return newNode(NodePreload, s), nil
}

// ---------- WHERE ----------

func (p *Parser) parseWhereClause() (*Node, error) {
	cond, err := p.parseBoolOr()
	if err != nil {
		return nil, err
	}
	return newNode(NodeWhere, cond), nil
}

func (p *Parser) parseBoolOr() (*Node, error) {
	left, err := p.parseBoolAnd()
	if err != nil {
		return nil, err
	}
	for p.peek().IsKeyword("OR") {
		p.advance()
		right, err := p.parseBoolAnd()
		if err != nil {
			return nil, err
		}
		left = newNode(NodeOr, left, right)
	}
	return left, nil
}

func (p *Parser) parseBoolAnd() (*Node, error) {
	left, err := p.parseBoolNot()
	if err != nil {
		return nil, err
	}
	for p.peek().IsKeyword("AND") {
		p.advance()
		right, err := p.parseBoolNot()
		if err != nil {
			return nil, err
		}
		left = newNode(NodeAnd, left, right)
	}
	return left, nil
}

func (p *Parser) parseBoolNot() (*Node, error) {
	if p.peek().IsOperator("!") || p.peek().IsKeyword("NOT") {
		p.advance()
		child, err := p.parseBoolCmp()
		if err != nil {
			return nil, err
		}
		return newNode(NodeNot, child), nil
	}
	return p.parseBoolCmp()
}

func (p *Parser) parseBoolCmp() (*Node, error) {
	tok := p.peek()
	if tok.IsKeyword("IN") {
		p.advance()
		g, err := p.parseGraphicExpr()
		if err != nil {
			return nil, err
		}
		return newNode(NodeIn, g), nil
	}
	if tok.IsKeyword("OUT") {
		p.advance()
		g, err := p.parseGraphicExpr()
		if err != nil {
			return nil, err
		}
		return newNode(NodeOut, g), nil
	}

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

	left, err := p.parseNumExpr()
	if err != nil {
		return nil, err
	}

	op := p.peek()
	switch {
	case op.IsOperator(">"):
		p.advance()
		if p.peek().IsOperator("=") {
			p.advance()
			right, err := p.parseNumExpr()
			if err != nil {
				return nil, err
			}
			return newNode(NodeGreaterEq, left, right), nil
		}
		right, err := p.parseNumExpr()
		if err != nil {
			return nil, err
		}
		return newNode(NodeGreater, left, right), nil
	case op.Value == ">=":
		p.advance()
		right, err := p.parseNumExpr()
		if err != nil {
			return nil, err
		}
		return newNode(NodeGreaterEq, left, right), nil
	case op.IsOperator("<"):
		p.advance()
		if p.peek().IsOperator("=") {
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
		return newNode(NodeLess, left, right), nil
	case op.Value == "<=":
		p.advance()
		right, err := p.parseNumExpr()
		if err != nil {
			return nil, err
		}
		return newNode(NodeLessEq, left, right), nil
	case op.IsOperator("="):
		p.advance()
		right, err := p.parseNumExpr()
		if err != nil {
			return nil, err
		}
		return newNode(NodeEqual, left, right), nil
	}

	return left, nil
}

// ---------- Arithmetic ----------

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

func (p *Parser) parseFactor() (*Node, error) {
	if p.peek().IsOperator("-") {
		p.advance()
		child, err := p.parseFactor()
		if err != nil {
			return nil, err
		}
		return newNode(NodeNegate, child), nil
	}
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
	return p.parseAtom()
}

func (p *Parser) parseAtom() (*Node, error) {
	tok := p.peek()

	if tok.Type == lexer.TokenNumber {
		p.advance()
		return leafNode(NodeNumber, tok.Value), nil
	}

	if tok.IsOperator("[") {
		return p.parseCoordExpr()
	}

	if tok.IsKeyword("RECT") {
		return p.parseRectExpr()
	}

	if tok.Type == lexer.TokenString {
		p.advance()
		return leafNode(NodeString, tok.Value), nil
	}

	if tok.Type == lexer.TokenIdent {
		return p.parseAttrExpr()
	}

	return nil, p.errorf("unexpected token %s in expression", tok)
}

// ---------- Spatial / Entity ----------

func (p *Parser) parseGraphicExpr() (*Node, error) {
	tok := p.peek()
	if tok.IsKeyword("RECT") {
		return p.parseRectExpr()
	}
	if tok.IsOperator("[") {
		coord, err := p.parseCoordExpr()
		if err != nil {
			return nil, err
		}
		return newNode(NodeCoordToRect, coord), nil
	}
	if tok.Type == lexer.TokenIdent {
		return p.parseAttrExpr()
	}
	return nil, p.errorf("expected RECT, coordinate, or attribute in graphic expression, got %s", tok)
}

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

func (p *Parser) parseCoordsExpr() (*Node, error) {
	first, err := p.parseCoordExpr()
	if err != nil {
		return nil, err
	}
	coords := []*Node{first}
	for p.peek().IsOperator(",") && p.peekAt(1).IsOperator("[") {
		p.advance() // skip ','
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

// ---------- Strings / Attributes ----------

func (p *Parser) parseStringsExpr() (*Node, error) {
	first, err := p.parseDotString()
	if err != nil {
		return nil, err
	}
	parts := []*Node{first}
	for p.peek().IsOperator(",") {
		next := p.peekAt(1)
		if next.Type != lexer.TokenIdent {
			break
		}
		p.advance() // skip ','
		ds, err := p.parseDotString()
		if err != nil {
			return nil, err
		}
		parts = append(parts, ds)
	}
	return newNode(NodeStrings, parts...), nil
}

func (p *Parser) parseDotString() (*Node, error) {
	tok := p.peek()
	if tok.Type != lexer.TokenIdent {
		return nil, p.errorf("expected identifier, got %s", tok)
	}
	p.advance()
	parts := []string{tok.Value}
	for p.peek().IsOperator(".") {
		p.advance()
		next := p.peek()
		if next.Type != lexer.TokenIdent {
			return nil, p.errorf("expected identifier after '.', got %s", next)
		}
		p.advance()
		parts = append(parts, next.Value)
	}
	if len(parts) == 1 {
		return leafNode(NodeDotString, parts[0]), nil
	}
	node := &Node{Type: NodeDotString}
	for _, part := range parts {
		node.Children = append(node.Children, leafNode(NodeString, part))
	}
	return node, nil
}

func (p *Parser) parseAttrExpr() (*Node, error) {
	ds, err := p.parseDotString()
	if err != nil {
		return nil, err
	}
	return newNode(NodeAttribute, ds), nil
}

func (p *Parser) parseStringValue() (*Node, error) {
	tok := p.peek()
	if tok.Type == lexer.TokenString {
		p.advance()
		return leafNode(NodeString, tok.Value), nil
	}
	if tok.Type == lexer.TokenIdent {
		p.advance()
		return leafNode(NodeString, tok.Value), nil
	}
	return nil, p.errorf("expected string or identifier, got %s", tok)
}

func (p *Parser) parseAllEntity() (*Node, error) {
	tok := p.peek()
	if tok.IsOperator("[") {
		return p.parseCoordExpr()
	}
	if tok.IsKeyword("RECT") {
		return p.parseRectExpr()
	}
	if tok.Type == lexer.TokenNumber {
		return p.parseNumExpr()
	}
	if tok.Type == lexer.TokenString {
		p.advance()
		return leafNode(NodeString, tok.Value), nil
	}
	if tok.Type == lexer.TokenIdent {
		return p.parseAttrExpr()
	}
	return nil, p.errorf("expected value expression, got %s", tok)
}

// ---------- helpers ----------

func (p *Parser) peek() lexer.Token {
	if p.pos >= len(p.tokens) {
		return lexer.Token{Type: lexer.TokenEOF}
	}
	return p.tokens[p.pos]
}

func (p *Parser) peekAt(offset int) lexer.Token {
	idx := p.pos + offset
	if idx >= len(p.tokens) {
		return lexer.Token{Type: lexer.TokenEOF}
	}
	return p.tokens[idx]
}

func (p *Parser) advance() lexer.Token {
	tok := p.peek()
	if p.pos < len(p.tokens) {
		p.pos++
	}
	return tok
}

func (p *Parser) expect(keyword string) {
	p.pos++
}

func (p *Parser) expectKeyword(kw string) error {
	tok := p.peek()
	if !tok.IsKeyword(kw) {
		return p.errorf("expected keyword %q, got %s", kw, tok)
	}
	p.advance()
	return nil
}

func (p *Parser) expectOp(op string) error {
	tok := p.peek()
	if !tok.IsOperator(op) {
		return p.errorf("expected %q, got %s", op, tok)
	}
	p.advance()
	return nil
}

func (p *Parser) errorf(format string, args ...interface{}) error {
	tok := p.peek()
	prefix := fmt.Sprintf("parse error at line %d col %d: ", tok.Line, tok.Col)
	return fmt.Errorf(prefix+format, args...)
}
