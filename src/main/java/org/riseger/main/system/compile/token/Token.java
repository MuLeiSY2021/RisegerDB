package org.riseger.main.system.compile.token;

import lombok.Data;
import org.riseger.main.system.compile.lextcal.Keyword;
import org.riseger.protoctl.serializer.JsonSerializer;

import java.util.Objects;

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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Token token = (Token) object;
        return id == token.id && line == token.line && column == token.column && type == token.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }

    public Object getEntity() {
        switch (type) {
            case STRING:
                if (sourceCode.startsWith("'")) {
                    return sourceCode.substring(1, sourceCode.length() - 1);
                } else {
                    return sourceCode;
                }

            case NUMBER:
                return Double.parseDouble(sourceCode);

            default:
                throw new IllegalArgumentException("非法的实体获取");
        }
    }
}
