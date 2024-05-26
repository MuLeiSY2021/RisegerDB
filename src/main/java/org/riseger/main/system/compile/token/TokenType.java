package org.riseger.main.system.compile.token;

import org.riseger.main.system.compile.CompilerConstant;

public enum TokenType {
    NUMBER(CompilerConstant.NUMBER_PREFIX),
    ATTRIBUTE(CompilerConstant.ATTRIBUTE_PREFIX),

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

    public static TokenType fromString(String type) {
        switch (type) {
            case "string":
                return TokenType.STRING;

            case "attribute":
                return TokenType.ATTRIBUTE;

            case "number":
                return TokenType.NUMBER;

            default:
                return null;
        }
    }
}
