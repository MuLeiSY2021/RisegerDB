package org.riseger.main.system.compile.token;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import lombok.Data;
import org.riseger.main.system.compile.lextcal.Keyword;
import org.riseger.protocol.serializer.JsonSerializer;

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

    public static String parseEntity(Object entity) {

        return null;
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
                    //TODO:临时兼容无属性名的设计
                    return sourceCode;
                }

            case NUMBER:
                return Double.parseDouble(sourceCode);

            case ATTRIBUTE:
                return sourceCode;

            default:
                throw new IllegalArgumentException("非法的实体获取");
        }
    }

    public static Object parseEntity(JsonElement entityElement) {
        if (entityElement != null) {
            if (entityElement.isJsonPrimitive()) {
                JsonPrimitive primitive = entityElement.getAsJsonPrimitive();
                if (primitive.isNumber()) {
                    // 处理数字
                    return primitive.getAsDouble();
                } else if (primitive.isString()) {
                    // 处理字符串
                    return primitive.getAsString();
                } else if (primitive.isBoolean()) {
                    // 处理布尔值
                    return primitive.getAsBoolean();
                }
            } else if (entityElement.isJsonNull()) {
                // 处理 null 值
                return null;
            } else if (entityElement.isJsonArray() || entityElement.isJsonObject()) {
                // 处理复杂对象 (可以根据需要扩展处理逻辑)
                return entityElement.toString();
            }
        }
        return null; // 默认返回 null
    }
}
