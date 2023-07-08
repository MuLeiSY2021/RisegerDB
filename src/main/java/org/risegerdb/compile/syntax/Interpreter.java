package org.risegerdb.compile.syntax;

import com.google.gson.Gson;
import org.risegerdb.compile.config.CompileConfig;
import org.risegerdb.compile.lextcal.Function;
import org.risegerdb.compile.lextcal.LexicalAnalyzerOutput;
import org.risegerdb.compile.lextcal.Lexicon;

import java.util.ArrayList;
import java.util.List;

public class Interpreter {

    private SyntaxTree tree = SyntaxTree.INSTANCE;



    public InterpreterOutput execute(LexicalAnalyzerOutput input) {
        InterpreterOutput output = new InterpreterOutput();

        List<List<Lexicon>> lines = phrasing(input.getLexemeList());
        System.out.println(new Gson().toJson(lines));
        return output;
    }

    private List<List<Lexicon>> phrasing(List<Lexicon> lexemeList) {
        List<List<Lexicon>> lines = new ArrayList<>();
        List<Lexicon> line = new ArrayList<>();

        boolean flg = false;
        for (Lexicon lexicon:lexemeList) {
            if(CompileConfig.LINEFEED_MAP.containsKey(lexicon.getId()) && !flg) {
                if(!line.isEmpty()) {
                    lines.add(line);
                }
                line = new ArrayList<>();
                line.add(lexicon);
            } else {
                line.add(lexicon);
                if(Function.FUNCTIONS_BY_KEY.get("{").getId().equals(lexicon.getId())) {
                    flg = true;
                } else if(Function.FUNCTIONS_BY_KEY.get("}").getId().equals(lexicon.getId())) {
                    flg = false;
                }
            }
        }
        if(!line.isEmpty()) {
            lines.add(line);
        }
        return lines;
    }
}
