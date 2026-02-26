package parser

// NodeType identifies the kind of AST node.
type NodeType int

const (
	NodeSQL NodeType = iota
	NodeUse
	NodeUseDatabase
	NodeUseMap
	NodeUseScope
	NodeUseModel
	NodeSearch
	NodeUpdate
	NodeUpdateClause
	NodeWhere
	NodePreload
	NodeGetDatabases
	NodeGetMaps
	NodeGetModels
	// DDL
	NodeCreateDatabase
	NodeCreateMap
	NodeCreateModel
	NodeCreateModelParam
	NodeDelete
	// Boolean / logic
	NodeAnd
	NodeOr
	NodeNot
	// Comparison
	NodeGreater
	NodeGreaterEq
	NodeLess
	NodeLessEq
	NodeEqual
	// Arithmetic
	NodeAdd
	NodeSub
	NodeMul
	NodeDiv
	NodeNegate
	// Spatial
	NodeIn
	NodeOut
	NodeRect
	NodeCoord
	NodeCoords
	NodeCoordToRect
	// Entity / leaf
	NodeAttribute
	NodeDotString
	NodeStrings
	NodeNumber
	NodeString
)

var nodeTypeNames = [...]string{
	"SQL", "Use", "UseDatabase", "UseMap", "UseScope", "UseModel",
	"Search", "Update", "UpdateClause", "Where", "Preload",
	"GetDatabases", "GetMaps", "GetModels",
	"CreateDatabase", "CreateMap", "CreateModel", "CreateModelParam", "Delete",
	"And", "Or", "Not",
	"Greater", "GreaterEq", "Less", "LessEq", "Equal",
	"Add", "Sub", "Mul", "Div", "Negate",
	"In", "Out", "Rect", "Coord", "Coords", "CoordToRect",
	"Attribute", "DotString", "Strings", "Number", "String",
}

func (n NodeType) String() string {
	if int(n) < len(nodeTypeNames) {
		return nodeTypeNames[n]
	}
	return "?"
}

// Node is a node in the abstract syntax tree.
type Node struct {
	Type     NodeType
	Value    string  // leaf value for literals
	Children []*Node
}

func newNode(typ NodeType, children ...*Node) *Node {
	return &Node{Type: typ, Children: children}
}

func leafNode(typ NodeType, value string) *Node {
	return &Node{Type: typ, Value: value}
}
