package org.riseger.main.compiler.semantic;

import lombok.Data;
import org.apache.log4j.Logger;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.compoent.SearchSession;
import org.riseger.main.compiler.function.type.Function_c;
import org.riseger.main.compiler.syntax.Syntax;
import org.riseger.main.compiler.syntax.SyntaxForest;
import org.riseger.main.compiler.token.Token;
import org.riseger.main.compiler.token.TokenType;
import org.riseger.protoctl.compiler.CommandTree;
import org.riseger.protoctl.compiler.function.Entity_f;
import org.riseger.protoctl.compiler.function.Function_f;
import org.riseger.protoctl.compiler.function.ProcessorFunction;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SemanticTree {
    private final Logger LOG = Logger.getLogger(SemanticTree.class);
    private final Node root = new Node(null);

    public SemanticTree(CommandTree commandTree) {
        this.root.copy(commandTree.getRoot());
    }

    public SemanticTree(List<Token> tokenList, SyntaxForest forest, SearchSession session) {
        suit(root, forest.getEntry(), tokenList, forest, session);
    }

    public void getFunctionList(SearchMemory searchMemory, CommandList commandList) {
        Queue<Function_c> queue = new ConcurrentLinkedQueue<>();
        commandList.setFunctionList(queue);
        sort();
        this.root.toQueue(queue, searchMemory, commandList);
    }

    private void sort() {
        this.root.sort(0);
    }

    private Node suit(Node node, int code, List<Token> tokenList, SyntaxForest forest, SearchSession session) {
        Node tmp = new Node(node);
        if (forest.isEnd(code)) {
            Token token = tokenList.get(0);
            if (token.getType() == TokenType.KEYWORD) {
                LOG.error("奇怪的Bug发生了，symbol" + token.getSourceCode() + "被错误判断");
            }
//            try {
            LOG.debug("匹配结束节点匹配成功，数值为：" + token.getSourceCode() + " 函数：" + forest.getEndFunctionClass());
//                Constructor<Function_f> constructor = forest.getEndFunctionClass().getConstructor(Object.class);
//                tmp.setFunction(constructor.newInstance(session.get(token.getId())));
            tmp.setFunction(new Entity_f(session.get(token.getId())));
                return tmp;
//            } catch (NoSuchMethodException e) {
//                LOG.error("No such constructor of Object.class", e);
//            } catch (InvocationTargetException | InstantiationException e) {
//                LOG.error("Class might be abstract", e);
//            } catch (IllegalAccessException e) {
//                LOG.error("The Constructor can not access", e);
//            }
//            return null;
        }

        for (List<Syntax> syntaxList : forest.getSyntaxNode(code).getMetaList()) {
            LOG.debug("匹配一条分支：" + syntaxList.stream().map(Syntax::toString));
            List<Object[]> res = suitOneBranch(tmp, tokenList.listIterator(), syntaxList.listIterator(), session);
            if (res != null) {
                for (Object[] objects : res) {
                    Node child = suit(tmp, (Integer) objects[0], ((List<Token>) objects[1]), forest, session);
                    tmp.add(child);
                }
                break;
            }
        }
        return tmp;
    }

    private List<Object[]> suitOneBranch(Node node, ListIterator<Token> tokenIterator, ListIterator<Syntax> treeIterator, SearchSession session) {
        List<Object[]> res = new LinkedList<>();
        Token token_tmp;
        Syntax tmp;

        do {
            tmp = treeIterator.next();
            token_tmp = tokenIterator.next();
            if (tmp.isKeyword()) {
                if (!tmp.equals(token_tmp)) {
                    LOG.debug("匹配失败,符号：" + tmp.getSymbol() + ":" + tmp.getHashCode() + " 不匹配源代码：" + token_tmp.getSourceCode() + ":" + token_tmp.getId());
                    return null;
                }
                LOG.debug("匹配成功，源代码为：" + token_tmp.getSourceCode() + " 匹配代码为" + tmp.getSymbol());
            } else {
                LOG.debug("非关键词,类型名为：" + tmp.getSymbol());
                List<Token> remain = new LinkedList<>();
                Object[] _res = new Object[2];
                _res[0] = tmp.getHashCode();
                _res[1] = remain;
                res.add(_res);
                Syntax tmp_next = null;
                if (treeIterator.hasNext()) {
                    tmp_next = treeIterator.next();
                }
                tokenIterator.previous();
                do {
                    token_tmp = tokenIterator.next();
                    LOG.debug("把 " + token_tmp.getSourceCode() + " 填充进去了");
                    remain.add(token_tmp);
                } while (tmp_next == null && tokenIterator.hasNext() ||
                        tmp_next != null && !tmp_next.equals(token_tmp) && tokenIterator.hasNext());
                if (tmp_next != null) {
                    treeIterator.previous();
                    tokenIterator.previous();
                }
            }
        } while (treeIterator.hasNext());
        try {
            node.setFunction(tmp.getFunctionFClass() == null ?
                    null :
                    tmp.getFunctionFClass().newInstance());

            LOG.debug("单支匹配成功，填充函数：" + tmp.getFunctionFClass());

        } catch (InstantiationException | IllegalAccessException e) {
            LOG.error("Function can not empty instance", e);
        }
        return res;
    }

    @Data
    public static class Node {
        private Node parent;

        private List<Node> children = new LinkedList<>();

        private boolean canSort = false;

        private Function_f function;

        private int level;

        public Node(Node parent) {
            this.parent = parent;
            if (parent != null) {
                parent.children.add(this);
            }
        }

        public void add(Node child) {
            this.children.add(child);
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

        void toQueue(Queue<Function_c> queue, SearchMemory searchMemory, CommandList commandList) {
            Function_c function = null;
            if (this.function != null) {
                function = Function_c.getFunctionFromMap(this.function, searchMemory, commandList);
                if (function instanceof ProcessorFunction) {
                    ProcessorFunction processorFunction = (ProcessorFunction) function;
                    processorFunction.preHandle(this, queue.size());
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
                Node tmp = new Node(this);
                tmp.copy(child);
                this.add(tmp);
            }
        }

        public void addHead(Function_f function) {
            this.parent.children.remove(this);
            Node tmp = new Node(this.parent);
            tmp.setFunction(function);
            this.parent.children.add(tmp);
            this.parent = tmp;
            tmp.children.add(this);
        }

        public void addChild(Function_f function, int index) {
            Node tmp = new Node(this);
            tmp.setFunction(function);
            Node child = this.children.get(index);
            child.setParent(tmp);
            tmp.add(child);
            this.children.remove(index);
            this.children.add(index, tmp);
        }
    }


}
