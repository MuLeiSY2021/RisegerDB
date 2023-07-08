package org.risegerdb.compile.syntax;

import com.google.gson.Gson;
import org.risegerdb.compile.config.CompileConfig;
import org.risegerdb.compile.lextcal.Function;
import org.risegerdb.compile.lextcal.LexicalAnalyzerOutput;
import org.risegerdb.compile.lextcal.Lexicon;

import java.util.*;

public class Interpreter {

    private SyntaxTree tree = SyntaxTree.INSTANCE;

    private static final Map<String,Boolean> PREPROCESS = new HashMap<String,Boolean>();
    static {
        PREPROCESS.put("SELECT",true);
        PREPROCESS.put("FROM",true);
        PREPROCESS.put("OTHER",true);
    }

    public InterpreterOutput execute(LexicalAnalyzerOutput input) {
        InterpreterOutput output = new InterpreterOutput();

        List<List<Lexicon>> lines = phrasing(input.getLexemeList());
        compiling(output,lines);
        return output;
    }

    private Lexicon compiling(InterpreterOutput output,List<List<Lexicon>> lines) {

        ListIterator<List<Lexicon>> it = lines.listIterator();
        for (List<Lexicon> line; it.hasNext();) {
            line = it.next();
            if (PREPROCESS.containsKey(line.get(0).getValue())) {
                it.remove();
                List<Lexicon> tmpList = new LinkedList<>();
                Lexicon key = line.get(0);
                for (Lexicon lexicon : line) {
                    if (lexicon.getValue() != null && lexicon.getValue().equals(",")) {
                        it.add(tmpList);
                        tmpList = new LinkedList<>();
                        tmpList.add(key);
                    } else {
                        tmpList.add(lexicon);
                    }
                }
                it.add(tmpList);
            }
        }

        for (List<Lexicon> line:lines) {
            PriorityTree tree = new PriorityTree(line);
            System.out.println(new Gson().toJson(tree));
            Session session = tree.getSession();
            List<Lexicon> singleCommand = session.getLine();
        }
        return null;
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
