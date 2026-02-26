package lexer

import (
	"testing"

	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func TestTokenizeSimpleKeywords(t *testing.T) {
	tokens, err := Tokenize("USE DATABASE MAP SEARCH WHERE")
	require.NoError(t, err)
	require.Equal(t, 6, len(tokens)) // 5 keywords + EOF

	assert.Equal(t, TokenKeyword, tokens[0].Type)
	assert.Equal(t, "USE", tokens[0].Value)
	assert.Equal(t, TokenKeyword, tokens[1].Type)
	assert.Equal(t, "DATABASE", tokens[1].Value)
	assert.Equal(t, TokenEOF, tokens[5].Type)
}

func TestTokenizeCaseInsensitive(t *testing.T) {
	tokens, err := Tokenize("use Database Search where")
	require.NoError(t, err)
	assert.Equal(t, "USE", tokens[0].Value)
	assert.Equal(t, "DATABASE", tokens[1].Value)
	assert.Equal(t, "SEARCH", tokens[2].Value)
	assert.Equal(t, "WHERE", tokens[3].Value)
}

func TestTokenizeNumbers(t *testing.T) {
	tokens, err := Tokenize("42 3.14 100")
	require.NoError(t, err)
	assert.Equal(t, TokenNumber, tokens[0].Type)
	assert.Equal(t, "42", tokens[0].Value)
	assert.Equal(t, TokenNumber, tokens[1].Type)
	assert.Equal(t, "3.14", tokens[1].Value)
}

func TestTokenizeStrings(t *testing.T) {
	tokens, err := Tokenize("'hello world' 'test'")
	require.NoError(t, err)
	assert.Equal(t, TokenString, tokens[0].Type)
	assert.Equal(t, "hello world", tokens[0].Value)
	assert.Equal(t, TokenString, tokens[1].Type)
	assert.Equal(t, "test", tokens[1].Value)
}

func TestTokenizeIdentifiers(t *testing.T) {
	tokens, err := Tokenize("name area building_model")
	require.NoError(t, err)
	assert.Equal(t, TokenIdent, tokens[0].Type)
	assert.Equal(t, "name", tokens[0].Value)
	assert.Equal(t, TokenIdent, tokens[1].Type)
	assert.Equal(t, "area", tokens[1].Value)
}

func TestTokenizeOperators(t *testing.T) {
	tokens, err := Tokenize("> >= < <= = + - * / ! , . | ( ) [ ]")
	require.NoError(t, err)
	ops := []string{">", ">=", "<", "<=", "=", "+", "-", "*", "/", "!", ",", ".", "|", "(", ")", "[", "]"}
	for i, expected := range ops {
		assert.Equal(t, TokenOperator, tokens[i].Type, "token %d", i)
		assert.Equal(t, expected, tokens[i].Value, "token %d", i)
	}
}

func TestTokenizeComplexQuery(t *testing.T) {
	query := `USE DATABASE 'test_db' | MAP 'china' SEARCH name, area WHERE area > 100 AND IN RECT([10,20], 50)`
	tokens, err := Tokenize(query)
	require.NoError(t, err)
	assert.True(t, len(tokens) > 10)
}

func TestTokenizeEmpty(t *testing.T) {
	tokens, err := Tokenize("")
	require.NoError(t, err)
	require.Equal(t, 1, len(tokens))
	assert.Equal(t, TokenEOF, tokens[0].Type)
}

func TestTokenizeComments(t *testing.T) {
	tokens, err := Tokenize("USE // this is a comment\nDATABASE")
	require.NoError(t, err)
	assert.Equal(t, "USE", tokens[0].Value)
	assert.Equal(t, "DATABASE", tokens[1].Value)
}

func TestTokenizeLineCol(t *testing.T) {
	tokens, err := Tokenize("USE\nDATABASE")
	require.NoError(t, err)
	assert.Equal(t, 1, tokens[0].Line)
	assert.Equal(t, 2, tokens[1].Line)
}

func TestTokenizeUnterminatedString(t *testing.T) {
	_, err := Tokenize("'unterminated")
	assert.Error(t, err)
	assert.Contains(t, err.Error(), "unterminated")
}

func TestTokenString(t *testing.T) {
	tok := Token{Type: TokenKeyword, Value: "USE", Line: 1, Col: 1}
	assert.Contains(t, tok.String(), "KEYWORD")
	assert.Contains(t, tok.String(), "USE")
}
