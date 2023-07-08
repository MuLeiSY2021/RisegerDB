package org.risegerdb.compile.lextcal;

import lombok.Data;

@Data
public class Lexicon {

    public enum Type {
        NUM,STR,FUN
    }

    private String id;

    private String value;

    private Double doubleValue;

    private Function function;

    private Type type;

    public Lexicon(String id, String value,Lexicon.Type type) {
        this.id = id;
        this.value = value;
        if (type == Type.FUN) {
            function = LexicalTree.INSTANCE.get(value);
        }
        this.type = type;
        this.doubleValue = null;
    }

    public Lexicon(String id, double tmp) {
        this.id = id;
        doubleValue = tmp;
        this.type = Type.NUM;
    }
}
