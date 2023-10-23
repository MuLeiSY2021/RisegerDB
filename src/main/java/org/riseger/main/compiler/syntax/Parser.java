package org.riseger.main.compiler.syntax;


import org.apache.log4j.Logger;
import org.riseger.main.compiler.semantic.SemanticTree;
import org.riseger.main.compiler.token.Token;
import org.riseger.protoctl.serializer.JsonSerializer;

import java.util.List;

public class Parser {
    private static final Logger LOG = Logger.getLogger(Parser.class);
    private final SyntaxForest syntaxForest;

    public Parser(SyntaxForest syntaxForest) {
        this.syntaxForest = syntaxForest;
    }

    public void invoke(List<Token> tokenList) {
        SemanticTree structureTree = new SemanticTree(tokenList, syntaxForest);
        LOG.debug(JsonSerializer.serializeToString(structureTree));
    }
}
