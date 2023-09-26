package org.riseger.main.compiler.session;

import lombok.Data;
import org.riseger.main.compiler.lextcal.Lexicator;
import org.riseger.main.compiler.syntax.Parser;
import org.riseger.main.compiler.token.Token;
import org.riseger.main.compiler.token.Tokenizer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Session {
    private final Tokenizer tokenizer;

    private final Lexicator lexicator;

    private final String sourcecode;

    private final Parser parser;
    private final Map<Integer, String> stringConstTable = new HashMap<>();
    private final Map<String, Integer> stringIndexTable = new HashMap<>();
    private List<Token> tokenList;
    private Map<Integer, Double> numberConstTable = new HashMap<>();
    private Map<Double, Integer> numberIndexTable = new HashMap<>();


    public Session(String sourcecode, Tokenizer tokenizer, Lexicator lexicator, Parser parser) {
        this.sourcecode = sourcecode;
        this.tokenizer = tokenizer;
        this.parser = parser;
        this.lexicator = lexicator;
    }


    public void run() {
        this.tokenList = this.tokenizer.invoke(sourcecode);
        this.lexicator.invoke(tokenList, this);
        this.parser.invoke(tokenList);
    }

    public int put(double tmp) {
        int i;
        if (!numberIndexTable.containsKey(tmp)) {
            i = numberConstTable.size();
            numberConstTable.put(i, tmp);
            numberIndexTable.put(tmp, i);
        } else {
            i = numberIndexTable.get(tmp);
        }
        return i;
    }

    public int put(String tmp) {
        int i;
        if (!stringIndexTable.containsKey(tmp)) {
            i = stringConstTable.size();
            stringConstTable.put(i, tmp);
            stringIndexTable.put(tmp, i);
        } else {
            i = stringIndexTable.get(tmp);
        }
        return i;
    }
}
