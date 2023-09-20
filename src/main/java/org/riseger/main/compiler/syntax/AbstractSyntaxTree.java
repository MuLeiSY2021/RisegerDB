package org.riseger.main.compiler.syntax;

import lombok.Data;
import org.riseger.main.compiler.keyword.Keyword;

import java.util.*;

public class AbstractSyntaxTree {
    private final Map<String, SyntaxTree> forest = new HashMap<>();

    public AbstractSyntaxTree(SyntaxRule rule) {
        for (Map.Entry<String, SyntaxRule.Rule> entry : rule.getRuleMap().entrySet()) {
            forest.put(entry.getKey(), new SyntaxTree(entry.getValue()));
        }
    }

    private static class SyntaxTree {
        private final String entry;

        private final Node root = new Node(null, null);

        public SyntaxTree(SyntaxRule.Rule rule) {
            this.entry = rule.getType();
            for (SyntaxRule.Meta meta : rule.getMeta()) {
                root.initialize(meta.getTiles().listIterator());
            }
        }

        @Data
        private static class Node {
            private final Node parent;
            private final boolean isKeyword;
            private final Keyword keyword;
            private final int typeHashcode;
            private List<Node> children;

            public Node(Node parent, SyntaxRule.Type type) {
                this.children = new LinkedList<>();
                this.parent = parent;
                if (type == null) {
                    this.isKeyword = false;
                    this.typeHashcode = -1;
                    this.keyword = null;
                } else {
                    this.isKeyword = type.isKey();
                    if (this.isKeyword) {
                        this.keyword = Keyword.addKeyword(type.getValue());
                        this.typeHashcode = -1;
                    } else {
                        this.typeHashcode = type.hashCode();
                        this.keyword = null;
                    }
                }
            }

            public void initialize(ListIterator<SyntaxRule.Type> types) {
                SyntaxRule.Type type = types.next();
                for (Node node : children) {
                    if (node.equals(type)) {
                        node.initialize(types);
                    }
                }
                Node tmp = new Node(this, type);
                this.children.add(tmp);
                tmp.initialize(types);
            }

            public boolean equals(SyntaxRule.Type type) {
                if (this.isKeyword) {
                    return type.getValue().equals(keyword.getCode());
                } else {
                    return this.typeHashcode == type.hashCode();
                }
            }
        }
    }
}
