package org.riseger.main.compiler.semantic;

import lombok.Data;
import org.apache.log4j.Logger;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.type.Function_c;
import org.riseger.main.compiler.syntax.SyntaxForest;
import org.riseger.main.compiler.token.Token;
import org.riseger.protoctl.compiler.CommandTree;
import org.riseger.protoctl.compiler.function.Entity_f;
import org.riseger.protoctl.compiler.function.Function_f;
import org.riseger.protoctl.compiler.function.Null_f;
import org.riseger.protoctl.compiler.function.ProcessorFunction;
import org.riseger.utils.tree.MultiBranchesTree;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SemanticTree {
    private final Logger LOG = Logger.getLogger(SemanticTree.class);
    private final Node root = new Node();

    public SemanticTree(CommandTree commandTree) {
        this.root.copy(commandTree.getRoot());
    }

    public SemanticTree(ArrayList<Token> tokenList, SyntaxForest forest) throws Exception {
        ArrayList<SyntaxEqualPack> syntaxEqualPacks = new ArrayList<>(tokenList.size());
        CopyableIterator<SyntaxEqualPack> iterator = new CopyableIterator<>(syntaxEqualPacks);
        for (Token token : tokenList) {
            syntaxEqualPacks.add(new SyntaxEqualPack(this, this.root, token, iterator, forest)); // 请根据需要传递适当的参数给SyntaxEqualPack构造函数
        }

        suitTree(this.root, forest.getEntry(), iterator, forest);
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

    public boolean suitTree(Node tmp, int treeCode, CopyableIterator<SyntaxEqualPack> iterator, SyntaxForest forest) throws Exception {
        MultiBranchesTree<Class<Function_f>> mbt = forest.getSyntaxNode(treeCode);
        Class<Function_f> f = mbt.find(iterator);
        if (f == null) return false;
        if (!Null_f.class.isAssignableFrom(f)) {
            tmp.setFunction(f.newInstance());
        }
        return true;
    }

    public boolean getEndNode(Node node, Token token, int code, SyntaxForest forest) {
        if (token.getType().equals(forest.getEndType(code))) {
            node.setFunction(new Entity_f(token.getEntity()));
            return true;
        } else {
            return false;
        }
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

        public void setFunction(Function_f function) {
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
            //TODO: 语法树解析时添加函数部分出现问题
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
