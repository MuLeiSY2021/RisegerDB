package org.risegerdb.compile.lextcal;

import lombok.Data;

import java.util.Objects;

@Data
public class Lexicon {

    public enum Type {
        NUM,STR,FUN,VAR
    }

    private transient String id;

    private String value;

    private Double doubleValue;

    private transient Function function;

    private transient Type type;

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

    public Lexicon(String id, String type) {
        this.id = id;
        this.type = Type.valueOf(type);
        this.doubleValue = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof String && o.equals(this.value)) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) return false;
        Lexicon lexicon = (Lexicon) o;
        return Objects.equals(getValue(), lexicon.getValue());
    }

    public boolean equals(String value) {
        if(value == null || this.value == null) return false;
        return value.equals(this.value);
    }

    public boolean equals(Type type) {
        if(this.type == null || type == null) return false;
        return type.equals(this.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
