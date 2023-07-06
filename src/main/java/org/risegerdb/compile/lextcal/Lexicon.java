package org.risegerdb.compile.lextcal;

import lombok.Data;
import org.risegerdb.compile.tokenize.Token;

@Data
public class Lexicon {




    public enum Type {
        NUM,STR,FUN
    }


    private String id;

    private String value;

    private double doubleValue;

    private Token tokenValue;

    private Type type;

    public Lexicon(String id, String value,Lexicon.Type type) {
        this.id = id;
        this.value = value;
        if (type == Type.FUN) {
            tokenValue = LexicalTree.INSTANCE.get(value);
        }
        this.type = type;
    }

    public Lexicon(String id, double tmp) {
        this.id = id;
        doubleValue = tmp;
        this.type = Type.NUM;
    }
}
