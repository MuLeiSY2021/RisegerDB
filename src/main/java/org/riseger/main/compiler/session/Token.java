package org.riseger.main.compiler.session;

import lombok.Data;

@Data
public class Token {
    private String sourceCode;

    private String code;

    private TokenType type;

    private int line;

    private int column;

    public Token(String sourceCode, int line, int column) {
        this.sourceCode = sourceCode;
        this.line = line;
        this.column = column;
    }

    public void setCode(String code) {

    }
}
