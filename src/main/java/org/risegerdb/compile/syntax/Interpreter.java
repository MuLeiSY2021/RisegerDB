package org.risegerdb.compile.syntax;

import org.risegerdb.compile.lextcal.LexicalAnalyzerOutput;

public class Interpreter {
    public static final Interpreter INSTANCE = new Interpreter();

    private PreProcessor processor = PreProcessor.INSTANCE;

    private SyntaxTree tree = SyntaxTree.INSTANCE;

    public InterpreterOutput execute(LexicalAnalyzerOutput input) {
        InterpreterOutput output = new InterpreterOutput();



        return output;
    }
}
