package org.risegerdb.compile.syntax;

import org.risegerdb.compile.config.CompileConfig;
import org.risegerdb.compile.lextcal.LexicalAnalyzerOutput;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class PreProcessor {
    public static final PreProcessor INSTANCE = new PreProcessor();

    private final Map<String,Function> functions = Function.FUNCTIONS;

    public void execute(List<String> tokens) {

    }

    public void execute(LexicalAnalyzerOutput output) {
        execute(output.getLexemeList());
    }
}
