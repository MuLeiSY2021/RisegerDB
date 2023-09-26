package org.riseger.main.compiler.syntax;


import org.riseger.main.compiler.token.Token;

import java.util.List;

public class Parser {
    private final SyntaxTree syntaxTree;

    public Parser(SyntaxTree syntaxTree) {
        this.syntaxTree = syntaxTree;
    }

    public SyntaxStructureTree invoke(List<Token> tokenList) {

    }
}
