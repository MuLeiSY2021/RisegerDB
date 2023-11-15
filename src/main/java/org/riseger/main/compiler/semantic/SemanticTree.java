package org.riseger.main.compiler.semantic;

import lombok.Data;
import org.apache.log4j.Logger;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.Function_c;
import org.riseger.main.compiler.syntax.SyntaxForest;
import org.riseger.main.compiler.token.Token;
import org.riseger.protoctl.compiler.CommandTree;
import org.riseger.protoctl.compiler.function.*;
import org.riseger.utils.Utils;
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
        Stack<TreeIterator<Function_f>> nodeStack = new Stack<>();
        for (Token token : tokenList) {
            syntaxEqualPacks.add(new SyntaxEqualPack(this, token, iterator, forest, nodeStack)); // 请根据需要传递适当的参数给SyntaxEqualPack构造函数
        }
        nodeStack.push(new SemanticTreeIterator(this.root));
        suitTree(nodeStack, forest.getEntry(), iterator, forest);
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

    public boolean suitTree(Stack<TreeIterator<Function_f>> nodeStack, int treeCode, CopyableIterator<SyntaxEqualPack> iterator, SyntaxForest forest) throws Exception {
        MultiBranchesTree<Class<Function_f>> mbt = forest.getSyntaxNode(treeCode);
        Class<Function_f> f = mbt.find(iterator);
        TreeIterator<Function_f> tmp = nodeStack.pop();
        if (f == null || tmp == null) {
            return false;
        } else if (!Null_f.class.isAssignableFrom(f)) {
            tmp.set(f.newInstance());
        }
        return true;
    }

    public boolean getEndNode(Stack<TreeIterator<Function_f>> nodeStack, Token token, int code, SyntaxForest forest) {
        TreeIterator<Function_f> tmp = nodeStack.pop();
        if (tmp == null) {
            return false;
        }
        if (token.getType().equals(forest.getEndType(code))) {
            tmp.set(new Entity_f(token.getEntity()));
            return true;
        } else {
            return false;
        }
    }

    public TreeIterator<Function_f> iteratorEmpty() {
        return new SemanticTreeIterator(new Node());
    }

    @Data
    public static class Node {
        private static final Logger LOG = Logger.getLogger(Node.class);

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
            if (canSort) {
                children.sort(Comparator.comparing(Node::getPriority));
            }

            int[] array = null;
            if (this.function instanceof ProcessorFunction_f) {
                ProcessorFunction_f processorFunction = (ProcessorFunction_f) this.function;
                array = processorFunction.getPostFunSize();
                level += processorFunction.getInsertFunSize();
            }

            int total = level;
            int i = 0;
            for (Node c : children) {
                if (array != null && array.length == i + 1) {
                    total += array[i];
                }
                total = c.sort(total);
                i++;
            }

            this.level = total;
            if (this.function != null) {
                this.level++;
                LOG.debug("Fun:" + Utils.getClassLastDotName(this.function.getClass()) + " Level:" + this.level);
            }
            return this.level;
        }

        void stretchTree(Integer[] len, SearchMemory searchMemory, CommandList commandList) {
            Function_c function;
            if (this.function != null) {
                function = Function_c.getFunctionFromMap(this.function, searchMemory, commandList);

                if (function instanceof ProcessorFunction) {
                    List<Function_f> functionList = new LinkedList<>();
                    ProcessorFunction processorFunction = (ProcessorFunction) function;
                    processorFunction.stretch(this, len[0], functionList);
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
                try {
                    if (function instanceof ProcessorFunction) {
                        ProcessorFunction processorFunction = (ProcessorFunction) function;
                        List<Function_f> list = processorFunction.preprocess();
                        if (list != null) {
                            for (Function_f f : list) {
                                queue.add(Function_c.getFunctionFromMap(f, searchMemory, commandList));
                            }
                        }
                    }
                } catch (NullPointerException e) {
                    LOG.debug(this.function.getClass().getCanonicalName());
                    throw e;
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

    public static class SemanticTreeIterator implements TreeIterator<Function_f> {
        private final Node node;

        public SemanticTreeIterator(Node node) {
            this.node = node;
        }

        @Override
        public TreeIterator<Function_f> copy(TreeIterator<Function_f> iterator) {
            return new SemanticTreeIterator(node);
        }

        @Override
        public void add(TreeIterator<Function_f> iterator) {
            this.node.add(((SemanticTreeIterator) iterator).node);
        }

        @Override
        public void set(Function_f function) {
            this.node.setFunction(function);
        }
    }
}
