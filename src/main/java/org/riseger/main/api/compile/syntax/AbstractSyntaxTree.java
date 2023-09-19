package org.riseger.main.api.compile.syntax;

import java.util.HashMap;
import java.util.Map;

public class AbstractSyntaxTree {
    private final Map<String, SyntaxTree> forest = new HashMap<>();

    private static class SyntaxTree {
        private String entry;

        private Node root;

        public SyntaxTree(SyntaxRule.Rule rule) {

        }

        private static class Node {

        }
    }
}
