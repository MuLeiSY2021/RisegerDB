package parser

import (
	"testing"

	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func TestParseUseDatabase(t *testing.T) {
	ast, err := Parse("USE DATABASE test_db")
	require.NoError(t, err)
	require.Equal(t, NodeUse, ast.Type)
	require.GreaterOrEqual(t, len(ast.Children), 1)
	assert.Equal(t, NodeUseDatabase, ast.Children[0].Type)
}

func TestParseUseDatabaseMap(t *testing.T) {
	ast, err := Parse("USE DATABASE test_db | MAP china")
	require.NoError(t, err)
	require.Equal(t, NodeUse, ast.Type)
	require.GreaterOrEqual(t, len(ast.Children), 2)
	assert.Equal(t, NodeUseDatabase, ast.Children[0].Type)
	assert.Equal(t, NodeUseMap, ast.Children[1].Type)
}

func TestParseSearch(t *testing.T) {
	ast, err := Parse("SEARCH name, area")
	require.NoError(t, err)
	require.Equal(t, NodeSearch, ast.Type)
	require.GreaterOrEqual(t, len(ast.Children), 1)
	assert.Equal(t, NodeStrings, ast.Children[0].Type)
}

func TestParseSearchWhere(t *testing.T) {
	ast, err := Parse("SEARCH name WHERE area > 100")
	require.NoError(t, err)
	require.Equal(t, NodeSearch, ast.Type)
	require.Equal(t, 2, len(ast.Children))
	assert.Equal(t, NodeWhere, ast.Children[1].Type)

	where := ast.Children[1]
	assert.Equal(t, NodeGreater, where.Children[0].Type)
}

func TestParseUseSearch(t *testing.T) {
	ast, err := Parse("USE DATABASE test_db | MAP china SEARCH name WHERE area > 50")
	require.NoError(t, err)
	require.Equal(t, NodeUse, ast.Type)
	require.GreaterOrEqual(t, len(ast.Children), 3)
}

func TestParseGetDatabases(t *testing.T) {
	ast, err := Parse("GET DATABASES")
	require.NoError(t, err)
	assert.Equal(t, NodeGetDatabases, ast.Type)
}

func TestParseGetMaps(t *testing.T) {
	ast, err := Parse("GET MAPS")
	require.NoError(t, err)
	assert.Equal(t, NodeGetMaps, ast.Type)
}

func TestParseGetModels(t *testing.T) {
	ast, err := Parse("GET MODELS")
	require.NoError(t, err)
	assert.Equal(t, NodeGetModels, ast.Type)
}

func TestParseWhereAndOr(t *testing.T) {
	ast, err := Parse("SEARCH name WHERE a > 1 AND b < 2 OR c = 3")
	require.NoError(t, err)
	where := ast.Children[1]
	assert.Equal(t, NodeWhere, where.Type)
	assert.Equal(t, NodeOr, where.Children[0].Type)
}

func TestParseWhereNot(t *testing.T) {
	ast, err := Parse("SEARCH name WHERE ! a > 1")
	require.NoError(t, err)
	where := ast.Children[1]
	assert.Equal(t, NodeNot, where.Children[0].Type)
}

func TestParseWhereIn(t *testing.T) {
	ast, err := Parse("SEARCH name WHERE IN RECT([10, 20], 50)")
	require.NoError(t, err)
	where := ast.Children[1]
	assert.Equal(t, NodeIn, where.Children[0].Type)
}

func TestParseArithmetic(t *testing.T) {
	ast, err := Parse("SEARCH name WHERE a + b * 2 > 10")
	require.NoError(t, err)
	where := ast.Children[1]
	cmp := where.Children[0]
	assert.Equal(t, NodeGreater, cmp.Type)
	assert.Equal(t, NodeAdd, cmp.Children[0].Type)
}

func TestParseCoord(t *testing.T) {
	ast, err := Parse("SEARCH name WHERE IN [10, 20]")
	require.NoError(t, err)
	where := ast.Children[1]
	in := where.Children[0]
	assert.Equal(t, NodeIn, in.Type)
	assert.Equal(t, NodeCoordToRect, in.Children[0].Type)
}

func TestParseRect(t *testing.T) {
	ast, err := Parse("USE SCOPE RECT([5, 5], 100)")
	require.NoError(t, err)
	scope := ast.Children[0]
	assert.Equal(t, NodeUseScope, scope.Type)
	assert.Equal(t, NodeRect, scope.Children[0].Type)
}

func TestParseDotString(t *testing.T) {
	ast, err := Parse("SEARCH a.b.c")
	require.NoError(t, err)
	search := ast
	assert.Equal(t, NodeSearch, search.Type)
}

func TestParseUpdate(t *testing.T) {
	ast, err := Parse("USE DATABASE test_db | MAP china UPDATE name = 'new_name' WHERE area > 10")
	require.NoError(t, err)
	assert.Equal(t, NodeUse, ast.Type)
	found := false
	for _, child := range ast.Children {
		if child.Type == NodeUpdate {
			found = true
		}
	}
	assert.True(t, found)
}

func TestParsePreload(t *testing.T) {
	ast, err := Parse("PRELOAD test_data")
	require.NoError(t, err)
	assert.Equal(t, NodePreload, ast.Type)
}

func TestParseError(t *testing.T) {
	_, err := Parse("INVALID QUERY")
	assert.Error(t, err)
}

func TestNodeTypeString(t *testing.T) {
	assert.Equal(t, "SQL", NodeSQL.String())
	assert.Equal(t, "Search", NodeSearch.String())
	assert.Equal(t, "Where", NodeWhere.String())
}
