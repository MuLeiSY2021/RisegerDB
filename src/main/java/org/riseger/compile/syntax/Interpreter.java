package org.riseger.compile.syntax;

import org.riseger.compile.context.Context;
import org.riseger.compile.tokenize.Token;
import org.riseger.compile.tree.KeywordsTree;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Interpreter {
    private static Map<String, Object> splitLineMap = new HashMap<>();

    static {
        List<String> tgr = new LinkedList<>();
        tgr.add("USE");
        tgr.add("SELECT");
        tgr.add("FROM");
        tgr.add("ON");
        tgr.add("WHERE");
        tgr.add("OTHER");
        for (String name : tgr) {
            splitLineMap.put(KeywordsTree.INSTANCE.getCode(name), new Object());
        }
    }

    private final Context context;

    public Interpreter(Context context) {
        this.context = context;
    }

    public void execute() {
        spiltLine();
        for (List<Token> line : context.getLines()) {
            FunctionTree.Session session = FunctionTree.INSTANCE.session(line);
            session.get();
        }
    }

    private void spiltLine() {
        List<Token> tmp = new LinkedList<>();
        for (Token token : context.getTokens()) {
            if (!splitLineMap.containsKey(token.getCode())) {
                tmp.add(token);
            } else {
                if (!tmp.isEmpty()) {
                    context.addLine(tmp);
                }
                tmp = new LinkedList<>();
                tmp.add(token);
            }
        }
        if (!tmp.isEmpty()) {
            context.addLine(tmp);
        }
    }

}
