package org.riseger.main.api.compile.tokenize;

import lombok.Data;

@Data
public class Token {
    private String sourceCode;

    private String code;

    private int line;

    private int column;

    public Token(String sourceCode, int line, int column) {
        this.sourceCode = sourceCode;
        this.line = line;
        this.column = column;
    }

    public Token(String code) {
        this.code = code;
    }
}
