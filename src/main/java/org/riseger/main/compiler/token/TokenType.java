package org.riseger.main.compiler.token;

import org.riseger.main.compiler.CompilerConstant;

public enum TokenType {
    NUMBER(CompilerConstant.NUMBER_PREFIX),
    KEYWORD(CompilerConstant.KEYWORD_PREFIX),
    STRING(CompilerConstant.STRING_PREFIX);

    private final String type;

    TokenType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
