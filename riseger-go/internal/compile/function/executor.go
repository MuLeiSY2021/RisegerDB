package function

import (
	"fmt"
	"strconv"

	"github.com/riseger/riseger-go/internal/cache"
	"github.com/riseger/riseger-go/internal/compile/parser"
	"github.com/riseger/riseger-go/pkg/rtree"
)

// Execute walks the AST and evaluates the query against the given context.
func Execute(node *parser.Node, ctx *Context) (*ResultSet, error) {
	if err := eval(node, ctx); err != nil {
		return nil, err
	}
	if ctx.Result != nil {
		return ctx.Result, nil
	}
	return EmptyResultSet(), nil
}

func eval(node *parser.Node, ctx *Context) error {
	switch node.Type {
	case parser.NodeSQL:
		for _, child := range node.Children {
			if err := eval(child, ctx); err != nil {
				return err
			}
		}
		return nil

	// ---------- USE ----------
	case parser.NodeUse:
		for _, child := range node.Children {
			if err := eval(child, ctx); err != nil {
				return err
			}
		}
		return nil

	case parser.NodeUseDatabase:
		name := extractString(node.Children[0])
		if ctx.Database != nil && ctx.Database.Name == name {
			return nil
		}
		ctx.Push(name)
		return nil

	case parser.NodeUseMap:
		name := extractString(node.Children[0])
		if ctx.Database == nil {
			return fmt.Errorf("no database selected")
		}
		m, ok := ctx.Database.GetMap(name)
		if !ok {
			return fmt.Errorf("map %q not found", name)
		}
		ctx.Map = m
		ctx.Threshold = m.Threshold()
		return nil

	case parser.NodeUseScope:
		if err := eval(node.Children[0], ctx); err != nil {
			return err
		}
		v := ctx.Pop()
		if rect, ok := v.(rtree.Rectangle); ok {
			ctx.Scope = rect
		}
		return nil

	case parser.NodeUseModel:
		if err := eval(node.Children[0], ctx); err != nil {
			return err
		}
		v := ctx.Pop()
		if models, ok := v.([]string); ok {
			ctx.Models = models
		}
		return nil

	// ---------- SEARCH ----------
	case parser.NodeSearch:
		if err := eval(node.Children[0], ctx); err != nil {
			return err
		}
		columns := ctx.Pop()
		var colNames []string
		switch v := columns.(type) {
		case []string:
			colNames = v
		case string:
			colNames = []string{v}
		}

		var whereFn func(elem rtree.Rectangle) bool
		if len(node.Children) > 1 {
			whereNode := node.Children[1]
			whereFn = func(elem rtree.Rectangle) bool {
				subCtx := *ctx
				subCtx.Stack = make([]interface{}, 0, 16)
				subCtx.Push(elem)
				if err := eval(whereNode, &subCtx); err != nil {
					return false
				}
				return subCtx.PopBool()
			}
		}

		ctx.Result = executeSearch(ctx, colNames, whereFn)
		return nil

	case parser.NodeWhere:
		return eval(node.Children[0], ctx)

	// ---------- GET ----------
	case parser.NodeGetDatabases:
		rs := NewResultSet()
		rs.Columns = []string{"database"}
		ctx.Result = rs
		return nil

	case parser.NodeGetMaps:
		rs := NewResultSet()
		rs.Columns = []string{"map"}
		if ctx.Database != nil {
			for _, m := range ctx.Database.ListMaps() {
				rs.AddRow(map[string]interface{}{"map": m.Name})
			}
		}
		ctx.Result = rs
		return nil

	case parser.NodeGetModels:
		rs := NewResultSet()
		rs.Columns = []string{"model", "parent"}
		if ctx.Database != nil {
			for _, m := range ctx.Database.ListModels() {
				rs.AddRow(map[string]interface{}{"model": m.Name, "parent": m.Parent})
			}
		}
		ctx.Result = rs
		return nil

	// ---------- UPDATE ----------
	case parser.NodeUpdate:
		for _, child := range node.Children {
			if err := eval(child, ctx); err != nil {
				return err
			}
		}
		return nil

	case parser.NodeUpdateClause:
		if err := eval(node.Children[0], ctx); err != nil {
			return err
		}
		attrName := ctx.PopString()
		if err := eval(node.Children[1], ctx); err != nil {
			return err
		}
		value := ctx.Pop()
		_ = attrName
		_ = value
		return nil

	// ---------- PRELOAD ----------
	case parser.NodePreload:
		path := extractString(node.Children[0])
		if ctx.Preload == nil {
			return fmt.Errorf("PRELOAD not supported: no preload handler registered")
		}
		taskID, err := ctx.Preload(path)
		if err != nil {
			return fmt.Errorf("PRELOAD failed: %w", err)
		}
		rs := NewResultSet()
		rs.Columns = []string{"status", "task_id", "file"}
		rs.AddRow(map[string]interface{}{
			"status":  "accepted",
			"task_id": taskID,
			"file":    path,
		})
		ctx.Result = rs
		return nil

	// ---------- Boolean / Logic ----------
	case parser.NodeAnd:
		if err := eval(node.Children[0], ctx); err != nil {
			return err
		}
		left := ctx.PopBool()
		if !left {
			ctx.Push(false)
			return nil
		}
		if err := eval(node.Children[1], ctx); err != nil {
			return err
		}
		right := ctx.PopBool()
		ctx.Push(left && right)
		return nil

	case parser.NodeOr:
		if err := eval(node.Children[0], ctx); err != nil {
			return err
		}
		left := ctx.PopBool()
		if left {
			ctx.Push(true)
			return nil
		}
		if err := eval(node.Children[1], ctx); err != nil {
			return err
		}
		right := ctx.PopBool()
		ctx.Push(left || right)
		return nil

	case parser.NodeNot:
		if err := eval(node.Children[0], ctx); err != nil {
			return err
		}
		ctx.Push(!ctx.PopBool())
		return nil

	// ---------- Comparison ----------
	case parser.NodeGreater:
		return evalCmp(node, ctx, func(a, b float64) bool { return a > b })
	case parser.NodeGreaterEq:
		return evalCmp(node, ctx, func(a, b float64) bool { return a >= b })
	case parser.NodeLess:
		return evalCmp(node, ctx, func(a, b float64) bool { return a < b })
	case parser.NodeLessEq:
		return evalCmp(node, ctx, func(a, b float64) bool { return a <= b })
	case parser.NodeEqual:
		return evalCmp(node, ctx, func(a, b float64) bool { return a == b })

	// ---------- Arithmetic ----------
	case parser.NodeAdd:
		return evalArith(node, ctx, func(a, b float64) float64 { return a + b })
	case parser.NodeSub:
		return evalArith(node, ctx, func(a, b float64) float64 { return a - b })
	case parser.NodeMul:
		return evalArith(node, ctx, func(a, b float64) float64 { return a * b })
	case parser.NodeDiv:
		return evalArith(node, ctx, func(a, b float64) float64 {
			if b == 0 {
				return 0
			}
			return a / b
		})
	case parser.NodeNegate:
		if err := eval(node.Children[0], ctx); err != nil {
			return err
		}
		ctx.Push(-ctx.PopFloat())
		return nil

	// ---------- Spatial ----------
	case parser.NodeIn:
		if err := eval(node.Children[0], ctx); err != nil {
			return err
		}
		scope := ctx.Pop()
		elem := peekElement(ctx)
		if rect, ok := scope.(rtree.Rectangle); ok && elem != nil {
			ctx.Push(rect.Contains(elem))
		} else {
			ctx.Push(false)
		}
		return nil

	case parser.NodeOut:
		if err := eval(node.Children[0], ctx); err != nil {
			return err
		}
		scope := ctx.Pop()
		elem := peekElement(ctx)
		if rect, ok := scope.(rtree.Rectangle); ok && elem != nil {
			ctx.Push(!rect.Contains(elem))
		} else {
			ctx.Push(true)
		}
		return nil

	case parser.NodeCoord:
		if err := eval(node.Children[0], ctx); err != nil {
			return err
		}
		x := ctx.PopFloat()
		if err := eval(node.Children[1], ctx); err != nil {
			return err
		}
		y := ctx.PopFloat()
		ctx.Push(rtree.NewRect(x, y, x, y, ctx.Threshold))
		return nil

	case parser.NodeRect:
		if err := eval(node.Children[0], ctx); err != nil {
			return err
		}
		center := ctx.Pop()
		if err := eval(node.Children[1], ctx); err != nil {
			return err
		}
		size := ctx.PopFloat()
		if coord, ok := center.(rtree.Rectangle); ok {
			cx := (coord.MinX() + coord.MaxX()) / 2
			cy := (coord.MinY() + coord.MaxY()) / 2
			rect := rtree.NewRect(cx-size, cy-size, cx+size, cy+size, ctx.Threshold)
			ctx.Push(rect)
		}
		return nil

	case parser.NodeCoordToRect:
		if err := eval(node.Children[0], ctx); err != nil {
			return err
		}
		return nil

	case parser.NodeCoords:
		for _, child := range node.Children {
			if err := eval(child, ctx); err != nil {
				return err
			}
		}
		return nil

	// ---------- Entity / Leaf ----------
	case parser.NodeAttribute:
		if err := eval(node.Children[0], ctx); err != nil {
			return err
		}
		name := ctx.PopString()
		elem := peekElement(ctx)
		if e, ok := elem.(*cache.Element); ok {
			val, _ := e.GetAttribute(name)
			ctx.Push(val)
		} else {
			ctx.Push(name)
		}
		return nil

	case parser.NodeDotString:
		if node.Value != "" {
			ctx.Push(node.Value)
			return nil
		}
		var parts []string
		for _, child := range node.Children {
			parts = append(parts, child.Value)
		}
		if len(parts) == 1 {
			ctx.Push(parts[0])
		} else {
			ctx.Push(parts[len(parts)-1])
		}
		return nil

	case parser.NodeStrings:
		var names []string
		for _, child := range node.Children {
			if err := eval(child, ctx); err != nil {
				return err
			}
			names = append(names, ctx.PopString())
		}
		ctx.Push(names)
		return nil

	case parser.NodeNumber:
		f, _ := strconv.ParseFloat(node.Value, 64)
		ctx.Push(f)
		return nil

	case parser.NodeString:
		ctx.Push(node.Value)
		return nil

	default:
		return fmt.Errorf("unsupported node type: %s", node.Type)
	}
}

