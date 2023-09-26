package org.riseger.main.compiler.syntax;

import lombok.Data;
import org.riseger.main.compiler.lextcal.Keyword;
import org.riseger.main.compiler.token.Token;
import org.riseger.protoctl.search.function.FUNCTION;

import java.util.*;

public class SyntaxTree {
    private final Map<Integer, SyntaxTreeChild> forest = new HashMap<>();

    private final Map<String, Integer> typeIdTable = new HashMap<>();

    private final Map<Integer, String> finalTypeIdTable = new HashMap<>();

    public SyntaxTree(SyntaxRule rule) {
        for (Map.Entry<String, SyntaxRule.Rule> entry : rule.getRuleMap().entrySet()) {
            int id;

            if (typeIdTable.containsKey(entry.getKey())) {
                id = typeIdTable.get(entry.getKey());
            } else {
                id = typeIdTable.size();
                typeIdTable.put(entry.getKey(), id);
            }
            if (entry.getValue().isEnd()) {
                finalTypeIdTable.put(id, entry.getKey());
            } else {
                forest.put(id, new SyntaxTreeChild(entry.getValue()));
            }
        }
    }

    public static FUNCTION parser(List<Token> tokenList) {

    }


    private static class SyntaxTreeChild {
        private final String entry;

        private final Node root = new Node(null, null);

        public SyntaxTreeChild(SyntaxRule.Rule rule) {
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
