package org.riseger.main.compiler.session;

import lombok.Data;
import org.riseger.main.compiler.CompilerConstant;
import org.riseger.main.compiler.lextcal.Tokenizer;
import org.riseger.main.compiler.syntax.SyntaxTree;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class Session {
    private final Tokenizer tokenizer;

    private final SyntaxTree syntaxTree;

    private final String sourcecode;

    private final List<Token> tokens = new LinkedList<>();

    private Map<String, String> stringConstTable = new HashMap<>();

    private Map<String, Double> numberConstTable = new HashMap<>();


    public Session(String sourcecode, Tokenizer tokenizer, SyntaxTree syntaxTree) {
        this.sourcecode = sourcecode;
        this.tokenizer = tokenizer;
        this.syntaxTree = syntaxTree;
    }


    public void run() {
        this.tokenizer.tokenize(context);
        this.parser
    }

    public void add(Token token) {
        this.tokens.add(token);
    }

    public void put(int size, double tmp) {
        this.getNumberConstTable().put(CompilerConstant.NUMBER_PREFIX + "_" + size, tmp);
    }

    public void put(int size, String tmp) {
        this.getStringConstTable().put(CompilerConstant.STRING_PREFIX + "_" + size, tmp);
    }

}
