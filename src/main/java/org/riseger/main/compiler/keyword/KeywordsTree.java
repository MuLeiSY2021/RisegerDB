package org.riseger.main.compiler.keyword;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

public class KeywordsTree {
    public static final KeywordsTree INSTANCE = new KeywordsTree();
    private final Node root = new Node();


    private KeywordsTree() {
        for (Keyword keyword : Keyword.getKeywords()) {
            root.insert(keyword);
        }
    }

    public String getCode(String name) {
        Keyword keyword;
        if ((keyword = root.get(name)) != null) {
            return keyword.getCode();
        } else {
            return null;
        }
    }

    public int getIndex(String word) {
        return root.getIndex(word);
    }

    public Keyword get(String tmp) {
        return root.get(tmp);
    }

    public boolean contain(String tmp) {
        return root.get(tmp) != null;
    }

    @Data
    private static class Node {
        private char word;

        private Keyword keyword;

        private List<Node> children = new LinkedList<>();

        private Node parent;

        public Node() {
        }

        public Node(char word, Keyword keyword, Node parent) {
            this.word = word;
            this.keyword = keyword;
            this.parent = parent;
        }

        public void insert(Keyword keyword, int index) {
            try {
                for (Node node : children) {
                    if (keyword.equals(index, node.getWord())) {
                        if (keyword.isTail(index)) {
                            node.setKeyword(keyword);
                        } else {
                            node.insert(keyword, index + 1);
                        }
                        return;
                    }
                }
                Node childNode;
                if (keyword.isTail(index)) {
                    childNode = new Node(keyword.getWords(index), keyword, this);
                } else {
                    childNode = new Node(keyword.getWords(index), null, this);
                }
                children.add(childNode);
                if (!keyword.isTail(index)) {
                    childNode.insert(keyword, index + 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(keyword.getWords());
            }
        }

        public Keyword get(String name, int index) {
            for (Node node : children) {
                if (node.getWord() == name.charAt(index)) {
                    if (name.length() == index + 1) {
                        return node.getKeyword();
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
                    "letter=" + word +
                    '}';
        }

        public Keyword get(String name) {
            return this.get(name, 0);
        }

        public void insert(Keyword keyword) {
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
                    if (node.word == word.charAt(index + 1)) {
                        return node.getIndex(word, index + 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(word + " " + index);
                }
            }
            return index + 1;
        }
    }


}
