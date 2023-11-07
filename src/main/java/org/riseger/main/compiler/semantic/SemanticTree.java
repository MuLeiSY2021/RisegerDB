package org.riseger.main.compiler.semantic;

import lombok.Data;
import org.apache.log4j.Logger;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.type.Function_c;
import org.riseger.main.compiler.syntax.Syntax;
import org.riseger.main.compiler.syntax.SyntaxForest;
import org.riseger.main.compiler.token.Token;
import org.riseger.protoctl.compiler.CommandTree;
import org.riseger.protoctl.compiler.function.Entity_f;
import org.riseger.protoctl.compiler.function.Function_f;
import org.riseger.protoctl.compiler.function.ProcessorFunction;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class SemanticTree {
    private final Logger LOG = Logger.getLogger(SemanticTree.class);
    private final Node root = new Node();

    public SemanticTree(CommandTree commandTree) {
        this.root.copy(commandTree.getRoot());
    }

    public SemanticTree(ArrayList<Token> tokenList, SyntaxForest forest) throws Exception {
        root.add(suitTree(forest.getEntry(), new TokenIterator(tokenList), forest));
    }

    public void getFunctionList(SearchMemory searchMemory, CommandList commandList) {
        sort();
        Integer[] integers = new Integer[1];
        integers[0] = 0;
        this.root.stretchTree(integers, searchMemory, commandList);

        Queue<Function_c> queue = new ConcurrentLinkedQueue<>();
        this.root.toQueue(queue, searchMemory, commandList);
        commandList.setFunctionList(queue);
    }

    private void sort() {
        this.root.sort(0);
    }

    private Node suitTree(int treeCode, TokenIterator iterator, SyntaxForest forest) throws InstantiationException, IllegalAccessException {
        if (forest.isEnd(treeCode)) {
            Token token = iterator.next();
            LOG.debug("语法终结点：" + token.getSourceCode());
            if (token.getType().equals(forest.getEndType(treeCode))) {
                Node node = new Node();
                node.setFunction(new Entity_f(token.getEntity()));
                LOG.debug("语法终结点 匹配成功");
                return node;
            } else {
                LOG.debug("语法终结点 匹配失败");
                return null;
            }
        }
        TokenIterator __ = iterator.copy();
        for (List<Syntax> syntaxList : forest.getSyntaxNode(treeCode).getMetaList()) {
            Node node = new Node();
            iterator.back(__);
            LOG.debug("进入" + syntaxList.stream().map(Syntax::toString).collect(Collectors.toList()) + "中匹配");
            Syntax syntax_end = null;
            for (Syntax syntax : syntaxList) {
                syntax_end = syntax;
                if (syntax.isKeyword()) {
                    Token token = iterator.next();
                    if (!syntax.equals(token)) {
                        iterator.previous();
                        LOG.debug("匹配失败,源代码：\"" + token.getSourceCode() + "\":" + token.getId() + " 不匹配语法：\"" + syntax.getSymbol() + "\":" + syntax.getId());
                        syntax_end = null;
                        break;
                    }
                    LOG.debug("匹配成功，源代码为：\"" + token.getSourceCode() + "\" 匹配代码为:\"" + syntax.getSymbol() + "\"");
                } else {
                    LOG.debug("非关键词匹配");
                    Node tmp = suitTree(syntax.getId(), iterator, forest);
                    if (tmp != null) {
                        LOG.debug("匹配成功，非关键词 '" + syntax.getSymbol() + "' 匹配");
                        node.add(tmp);
                    } else {
                        LOG.debug("非关键词" + syntax.getSymbol() + "匹配失败");
                        syntax_end = null;
                        break;
                    }
                }
            }
            if (syntax_end != null) {
                LOG.debug("单支" + syntaxList.stream().map(Syntax::toString).collect(Collectors.toList()) + "匹配成功，填充函数：" + syntax_end.getFunctionFClass());
                try {
                    if (syntax_end.getFunctionFClass() != null) {
                        node.setFunction(syntax_end.getFunctionFClass().newInstance());
                    }
                    return node;
                } catch (InstantiationException | IllegalAccessException e) {
                    LOG.error("Function can not empty instance", e);
                    throw e;
                }
            }
            if (!iterator.hasNext()) {
                return null;
            }
        }
        LOG.debug("匹配失败，" + forest.getSyntaxNode(treeCode).getMetaList().get(0).stream().map(Syntax::toString).collect(Collectors.toList()) + " 树内所有分支无匹配");
        return null;
    }

    @Data
    public static class Node {
        private Node parent;

        private ArrayList<Node> children = new ArrayList<>(4);

        private boolean canSort = false;

        private Function_f function;

        private int level;

        public Node() {
        }

        public void add(Node child) {
            this.children.add(child);
            child.setParent(this);
        }

        void setFunction(Function_f function) {
            this.function = function;
            if (this.function != null) {
                this.canSort = function.canSort();
            }
        }

        Integer getPriority() {
            if (function == null) {
                return -1;
            } else {
                return function.getWeight();
            }
        }

        int sort(int level) {
            if (children.isEmpty()) {
                this.level = ++level;
                return this.level;
            }
            if (canSort) {
                children.sort(Comparator.comparing(Node::getPriority));
            }

            int total = 0;
            for (Node c : children) {
                total += c.sort(total);
            }
            this.level = total;
            return this.level;
        }

        void stretchTree(Integer[] len, SearchMemory searchMemory, CommandList commandList) {
            Function_c function;
            if (this.function != null) {
                function = Function_c.getFunctionFromMap(this.function, searchMemory, commandList);

                if (function instanceof ProcessorFunction) {
                    List<Function_f> functionList = new LinkedList<>();
                    ProcessorFunction processorFunction = (ProcessorFunction) function;
                    processorFunction.stretch(this, this.level, functionList);
                    len[0] += functionList.size();
                }
            }
            for (Node child : children) {
                child.stretchTree(len, searchMemory, commandList);
            }
            if (this.function != null) {
                len[0]++;
            }
        }

        void toQueue(Queue<Function_c> queue, SearchMemory searchMemory, CommandList commandList) {
            Function_c function = null;
            if (this.function != null) {
                function = Function_c.getFunctionFromMap(this.function, searchMemory, commandList);
                if (function instanceof ProcessorFunction) {
                    ProcessorFunction processorFunction = (ProcessorFunction) function;
                    for (Function_f f : processorFunction.preprocess()) {
                        queue.add(Function_c.getFunctionFromMap(f, searchMemory, commandList));
                    }
                }
            }
            for (Node child : children) {
                child.toQueue(queue, searchMemory, commandList);
            }
            if (this.function != null) {
                queue.add(function);
            }
        }

        void copy(CommandTree.Node root) {
            this.function = root.getFunction();
            for (CommandTree.Node child : root.getChildren()) {
                Node tmp = new Node();
                tmp.copy(child);
                this.add(tmp);
            }
        }

        public void addHead(Function_f function) {
            this.parent.children.remove(this);
            Node tmp = new Node();
            tmp.setFunction(function);
            this.parent.add(tmp);
            tmp.add(this);
        }

        public void addChild(Function_f function, int index) {
            Node tmp = new Node();
            tmp.setFunction(function);
            Node child = this.children.get(index);
            child.setParent(tmp);
            tmp.add(child);
            this.children.remove(index);
            this.children.add(index, tmp);
        }
    }


}
