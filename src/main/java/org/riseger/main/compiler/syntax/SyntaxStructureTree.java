package org.riseger.main.compiler.syntax;

import org.riseger.main.compiler.token.Token;

import java.util.LinkedList;
import java.util.List;

public class SyntaxStructureTree {
    private static class Node {
        private final Token token;

        private final List<Node> children = new LinkedList<>();

        public Node(Token token) {
            this.token = token;
        }
    }

}
