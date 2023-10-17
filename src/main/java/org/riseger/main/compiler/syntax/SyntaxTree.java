package org.riseger.main.compiler.syntax;

import lombok.Data;
import lombok.Getter;
import org.apache.commons.collections4.IteratorUtils;
import org.riseger.main.compiler.lextcal.Keyword;
import org.riseger.main.compiler.token.Token;
import org.riseger.protoctl.exception.CompileException;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.serializer.JsonSerializer;

import java.util.*;

public class SyntaxTree {
    private final Map<Integer, SyntaxTreeChild> forest = new HashMap<>();

    private final Map<Integer, String> finalTypeIdTable = new HashMap<>();

    private final int entry;

    public SyntaxTree(SyntaxRule rule) {
        this.entry = rule.getRuleMap().get("sql").getTypeId();

        for (Map.Entry<String, SyntaxRule.Rule> entry : rule.getRuleMap().entrySet()) {
            if (entry.getValue().isEnd()) {
                finalTypeIdTable.put(entry.getValue().getTypeId(), entry.getKey());
            } else {
                forest.put(entry.getValue().getTypeId(), new SyntaxTreeChild(entry.getValue(), rule));
            }
        }
    }

    public SyntaxStructureTree convert(List<Token> tokenList) {
        SyntaxStructureTree tree = new SyntaxStructureTree();
        parse(tokenList.listIterator(), tree.layerIterator());
        return tree;
    }

    private void parse(ListIterator<Token> tokenIterator, LayerIterator layerIterator) {
        this.parse(tokenIterator, entry, layerIterator);
    }

    private void parse(ListIterator<Token> tokenIterator, int typeCode, LayerIterator layerIterator) {
        if (this.finalTypeIdTable.containsKey(typeCode)) {
            //TODO:不确定边界
            layerIterator.add(tokenIterator.next(), false, null, typeCode);
            return;
        }
        SyntaxTreeChild child = this.forest.get(typeCode);
        child.parse(tokenIterator, layerIterator);
        //TODO:
    }

    @Getter
    private class SyntaxTreeChild {

        private final int typeCode;

        private final FUNCTION function;

        private final Node root = new Node(null, null, null);

        public SyntaxTreeChild(SyntaxRule.Rule rule, SyntaxRule syntaxRule) {
            this.function = rule.getFunction();
            this.typeCode = rule.getTypeId();
            for (SyntaxRule.Meta meta : rule.getMeta()) {
                root.initialize(meta.getTiles().listIterator(), syntaxRule);
            }
        }

        public void parse(ListIterator<Token> tokenIterator, LayerIterator layerIterator) {
            if (!this.root.parse(tokenIterator, layerIterator)) {
                tokenIterator.previous();
                throw new CompileException("错误位于:" + JsonSerializer.serializeToString(tokenIterator.next()));
            }
        }

        @Data
        private class Node {
            private final Node parent;
            private final boolean isKeyword;
            private final Keyword keyword;
            private final int typeCode;
            private List<Node> children;

            public Node(Node parent, SyntaxRule.Type type, SyntaxRule syntaxRule) {
                this.children = new LinkedList<>();
                this.parent = parent;
                if (type == null) {
                    this.isKeyword = false;
                    this.typeCode = -2;
                    this.keyword = null;
                } else {
                    this.isKeyword = type.isKey();
                    if (this.isKeyword) {
                        this.keyword = Keyword.addKeyword(type.getValue());
                        this.typeCode = -1;
                    } else {
                        this.typeCode = syntaxRule.getRuleMap().get(type.getValue()).getTypeId();
                        this.keyword = null;
                    }
                }
            }

            public void initialize(ListIterator<SyntaxRule.Type> types, SyntaxRule syntaxRule) {
                if (!types.hasNext()) {
                    return;
                }
                SyntaxRule.Type type = types.next();
                for (Node node : children) {
                    if (node.equals(type)) {
                        node.initialize(types, syntaxRule);
                    }
                }
                Node tmp = new Node(this, type, syntaxRule);
                this.children.add(tmp);
                tmp.initialize(types, syntaxRule);
            }

            public boolean equals(SyntaxRule.Type type) {
                if (this.isKeyword) {
                    return type.getValue().equals(keyword.getCode());
                } else {
                    return this.typeCode == type.hashCode();
                }
            }

            public boolean parse(ListIterator<Token> tokenIterator, LayerIterator layerIterator) {
                if (typeCode != -2) {
                    if (!this.suit(tokenIterator, layerIterator)) {
                        return false;
                    } else if (this.children.isEmpty()) {
                        return true;
                    }
                }

                List<Token> save = IteratorUtils.toList(tokenIterator);
                ListIterator<Token> iterator = save.listIterator();
                for (Node c : this.children) {
                    if (!c.suit(iterator, layerIterator)) {
                        iterator = save.listIterator();
                    } else {
                        return true;
                    }
                }
                return false;

            }

            public boolean suit(ListIterator<Token> tokenIterator, LayerIterator layerIterator) {
                if (!tokenIterator.hasNext()) {
                    return false;
                }
                Token token = tokenIterator.next();

                if (this.isKeyword) {
                    if (this.keyword.getId() == token.getId()) {
                        layerIterator.add(token, true, this.keyword, this.typeCode);
                    } else {
                        return false;
                    }
                    if (this.children.isEmpty()) {
                        return true;
                    } else {
                        for (Node node : this.children) {
                            if (node.parse(tokenIterator, layerIterator)) {
                                return true;
                            }
                        }
                        return false;
                    }
                } else {
                    List<Token> tokens = new LinkedList<>();
                    Node target;
                    tokens.add(token);
                    if (!this.children.isEmpty()) {
                        for (ListIterator<Token> it = tokenIterator; it.hasNext(); ) {
                            Token tmp = it.next();

                            if ((target = hasSuit(tmp)) == null) {
                                tokens.add(tmp);
                            } else {
                                if (target.suit(tokenIterator, layerIterator)) {
                                    layerIterator.add(token, false, this.keyword, this.typeCode);
                                    SyntaxTree.this.parse(tokens.listIterator(), this.typeCode, layerIterator.deeper());
                                }
                            }
                        }

                        //无法进行配对，在父的另一分支
                        return false;
                    } else {
                        layerIterator.add(token, false, this.keyword, this.typeCode);
                        tokenIterator.forEachRemaining(tokens::add);
                        SyntaxTree.this.parse(tokens.listIterator(), this.typeCode, layerIterator.deeper());
                        return true;
                    }
                }
            }

            private Node hasSuit(Token token) {
                for (Node child : this.children) {
                    if (!child.isKeyword) {
                        continue;
                    }

                    if (token.equals(child.keyword)) {
                        return child;
                    }
                }

                return null;
            }
        }
    }
}
