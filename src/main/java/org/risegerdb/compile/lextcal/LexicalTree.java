package org.risegerdb.compile.lextcal;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

public class LexicalTree {
    private final Node root = new Node();

    private LexicalTree() {
        for (Function function:Function.FUNCTIONS_BY_KEY.values()) {
            root.insert(function);
        }
    }


    public static final LexicalTree INSTANCE = new LexicalTree();


    @Data
    private static class Node {
        private char letter;

        private Function function;

        private List<Node> children = new LinkedList<>();

        private Node parent;

        public Node() {
        }

        public Node(char letter, Function function, Node parent) {
            this.letter = letter;
            this.function = function;
            this.parent = parent;
        }

        public void insert(Function function, int index) {
            try {
                for (Node node : children) {
                    if (node.getLetter() == function.getKey().charAt(index)) {
                        if (function.getKey().length() == index + 1) {
                            node.setFunction(function);
                        } else {
                            node.insert(function, index + 1);
                        }
                        return;
                    }
                }
                Node childNode;
                if (function.getKey().length() == index + 1) {
                    childNode = new Node(function.getKey().charAt(index), function, this);
                } else {
                    childNode = new Node(function.getKey().charAt(index), null, this);
                }
                children.add(childNode);
                if (function.getKey().length() > index + 1) {
                    childNode.insert(function, index + 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(function.getKey());
            }
        }

        public Function get(String name, int index) {
            for (Node node : children) {
                if (node.getLetter() == name.charAt(index)) {
                    if (name.length() == index + 1) {
                        return node.getFunction();
                    } else {
                        return node.get(name, index + 1);
                    }
                }
            }
            return null;
        }


        @Override
        public String toString() {
            return "Node{" +
                    "letter=" + letter +
                    '}';
        }

        public Function get(String name) {
            return this.get(name, 0);
        }

        public void insert(Function keyword) {
            this.insert(keyword, 0);
        }

        public int getIndex(String word) {
            return getIndex(word, -1);
        }

        public int getIndex(String word, int index) {
            if (index + 1 == word.length()) {
                return index + 1;
            }
            for (Node node : children) {
                try {
                    if (node.letter == word.charAt(index + 1)) {
                        return node.getIndex(word, index + 1);
                    }
                }catch ( Exception e) {
                    e.printStackTrace();
                    System.out.println(word + " "+index);
                }
            }
            return index + 1;
        }
    }

    public String getId(String name) {
        Function function;
        if((function =root.get(name)) != null) {
            return function.getId();
        } else {
            return null;
        }
    }

    public int getIndex(String word) {
        return root.getIndex(word);
    }

    public Function get(String tmp) {
        return root.get(tmp);
    }

}
