package org.risegerdb.compile;

import com.google.gson.Gson;
import org.risegerdb.compile.lextcal.LexicalAnalyzer;
import org.risegerdb.compile.lextcal.LexicalAnalyzerOutput;
import org.risegerdb.compile.syntax.Interpreter;
import org.risegerdb.compile.syntax.PreProcessor;
import org.risegerdb.compile.syntax.SyntaxTree;
import org.risegerdb.compile.tokenize.Tokenizer;
import org.risegerdb.utils.Utils;

import java.io.File;
import java.util.List;

public class Compiler {
    private final Tokenizer tokenizer = Tokenizer.INSTANCE;

    private final PreProcessor processor = PreProcessor.INSTANCE;

    private final LexicalAnalyzer lexicalAnalyzer = LexicalAnalyzer.INSTANCE;

    private final Interpreter interpreter = Interpreter.INSTANCE;


    public String compile(String code) {
        List<String> result = tokenizer.execute(code);
        LexicalAnalyzerOutput output = lexicalAnalyzer.execute(result);
        processor.execute(output);
        return new Gson().toJson(output);
    }

    public String compile(File file) {
        String code = Utils.getText(file);
        return compile(code);
    }



    public static void main(String[] args) {
        File code = new File("./src/main/resources/code.txt");
        Compiler compiler = new Compiler();
        String result = compiler.compile(code);
        System.out.println(result);
    }
}
