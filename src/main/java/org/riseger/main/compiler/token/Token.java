package org.riseger.main.compiler.token;

import lombok.Data;
import org.riseger.main.compiler.CompilerConstant;

@Data
public class Token {
    private String sourceCode;

    private String code;

    private int id;

    private TokenType type;

    private int line;

    private int column;

    public Token(String sourceCode, int line, int column) {
        this.sourceCode = sourceCode;
        this.line = line;
        this.column = column;
    }

    @Override
    public String toString() {
        return type.toString() + CompilerConstant.SPLIT_PREFIX + this.id;
    }

    public void set(int id, TokenType tokenType) {
        this.id = id;
        this.type = tokenType;
    }
}
