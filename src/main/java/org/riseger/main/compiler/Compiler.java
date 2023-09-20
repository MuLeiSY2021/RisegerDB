package org.riseger.main.compiler;

import lombok.Data;
import org.riseger.main.compiler.lextcal.TokenAnalyzer;
import org.riseger.main.compiler.lextcal.Tokenizer;
import org.riseger.main.compiler.syntax.Interpreter;

@Data
public class Compiler {
    public final Context context;

    private final Tokenizer tokenizer;

    private final TokenAnalyzer tokenAnalyzer;

    private final Interpreter interpreter;

    public Compiler(Context context) {
        this.context = context;
        tokenizer = new Tokenizer(context);
        tokenAnalyzer = new TokenAnalyzer(context);
        interpreter = new Interpreter(context);
    }

    public void compile() {
        tokenizer.execute();
        tokenAnalyzer.execute();
        interpreter.execute();
    }

    public void tokenAnalyze() {
        tokenizer.execute();
        tokenAnalyzer.execute();
    }

    public void tokenize() {
        tokenizer.execute();
    }
}
