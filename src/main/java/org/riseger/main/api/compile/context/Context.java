package org.riseger.main.api.compile.context;

import lombok.Data;
import org.riseger.main.api.compile.config.Config;
import org.riseger.main.api.compile.tokenize.Token;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class Context {
    private final String sourcecode;

    public Context(String sourcecode) {
        this.sourcecode = sourcecode;
    }

    private final List<Token> tokens = new LinkedList<>();

    private final List<List<Token>> lines = new LinkedList<>();

    private Map<String, String> stringConstTable = new HashMap<>();

    private Map<String, Double> numberConstTable = new HashMap<>();

    public void addAll(List<Token> lines) {
        this.tokens.addAll(lines);
    }

    public void add(Token token) {
        this.tokens.add(token);
    }

    public void put(int size, double tmp) {
        this.getNumberConstTable().put(Config.NUMBER_PREFIX + "_" + size, tmp);
    }

    public void put(int size, String tmp) {
        this.getStringConstTable().put(Config.STRING_PREFIX + "_" + size, tmp);
    }

    public void addLine(List<Token> tmp) {
        this.lines.add(tmp);
    }
}
