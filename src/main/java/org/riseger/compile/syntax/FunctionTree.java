package org.riseger.compile.syntax;

import com.google.gson.Gson;
import lombok.Data;
import org.riseger.compile.config.Config;
import org.riseger.compile.tokenize.Token;
import org.riseger.exception.CompileException;
import org.riseger.exception.NullTokenException;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class FunctionTree {
    private final Node root = new Node();

    private FunctionTree() {
        for (Function function : Function.FUNCTIONS) {
            root.insert(function);
        }
    }

    public static final FunctionTree INSTANCE = new FunctionTree();

    @Data
    private static class Node {
        private String word;

        private Function function;

        private List<Node> children = new LinkedList<>();

        private Node parent;

        private int priority;

        public Node() {
        }

        public Node(String word, Node parent, int priority) {
            this.word = word;
            this.parent = parent;
            if (word.startsWith(Config.TYPE_PREFIX)) {
                this.priority = -Function.maxPriority;
            } else {
                this.priority = priority;
            }
        }

        public void insert(Function child) {
            this.insert(child.getList(), 0, child);
        }

        public void insert(List<String> words, int index, Function function) {
            if (index > words.size() - 1) {
                return;
            }
            Node child = new Node(words.get(index), this, function.getPriority());
            if (words.size() - 1 == index) {
                child.setFunction(function);
            }
            child = this.insert(child);
            child.insert(words, index + 1, function);
        }

        private Node insert(Node node) {
            for (Node node1 : this.children) {
                if (node1.getWord().equals(node.word)) {
                    return node1;
                }
            }
            this.children.add(node);
            return node;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "letter='" + this.word + "'" +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Node)) return false;
            Node node = (Node) o;
            return Objects.equals(word, node.word);
        }

        public boolean equals(Token o) {
            if (o == null) return false;
            return Objects.equals(word, o.getCode()) ||
                    !o.getCode().startsWith(Config.KEYWORD_PREFIX) &&
                            word.endsWith(o.getCode().split(Config.SPLIT_PREFIX)[0]);
        }

        @Override
        public int hashCode() {
            return Objects.hash(word);
        }
    }

    public Iterator iterator() {
        return new Iterator(this.root);
    }

    private class Iterator {
        Node current;

        int index;

        public Iterator(Node root) {
            current = root;
        }

        public boolean hasNext() {
            return current.getChildren().size() > 0;
        }

        public Node next(Token node) {
            for (Node node0 : current.getChildren()) {
                if (node0.equals(node)) {
                    current = node0;
                    index++;
                    return current;
                }
            }
            return null;
        }

        public boolean hasPrev() {
            return current.getParent() != null;
        }


        public Node prev() {
            Node p = current.getParent();
            if (p != null) current = p;
            return p;
        }

        private boolean contain(Token token) {
            for (Node node : current.children) {
                if (node.equals(token)) {
                    return true;
                }
            }
            return false;
        }
    }

    public Session session(List<Token> tokens) {
        return new Session(tokens);
    }

    public class Session {
        List<Token> tokenLine;

        int max = Integer.MIN_VALUE;

        int max_i = -1;

        ListIterator<Token> iterator;

        int extra = 0;

        int index = 0;

        Queue<String> queue = new LinkedBlockingQueue<>(50);

        private final Map<String, String> bracketsMap = new HashMap<>();

        {
            this.bracketsMap.put("(", ")");
            this.bracketsMap.put("[", "]");
            this.bracketsMap.put("{", "}");
        }

        private final Map<String, Integer> priorityMap = new HashMap<>();

        {
            this.priorityMap.put("(", 3);
            this.priorityMap.put("[", 2);
            this.priorityMap.put("{", 1);
        }


        public Session(List<Token> tokens) {
            this.tokenLine = new LinkedList<>(tokens);
            iterator = tokenLine.listIterator();
        }

        private void setExtra(Token token) {
            String src = token.getSourceCode();
            if (bracketsMap.containsKey(src)) {
                queue.offer(bracketsMap.get(src));
                extra -= Function.maxPriority + priorityMap.get(src);
            } else if (!queue.isEmpty() && queue.peek().equals(src)) {
                String bracket = queue.poll();
                extra += Function.maxPriority + priorityMap.get(bracket);
            }
        }

        public int get() {
            FunctionTree.Iterator treeIterator = iterator();
            Token token;

            for (token = iterator.next(); iterator.hasNext(); token = iterator.next()) {
                Node node = treeIterator.next(token);
                if (node != null) {
                    setExtra(token);
                    setMax(node, index);
                    index++;
                } else {
                    iterator.previous();
                    iterator.add(new Token(get(index)));
                    index--;
                }
                System.out.println("index:" + index + " Line:" + new Gson().toJson(tokenLine));
            }
            return this.max_i;
        }

        private String get(int index) {
            FunctionTree.Iterator treeIterator = iterator();
            Token token = iterator.next();
            if (token == null) {
                throw new NullTokenException();
            }
            if (!treeIterator.contain(token)) {
                throw new CompileException(token);
            }
            for (; iterator.hasNext(); token = iterator.next()) {
                Node node = treeIterator.next(token);
                if (node != null) {
                    setExtra(token);
                    setMax(node, index);
                    index++;
                    if (!treeIterator.hasNext()) {
                        return node.getFunction().getReturnType();
                    }
                } else {
                    iterator.previous();
                    iterator.add(new Token(get(index)));
                }
                System.out.println("index:" + index + " Line:" + new Gson().toJson(tokenLine));

            }
            return null;
        }

        public void setMax(Node node, int max_i) {
            if (node.getPriority() - extra > this.max) {
                this.max = node.getPriority() - extra;
                this.max_i = max_i;
            }
        }

    }
}