func evalCmp(node *parser.Node, ctx *Context, cmp func(a, b float64) bool) error {
	if err := eval(node.Children[0], ctx); err != nil {
		return err
	}
	a := ctx.PopFloat()
	if err := eval(node.Children[1], ctx); err != nil {
		return err
	}
	b := ctx.PopFloat()
	ctx.Push(cmp(a, b))
	return nil
}

func evalArith(node *parser.Node, ctx *Context, op func(a, b float64) float64) error {
	if err := eval(node.Children[0], ctx); err != nil {
		return err
	}
	a := ctx.PopFloat()
	if err := eval(node.Children[1], ctx); err != nil {
		return err
	}
	b := ctx.PopFloat()
	ctx.Push(op(a, b))
	return nil
}

func extractString(n *parser.Node) string {
	if n.Value != "" {
		return n.Value
	}
	if len(n.Children) > 0 {
		return extractString(n.Children[0])
	}
	return ""
}

func peekElement(ctx *Context) rtree.Rectangle {
	for i := len(ctx.Stack) - 1; i >= 0; i-- {
		if r, ok := ctx.Stack[i].(rtree.Rectangle); ok {
			return r
		}
	}
	return nil
}

func executeSearch(ctx *Context, columns []string, whereFn func(rtree.Rectangle) bool) *ResultSet {
	rs := NewResultSet()
	rs.Columns = columns

	if ctx.Map == nil {
		return rs
	}

	for _, layer := range ctx.Map.ListLayers() {
		if layer.IsSubMap() {
			continue
		}
		for _, elem := range layer.Elements() {
			if whereFn != nil && !whereFn(elem) {
				continue
			}
			row := make(map[string]interface{})
			if e, ok := elem.(*cache.Element); ok {
				for _, col := range columns {
					val, _ := e.GetAttribute(col)
					row[col] = val
				}
			}
			rs.AddRow(row)
		}
	}
	return rs
}
