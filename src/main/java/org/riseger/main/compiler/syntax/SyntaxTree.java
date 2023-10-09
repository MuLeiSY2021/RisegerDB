package org.riseger.main.compiler.syntax;

import lombok.Data;
import org.riseger.main.compiler.lextcal.Keyword;
import org.riseger.main.compiler.token.Token;
import org.riseger.protoctl.exception.CompileException;
import org.riseger.protoctl.serializer.JsonSerializer;

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

    public SyntaxStructureTree convert(List<Token> tokenList) {
        SyntaxStructureTree tree = new SyntaxStructureTree();
        parse(tokenList.listIterator(), tree.layerIterator());
        return tree;
    }

    private void parse(ListIterator<Token> tokenIterator, LayerIterator layerIterator) {
        this.parse(tokenIterator, 0, layerIterator);
    }

    private void parse(ListIterator<Token> tokenIterator, int typeCode, LayerIterator layerIterator) {
        SyntaxTreeChild child = this.forest.get(typeCode);
        child.parse(tokenIterator, layerIterator);
        //TODO:
    }


    private class SyntaxTreeChild {
        private final String entry;

        private final Node root = new Node(null, null);

        public SyntaxTreeChild(SyntaxRule.Rule rule) {
            this.entry = rule.getType();
            for (SyntaxRule.Meta meta : rule.getMeta()) {
                root.initialize(meta.getTiles().listIterator());
            }
        }

        public void parse(ListIterator<Token> tokenIterator, LayerIterator layerIterator) {
            for (Node c : root.children) {
                if (!c.suit(tokenIterator, layerIterator)) {
                    tokenIterator.previous();
                    throw new CompileException("语法错误，在：" + Arrays.toString(JsonSerializer.serialize(tokenIterator.next())));
                }
            }
        }

        @Data
        private class Node {
            private final Node parent;
            private final boolean isKeyword;
            private final Keyword keyword;
            private final int typeCode;
            private List<Node> children;

            public Node(Node parent, SyntaxRule.Type type) {
                this.children = new LinkedList<>();
                this.parent = parent;
                if (type == null) {
                    this.isKeyword = false;
                    this.typeCode = -1;
                    this.keyword = null;
                } else {
                    this.isKeyword = type.isKey();
                    if (this.isKeyword) {
                        this.keyword = Keyword.addKeyword(type.getValue());
                        this.typeCode = -1;
                    } else {
                        this.typeCode = type.hashCode();
                        this.keyword = null;
                    }
                }
            }

            public void initialize(ListIterator<SyntaxRule.Type> types) {
                if (!types.hasNext()) {
                    return;
                }
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
                    return this.typeCode == type.hashCode();
                }
            }

            public boolean suit(ListIterator<Token> tokenIterator, LayerIterator layerIterator) {
                Token token = tokenIterator.next();

                if (this.isKeyword) {
                    if (this.keyword.getId() == token.getId()) {
                        layerIterator.add(token, true, this.keyword, this.typeCode);
                    } else {
                        return false;
                    }
                } else {
                    SyntaxTree.this.parse(tokenIterator, this.typeCode, layerIterator.deeper());
                }
                for (Node child : this.children) {
                    if (!child.suit(tokenIterator, layerIterator)) {
                        return true;
                    }
                }
                return true;
            }
        }
    }
}
