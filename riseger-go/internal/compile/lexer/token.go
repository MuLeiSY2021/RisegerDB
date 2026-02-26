package lexer

import "fmt"

// TokenType classifies tokens produced by the lexer.
type TokenType int

const (
	TokenNumber     TokenType = iota // numeric literal: 3.14, 100
	TokenString                      // quoted string: 'hello'
	TokenIdent                       // identifier / attribute name: name, area
	TokenKeyword                     // reserved keyword: USE, SEARCH, WHERE, ...
	TokenOperator                    // single-char operators: > < = + - * / ! , . | ( ) [ ]
	TokenEOF                         // end of input
)

func (t TokenType) String() string {
	names := [...]string{"NUMBER", "STRING", "IDENT", "KEYWORD", "OPERATOR", "EOF"}
	if int(t) < len(names) {
		return names[t]
	}
	return "UNKNOWN"
}

// Token is a lexical unit produced by the lexer.
type Token struct {
	Type    TokenType
	Value   string
	Line    int
	Col     int
}

func (t Token) String() string {
	return fmt.Sprintf("%s(%q)@%d:%d", t.Type, t.Value, t.Line, t.Col)
}

func (t Token) IsKeyword(kw string) bool {
	return t.Type == TokenKeyword && t.Value == kw
}

func (t Token) IsOperator(op string) bool {
	return t.Type == TokenOperator && t.Value == op
}
