package org.riseger.main.compiler.syntax;

import lombok.Getter;
import org.riseger.main.compiler.token.TokenType;
import org.riseger.protoctl.compiler.function.Function_f;
import org.riseger.utils.tree.MultiBranchesTree;

import java.util.HashMap;
import java.util.Map;

public class SyntaxForest {
    private final Map<Integer, MultiBranchesTree<Class<Function_f>>> forest = new HashMap<Integer, MultiBranchesTree<Class<Function_f>>>();

    private final Map<Integer, TokenType> finalTypeIdTable = new HashMap<>();
    @Getter
    private final int entry;
    @Getter
    private Class<Function_f> endFunctionClass;

    public SyntaxForest(SyntaxRule rule) {
        this.entry = rule.getRuleMap().get("sqls").getTypeId();

        for (Map.Entry<String, SyntaxRule.Rule> entry : rule.getRuleMap().entrySet()) {
            SyntaxRule.Rule entryRule = entry.getValue();
            if (entryRule.isEnd()) {
                finalTypeIdTable.put(entryRule.getTypeId(), TokenType.fromString(entry.getKey()));
                this.endFunctionClass = rule.getEndFunction();
            } else {
                MultiBranchesTree<Class<Function_f>> mbt = new MultiBranchesTree<>();
                for (SyntaxRule.Meta meta : entryRule.getMeta()) {
                    mbt.insert(new SyntaxTreeElement(meta, rule));
                }
                forest.put(entry.getValue().getTypeId(), mbt);
            }
        }
    }

    public MultiBranchesTree<Class<Function_f>> getSyntaxNode(int code) {
        return forest.get(code);
    }

    public boolean isEnd(int code) {
        return finalTypeIdTable.containsKey(code);
    }

    public boolean isEndType(int code, TokenType type) {
        return finalTypeIdTable.get(code).equals(type);
    }

    public TokenType getEndType(int code) {
        return finalTypeIdTable.get(code);
    }

    public boolean isHas(int code) {
        return forest.containsKey(code) || finalTypeIdTable.containsKey(code);
    }


    //    public SemanticTree convert(List<Token> tokenList) {
//        SemanticTree tree = new SemanticTree();
//        parse(tokenList.listIterator(), tree.layerIterator());
//        return tree;
//    }
//
//    private void parse(ListIterator<Token> tokenIterator, LayerIterator layerIterator) {
//        this.parse(tokenIterator, entry, layerIterator);
//    }
//
//    private void parse(ListIterator<Token> tokenIterator, int typeCode, LayerIterator layerIterator) {
//        if (this.finalTypeIdTable.containsKey(typeCode)) {
//            //TODO:不确定边界
//            layerIterator.add(tokenIterator.next(), false, null, typeCode);
//            return;
//        }
//        MultiBranchesTree<Syntax> child = this.forest.getE(typeCode);
//        child.parse(tokenIterator, layerIterator);
//        //TODO:
//    }
//    @Getter
//    private class SyntaxTreeChild {
//
//        private final Node root = new Node(null, null, null);
//
//        public SyntaxTreeChild(SyntaxRule.Rule rule, SyntaxRule syntaxRule) {
//            for (SyntaxRule.Meta meta : rule.getMeta()) {
//                root.initialize(meta.getTiles().listIterator(), syntaxRule);
//            }
//        }
//
//        public void parse(ListIterator<Token> tokenIterator, LayerIterator layerIterator) {
//            if (!this.root.parse(tokenIterator, layerIterator)) {
//                tokenIterator.previous();
//                throw new CompileException("错误位于:" + JsonSerializer.serializeToString(tokenIterator.next()));
//            }
//        }
//
//        @Data
//        private class Node {
//            private final Node parent;
//            private List<Node> children;
//
//            private final Syntax syntax;
//
//
//            public Node(Node parent, SyntaxRule.Tile type, SyntaxRule syntaxRule) {
//                this.children = new LinkedList<>();
//                this.parent = parent;
//                this.syntax = new Syntax(type,syntaxRule);
//            }
//
//            public void initialize(ListIterator<SyntaxRule.Tile> types, SyntaxRule syntaxRule) {
//                if (!types.hasNext()) {
//                    return;
//                }
//                SyntaxRule.Tile type = types.next();
//                for (Node node : children) {
//                    if (node.equals(type)) {
//                        node.initialize(types, syntaxRule);
//                    }
//                }
//                Node tmp = new Node(this, type, syntaxRule);
//                this.children.add(tmp);
//                tmp.initialize(types, syntaxRule);
//            }
//
//            public boolean equals(SyntaxRule.Tile type) {
//                if (this.isKeyword) {
//                    return type.getValue().equals(keyword.getCode());
//                } else {
//                    return this.typeCode == type.hashCode();
//                }
//            }
//
//            public boolean parse(ListIterator<Token> tokenIterator, LayerIterator layerIterator) {
//                if (typeCode != -2) {
//                    if (!this.suit(tokenIterator, layerIterator)) {
//                        return false;
//                    } else if (this.children.isEmpty()) {
//                        return true;
//                    }
//                }
//
//                List<Token> save = IteratorUtils.toList(tokenIterator);
//                ListIterator<Token> iterator = save.listIterator();
//                for (Node c : this.children) {
//                    if (!c.suit(iterator, layerIterator)) {
//                        iterator = save.listIterator();
//                    } else {
//                        return true;
//                    }
//                }
//                return false;
//
//            }
//
//            public boolean suit(ListIterator<Token> tokenIterator, LayerIterator layerIterator) {
//                if (!tokenIterator.hasNext()) {
//                    return false;
//                }
//                Token token = tokenIterator.next();
//
//                if (this.isKeyword) {
//                    if (this.keyword.getId() == token.getId()) {
//                        layerIterator.add(token, true, this.keyword, this.typeCode);
//                    } else {
//                        return false;
//                    }
//                    if (this.children.isEmpty()) {
//                        return true;
//                    } else {
//                        for (Node node : this.children) {
//                            if (node.parse(tokenIterator, layerIterator)) {
//                                return true;
//                            }
//                        }
//                        return false;
//                    }
//                } else {
//                    List<Token> tokens = new LinkedList<>();
//                    Node target;
//                    tokens.add(token);
//                    if (!this.children.isEmpty()) {
//                        while (tokenIterator.hasNext()) {
//                            Token tmp = tokenIterator.next();
//
//                            if ((target = hasSuit(tmp)) == null) {
//                                tokens.add(tmp);
//                            } else {
//                                if (target.suit(tokenIterator, layerIterator)) {
//                                    layerIterator.add(token, false, this.keyword, this.typeCode);
//                                    SyntaxForest.this.parse(tokens.listIterator(), this.typeCode, layerIterator.deeper());
//                                }
//                            }
//                        }
//
//                        //无法进行配对，在父的另一分支
//                        return false;
//                    } else {
//                        layerIterator.add(token, false, this.keyword, this.typeCode);
//                        tokenIterator.forEachRemaining(tokens::add);
//                        SyntaxForest.this.parse(tokens.listIterator(), this.typeCode, layerIterator.deeper());
//                        return true;
//                    }
//                }
//            }
//
//            private Node hasSuit(Token token) {
//                for (Node child : this.children) {
//                    if (!child.isKeyword) {
//                        continue;
//                    }
//
//                    if (token.equals(child.keyword)) {
//                        return child;
//                    }
//                }
//
//                return null;
//            }
//        }
//    }
}
