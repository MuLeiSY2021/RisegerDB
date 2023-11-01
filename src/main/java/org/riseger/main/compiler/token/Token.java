package org.riseger.main.compiler.token;

import lombok.Data;
import org.riseger.main.compiler.lextcal.Keyword;
import org.riseger.protoctl.serializer.JsonSerializer;

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
        return JsonSerializer.serializeToString(this);
    }

    public void set(int id, TokenType tokenType) {
        this.id = id;
        this.type = tokenType;
    }

    public boolean equals(Keyword keyword) {
        if (keyword != null) {
            return keyword.getId() == this.id;
        }
        return false;
    }

    public boolean isKeyword() {
        return this.type == TokenType.KEYWORD;
    }
}
