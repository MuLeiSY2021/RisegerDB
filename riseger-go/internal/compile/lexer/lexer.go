package lexer

import (
	"fmt"
	"strings"
	"unicode"
)

// Keywords recognized by the lexer (case-insensitive matching, stored uppercase).
var keywords = map[string]bool{
	"USE": true, "DATABASE": true, "MAP": true, "SCOPE": true, "MODEL": true,
	"SEARCH": true, "WHERE": true, "UPDATE": true, "PRELOAD": true,
	"GET": true, "DATABASES": true, "MAPS": true, "MODELS": true,
	"AND": true, "OR": true, "NOT": true, "IN": true, "OUT": true,
	"RECT": true, "AS": true, "CREATE": true, "DELETE": true,
	"SET": true, "PARENT": true, "PARAM": true, "THRESHOLD": true,
	"NODESIZE": true, "FROM": true,
}

// Lexer performs lexical analysis on SQL-like query strings.
type Lexer struct {
	input []rune
	pos   int
	line  int
	col   int
}

func NewLexer(input string) *Lexer {
	return &Lexer{
		input: []rune(input),
		pos:   0,
		line:  1,
		col:   1,
	}
}

// Tokenize returns all tokens from the input.
func Tokenize(input string) ([]Token, error) {
	l := NewLexer(input)
	var tokens []Token
	for {
		tok, err := l.Next()
		if err != nil {
			return nil, err
		}
		tokens = append(tokens, tok)
		if tok.Type == TokenEOF {
			break
		}
	}
	return tokens, nil
}

// Next returns the next token.
func (l *Lexer) Next() (Token, error) {
	l.skipWhitespace()
	if l.pos >= len(l.input) {
		return Token{Type: TokenEOF, Line: l.line, Col: l.col}, nil
	}

	ch := l.input[l.pos]

	if ch == '\'' {
		return l.readString()
	}

	if ch == '>' && l.peek(1) == '=' {
		tok := Token{Type: TokenOperator, Value: ">=", Line: l.line, Col: l.col}
		l.advance(2)
		return tok, nil
	}
	if ch == '<' && l.peek(1) == '=' {
		tok := Token{Type: TokenOperator, Value: "<=", Line: l.line, Col: l.col}
		l.advance(2)
		return tok, nil
	}

	if isOperator(ch) {
		tok := Token{Type: TokenOperator, Value: string(ch), Line: l.line, Col: l.col}
		l.advance(1)
		return tok, nil
	}

	if isDigit(ch) || (ch == '-' && l.pos+1 < len(l.input) && isDigit(l.input[l.pos+1]) && l.shouldBeNegativeNumber()) {
		return l.readNumber()
	}

	if isIdentStart(ch) {
		return l.readIdentOrKeyword()
	}

	return Token{}, fmt.Errorf("unexpected character %q at line %d col %d", string(ch), l.line, l.col)
}

func (l *Lexer) readString() (Token, error) {
	line, col := l.line, l.col
	l.advance(1) // skip opening '
	var sb strings.Builder
	for l.pos < len(l.input) && l.input[l.pos] != '\'' {
		sb.WriteRune(l.input[l.pos])
		l.advance(1)
	}
	if l.pos >= len(l.input) {
		return Token{}, fmt.Errorf("unterminated string at line %d col %d", line, col)
	}
	l.advance(1) // skip closing '
	return Token{Type: TokenString, Value: sb.String(), Line: line, Col: col}, nil
}

func (l *Lexer) readNumber() (Token, error) {
	line, col := l.line, l.col
	var sb strings.Builder
	if l.input[l.pos] == '-' {
		sb.WriteRune('-')
		l.advance(1)
	}
	for l.pos < len(l.input) && (isDigit(l.input[l.pos]) || l.input[l.pos] == '.') {
		sb.WriteRune(l.input[l.pos])
		l.advance(1)
	}
	return Token{Type: TokenNumber, Value: sb.String(), Line: line, Col: col}, nil
}

func (l *Lexer) readIdentOrKeyword() (Token, error) {
	line, col := l.line, l.col
	var sb strings.Builder
	for l.pos < len(l.input) && isIdentPart(l.input[l.pos]) {
		sb.WriteRune(l.input[l.pos])
		l.advance(1)
	}
	word := sb.String()
	upper := strings.ToUpper(word)
	if keywords[upper] {
		return Token{Type: TokenKeyword, Value: upper, Line: line, Col: col}, nil
	}
	return Token{Type: TokenIdent, Value: word, Line: line, Col: col}, nil
}

func (l *Lexer) skipWhitespace() {
	for l.pos < len(l.input) {
		ch := l.input[l.pos]
		if ch == '/' && l.peek(1) == '/' {
			for l.pos < len(l.input) && l.input[l.pos] != '\n' {
				l.advance(1)
			}
			continue
		}
		if ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n' {
			l.advance(1)
			continue
		}
		break
	}
}

func (l *Lexer) advance(n int) {
	for i := 0; i < n && l.pos < len(l.input); i++ {
		if l.input[l.pos] == '\n' {
			l.line++
			l.col = 1
		} else {
			l.col++
		}
		l.pos++
	}
}

func (l *Lexer) peek(offset int) rune {
	p := l.pos + offset
	if p >= len(l.input) {
		return 0
	}
	return l.input[p]
}

// shouldBeNegativeNumber determines if '-' is a unary minus (negative number)
// vs. a subtraction operator by checking what came before.
func (l *Lexer) shouldBeNegativeNumber() bool {
	if l.pos == 0 {
		return true
	}
	prev := l.pos - 1
	for prev >= 0 && (l.input[prev] == ' ' || l.input[prev] == '\t') {
		prev--
	}
	if prev < 0 {
		return true
	}
	ch := l.input[prev]
	return isOperator(ch) && ch != ')' && ch != ']'
}

func isDigit(ch rune) bool {
	return ch >= '0' && ch <= '9'
}

func isIdentStart(ch rune) bool {
	return unicode.IsLetter(ch) || ch == '_'
}

func isIdentPart(ch rune) bool {
	return unicode.IsLetter(ch) || unicode.IsDigit(ch) || ch == '_'
}

func isOperator(ch rune) bool {
	switch ch {
	case '>', '<', '=', '+', '-', '*', '/', '!', ',', '.', '|', '(', ')', '[', ']':
		return true
	}
	return false
}
