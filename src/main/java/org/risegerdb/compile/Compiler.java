package org.risegerdb.compile;

import com.google.gson.Gson;
import lombok.Data;
import org.risegerdb.compile.context.Context;
import org.risegerdb.compile.lextcal.TokenAnalyzer;
import org.risegerdb.compile.syntax.Interpreter;
import org.risegerdb.compile.tokenize.Tokenizer;
import org.risegerdb.utils.Utils;

import java.io.File;
import java.util.List;

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
