package org.risegerdb.compile.syntax;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import lombok.Data;
import org.risegerdb.compile.Compiler;
import org.risegerdb.compile.config.CompileConfig;
import org.risegerdb.compile.lextcal.Function;
import org.risegerdb.compile.lextcal.Lexicon;

import java.beans.Transient;
import java.util.*;

public class PriorityTree {
    private final Node root;

    public PriorityTree(List<Lexicon> line) {
        this.root = new Node(line);
    }

    public static final Map<String,Integer> MULTIPLE_MAP = new HashMap<>();
    static {
        MULTIPLE_MAP.put("{",3);
        MULTIPLE_MAP.put("[",2);
        MULTIPLE_MAP.put("(",1);
    }

    public static final Map<String,String> ANOTHER_PART_MAP = new HashMap<>();
    static {
        ANOTHER_PART_MAP.put("{","}");
        ANOTHER_PART_MAP.put("[","]");
        ANOTHER_PART_MAP.put("(",")");
    }

    @Data
    static class Node {
        private List<Lexicon> line;

        private int prevIndex;

        private Node lChild;

        private Node rChild;

        private transient Node parent;

        public Node(List<Lexicon> line) {
            this.line = line;
            setChild(line);
        }

        private void setChild(List<Lexicon> line) {
            if(canSplit(line)) {
                int[] indexes = getPrioritizeIndex(line);
                if (indexes[0] > -1) {
                    this.line = new LinkedList<>(line.subList(indexes[0], indexes[1] + 1));
                    if(indexes[0] > 0) {
                        List<Lexicon> children = line.subList(0, indexes[0]);
                        if (canSplit(children)) {
                            this.lChild = new Node(children, this, 0);
                        } else {
                            this.line.addAll(0, children);
                        }
                    }
                    if(indexes[1] + 1 < line.size() ) {
                        List<Lexicon> children = line.subList(indexes[1] + 1, line.size());
                        if (canSplit(children)){
                            this.rChild = new Node(line.subList(indexes[1] + 1, line.size()), this, indexes[1] + 1);
                        } else{
                            this.line.addAll(children);
                        }
                    }
                }
            }
        }

        public boolean isFunction(List<Lexicon> children) {
            boolean flg = true;
            return flg;
        }

        public Node(List<Lexicon> line, Node parent,int prevIndex) {
            this.line = line;
            this.parent = parent;
            setChild(line);
            this.prevIndex = prevIndex;
        }

        private int[] getPrioritizeIndex(List<Lexicon> line) {
                int[] result = new int[2];
                int max = Integer.MIN_VALUE,
                        index = 0,
                        muti = 0;
                Stack<String> prefix = new Stack<>();
                for (Lexicon lexicon : line) {
                    try {
                        Function f = lexicon.getFunction();
                        if (MULTIPLE_MAP.containsKey(lexicon.getValue())) {
                            muti += MULTIPLE_MAP.get(lexicon.getValue());
                            prefix.add(lexicon.getValue());
                        } else if (!prefix.isEmpty() && lexicon.getValue() != null && lexicon.getValue().equals(ANOTHER_PART_MAP.get(prefix.peek()))) {
                            muti -= MULTIPLE_MAP.get(prefix.peek());
                            prefix.pop();
                        }
                        int p = f == null || f.getPriority() == null && lexicon.getValue() != null &&
                                !MULTIPLE_MAP.containsKey(lexicon.getValue()) ?
                                Integer.MIN_VALUE : f.getPriority() == null ?
                                MULTIPLE_MAP.get(lexicon.getValue()) : f.getPriority()
                                - muti * CompileConfig.MORE_PRIORITY + prefix.size();
                        if (p > max) {
                            max = p;
                            result[0] = index;
                            result[1] = index;
                        }
                        index++;
                    } catch (Exception e) {
                        System.err.println(new Gson().toJson(line) + "\n" + new Gson().toJson(lexicon));
                        throw e;
                    }
                }
                if(PriorityTree.MULTIPLE_MAP.containsKey(line.get(result[0]).getValue()) &&
                    result[0] > 0 && (line.get(result[0] - 1).equals(Lexicon.Type.STR) ||
                        line.get(result[0] - 1).equals(Lexicon.Type.FUN))) {
                    result[0]--;
                    index = result[0];
                    Iterator<Lexicon> iterator = line.listIterator(result[0] + 1);
                    Stack<String> stack = new Stack<>();
                    stack.push(line.get(result[0]).getValue());
                    for (Lexicon lexicon = iterator.next(); iterator.hasNext(); lexicon = iterator.next()) {
                        if(stack.isEmpty()) {
                            result[1] = index;
                        }
                        if(MULTIPLE_MAP.containsKey(lexicon.getValue())) {
                            stack.push(lexicon.getValue());
                        } else if(lexicon.equals(ANOTHER_PART_MAP.get(stack.peek()))) {
                            stack.pop();
                        }
                        index++;
                    }
                }
                return result;
        }

        private boolean canSplit(List<Lexicon> line) {
            int i = 0;
            for (Lexicon lexicon : line) {
                if(lexicon.getFunction() != null &&
                        lexicon.getFunction().getPriority() != null &&
                        lexicon.getFunction().getPriority() < Integer.MAX_VALUE) {
                    i++;
                }
            }
            return i > 0;
        }

        private Node getTail() {
            if(this.lChild != null) {
                return this.lChild.getTail();
            } else if(this.rChild != null) {
                return this.rChild.getTail();
            } else {
                return this;
            }
        }

    }

    public Session getSession() {
        return new Session(root.getTail());
    }

}
