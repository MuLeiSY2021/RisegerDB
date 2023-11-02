package org.riseger.main.compiler.semantic;

import lombok.Data;
import org.apache.log4j.Level;
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
    private final Node root = new Node();

    private final Map<Integer, Node> cache = new HashMap<>();

    public SemanticTree(CommandTree commandTree) {
        this.root.copy(commandTree.getRoot());
    }

    public SemanticTree(List<Token> tokenList, SyntaxForest forest, SearchSession session) {
        root.add(suit(forest.getEntry(), tokenList, forest, session));
    }

    public void getFunctionList(SearchMemory searchMemory, CommandList commandList) {
        Queue<Function_c> queue = new ConcurrentLinkedQueue<>();
        sort();
        this.root.toQueue(queue, searchMemory, commandList);
        commandList.setFunctionList(queue);
    }

    private void sort() {
        this.root.sort(0);
    }

    private Node suit(int code, List<Token> tokenList, SyntaxForest forest, SearchSession session) {
        if (forest.isEnd(code)) {
            if (tokenList.size() > 1) {
                return null;
            }
            Node res = new Node();
            Token token = tokenList.get(0);
            if (token.getType().equals(TokenType.KEYWORD)) {
                return null;
            }
            if (token.getType() == TokenType.KEYWORD) {
                LOG.error("奇怪的Bug发生了，symbol" + token.getSourceCode() + "被错误判断");
            }
            LOG.debug("匹配结束节点匹配成功，数值为：" + token.getSourceCode() + " 函数：" + forest.getEndFunctionClass());

            res.setFunction(new Entity_f(token.getEntity()));
            LOG.debug("完整函数出现，储存为缓存");
            put(tokenList, code, res);
            return res;
        }
        for (List<Syntax> syntaxList : forest.getSyntaxNode(code).getMetaList()) {
            if (LOG.isEnabledFor(Level.DEBUG)) {
                StringBuilder sb = new StringBuilder();
                for (Syntax syntax : syntaxList) {
                    sb.append(syntax.getSymbol()).append(" ");
                }
                LOG.debug("匹配一条分支：" + sb);
            }

            Node tmp = suitOneBranch(tokenList, code, syntaxList.listIterator(), forest, session);
//            put(tokenList,tmp);
            if (tmp != null) {
                return tmp;
            }
        }
        return null;
    }

    public void put(List<Token> tokenList, int code, Node element) {
        this.cache.put(Objects.hash(tokenList.hashCode(), code), element);
    }

    public Node get(List<Token> tokenList) {
        return this.cache.get(tokenList.hashCode());
    }

    public boolean contain(List<Token> tokenList) {
        return this.cache.containsKey(tokenList.hashCode());
    }

    private Node suitOneBranch(List<Token> tokenList, int code, ListIterator<Syntax> treeIterator, SyntaxForest forest, SearchSession session) {
        ListIterator<Token> tokenIterator = tokenList.listIterator();
        //TODO：修改语法树以适配新的语法规则（修改迭代）
        if (contain(tokenList)) {
            if (LOG.isEnabledFor(Level.DEBUG)) {
                StringBuilder sb = new StringBuilder();
                for (Token token : tokenList) {
                    sb.append(token.getSourceCode()).append(" ");
                }
                LOG.debug("命中索引！：" + sb);
            }
            return get(tokenList);
        }
        Token token_tmp;
        Syntax syntax_tmp;
        boolean res_bol = true;
        Node res = new Node();
        do {
            if (!tokenIterator.hasNext()) {
                LOG.debug("单支匹配失败，token无后续了");
                return null;
            }
            syntax_tmp = treeIterator.next();
            token_tmp = tokenIterator.next();
            if (syntax_tmp.isKeyword()) {
                if (!syntax_tmp.equals(token_tmp)) {
                    LOG.debug("匹配失败,源代码：\"" + token_tmp.getSourceCode() + "\":" + token_tmp.getId() + " 不匹配语法：\"" + syntax_tmp.getSymbol() + "\":" + syntax_tmp.getId());
                    return null;
                }
                LOG.debug("匹配成功，源代码为：\"" + token_tmp.getSourceCode() + "\" 匹配代码为:\"" + syntax_tmp.getSymbol() + "\"");
            } else {
                LOG.debug("非关键词,类型名为：" + syntax_tmp.getSymbol());
                List<Token> remain = new LinkedList<>();
                Syntax syntax_next = null;
                if (treeIterator.hasNext()) {
                    syntax_next = treeIterator.next();
                }
                tokenIterator.previous();//前文获取过了第一个token，所以在这里必须先归位
                Node tmp_ = null;
                while (tokenIterator.hasNext()) {
                    token_tmp = tokenIterator.next();
                    if (syntax_next != null && token_tmp.isKeyword() && token_tmp.getId() == syntax_next.getId()) {
                        LOG.debug("分支片段语法查询");
                        tmp_ = suit(syntax_tmp.getId(), remain, forest, session);
                        if (tmp_ != null) {
                            LOG.debug("符合语法规则");
                            break;
                        }
                        LOG.debug("不符合，进入下一个分支终结符");
                    }
                    LOG.debug("把 " + token_tmp.getSourceCode() + " 填充进去了");
                    remain.add(token_tmp);
                }
                if (syntax_next == null || tmp_ == null) {
                    LOG.debug("分支片段语法查询");
                    tmp_ = suit(syntax_tmp.getId(), remain, forest, session);
                    if (LOG.isEnabledFor(Level.DEBUG)) {
                        if (tmp_ != null) {
                            LOG.debug("符合语法规则");
                        }
                        LOG.debug("不符合，进入下一个分支终结符");
                    }
                }


                res_bol = tmp_ != null;
                if (tmp_ != null) {
                    res.add(tmp_);
                }
                if (syntax_next != null) {
                    treeIterator.previous();
                    tokenIterator.previous();
                }

            }
        } while (treeIterator.hasNext() & res_bol);
        if (!res_bol) {
            LOG.debug("单支匹配失败,有迭代节点条件不匹配");
            return null;
        }
        try {
            res.setFunction(syntax_tmp.getFunctionFClass() == null ?
                    null :
                    syntax_tmp.getFunctionFClass().newInstance());

            LOG.debug("单支匹配成功，填充函数：" + syntax_tmp.getFunctionFClass());

        } catch (InstantiationException | IllegalAccessException e) {
            LOG.error("Function can not empty instance", e);
        }
        LOG.debug("完整函数出现，储存为缓存");
        put(tokenList, code, res);
        return res;
    }

    @Data
    public static class Node {
        private Node parent;

        private List<Node> children = new LinkedList<>();

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

        void toQueue(Queue<Function_c> queue, SearchMemory searchMemory, CommandList commandList) {
            Function_c function = null;
            if (this.function != null) {
                function = Function_c.getFunctionFromMap(this.function, searchMemory, commandList);

                if (function instanceof ProcessorFunction) {
                    List<Function_f> functionList = new LinkedList<>();
                    ProcessorFunction processorFunction = (ProcessorFunction) function;
                    processorFunction.preHandle(this, queue.size(), functionList);
                    for (Function_f f : functionList) {
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
            this.parent.children.add(tmp);
            this.parent = tmp;
            tmp.children.add(this);
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
