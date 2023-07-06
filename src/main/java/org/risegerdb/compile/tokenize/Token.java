package org.risegerdb.compile.tokenize;

import lombok.Data;

import java.util.Objects;

@Data
public class Token {
    private static Integer COUNT = 0;

    private String name;

    private int id;

    public Token() {
    }

    public Token(String name) {
        this.name = name;
        this.id = COUNT++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return getName().equals(token.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    public static Token tmpToken(String name) {
        Token res = new Token();
        res.setName(name);
        return res;
    }
}
