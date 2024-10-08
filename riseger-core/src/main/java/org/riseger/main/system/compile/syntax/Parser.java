package org.riseger.main.system.compile.syntax;


import org.apache.log4j.Logger;
import org.riseger.main.system.compile.semantic.SemanticTree;
import org.riseger.main.system.compile.token.Token;

import java.util.ArrayList;

public class Parser {
    private static final Logger LOG = Logger.getLogger(Parser.class);
    private final SyntaxForest syntaxForest;

    public Parser(SyntaxForest syntaxForest) {
        this.syntaxForest = syntaxForest;
    }

    public SemanticTree invoke(ArrayList<Token> tokenList) throws Exception {
        return new SemanticTree(tokenList, syntaxForest);
    }
}
