package org.riseger.main.compiler.syntax;


import org.apache.log4j.Logger;
import org.riseger.main.compiler.token.Token;
import org.riseger.protoctl.serializer.JsonSerializer;

import java.util.List;

public class Parser {
    private final SyntaxTree syntaxTree;

    private static final Logger LOG = Logger.getLogger(Parser.class);

    public Parser(SyntaxTree syntaxTree) {
        this.syntaxTree = syntaxTree;
    }

    public void invoke(List<Token> tokenList) {
        SyntaxStructureTree structureTree = syntaxTree.convert(tokenList);
        LOG.debug(JsonSerializer.serializeToString(structureTree));
    }
}
