package compile

import (
	"fmt"

	"github.com/riseger/riseger-go/internal/cache"
	"github.com/riseger/riseger-go/internal/compile/function"
	"github.com/riseger/riseger-go/internal/compile/parser"
)

// Compiler compiles and executes SQL-like queries against the database engine.
type Compiler struct {
	cache   *cache.CacheManager
	preload function.PreloadFunc
}

func NewCompiler(cache *cache.CacheManager) *Compiler {
	return &Compiler{cache: cache}
}

// SetPreloadHandler registers the callback that handles PRELOAD commands.
// This is called by the engine after construction to wire up the import pipeline.
func (c *Compiler) SetPreloadHandler(fn function.PreloadFunc) {
	c.preload = fn
}

// Execute parses and executes a query string. The context (database, map, etc.)
// is built up from USE statements in the query.
func (c *Compiler) Execute(query string) (*function.ResultSet, error) {
	ast, err := parser.Parse(query)
	if err != nil {
		return nil, fmt.Errorf("compile error: %w", err)
	}
	return c.ExecuteAST(ast)
}

// ExecuteAST executes a pre-parsed AST.
func (c *Compiler) ExecuteAST(ast *parser.Node) (*function.ResultSet, error) {
	ctx := function.NewContext()
	ctx.Preload = c.preload

	c.resolveDatabase(ast, ctx)

	rs, err := function.Execute(ast, ctx)
	if err != nil {
		return nil, fmt.Errorf("execution error: %w", err)
	}
	return rs, nil
}

// resolveDatabase scans USE DATABASE clauses to set the database context.
func (c *Compiler) resolveDatabase(ast *parser.Node, ctx *function.Context) {
	if ast.Type == parser.NodeUse {
		for _, child := range ast.Children {
			if child.Type == parser.NodeUseDatabase && len(child.Children) > 0 {
				name := extractStringValue(child.Children[0])
				if db, ok := c.cache.GetDatabase(name); ok {
					ctx.Database = db
				}
			}
		}
	}
}

func extractStringValue(n *parser.Node) string {
	if n.Value != "" {
		return n.Value
	}
	if len(n.Children) > 0 {
		return extractStringValue(n.Children[0])
	}
	return ""
}
