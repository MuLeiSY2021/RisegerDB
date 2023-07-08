package org.risegerdb.compile.tokenize;

import lombok.Data;

@Data
public class Token {
    private String source;

    private String value;

    public Token() {
    }

    public Token(String source, String value) {
        this.source = source;
        this.value = value;
    }
}
