package org.risegerdb.compile.lextcal;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import org.risegerdb.compile.config.CompileConfig;
import org.risegerdb.compile.tokenize.Token;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

public class LexicalTree {
    private final Node root = new Node();

    private LexicalTree() {
        Gson gson = new Gson();
        List<Token> tmp = null;
        try {
            tmp = gson.fromJson(new BufferedReader(new FileReader(CompileConfig.KEYWORDS_FILE_URI))
                    ,new TypeToken<List<Token>>() {}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Token> keywords = tmp;
        if(keywords != null) {
            for (Token keyword : keywords) {
                root.insert(keyword);
            }
        }
    }


    public static final LexicalTree INSTANCE = new LexicalTree();

    @Data
    private static class Node {
        private char letter;

        private Token token;

        private List<Node> children = new LinkedList<>();

        private Node parent;

        public Node() {
        }

        public Node(char letter, Token token, Node parent) {
            this.letter = letter;
            this.token = token;
            this.parent = parent;
        }

        public void insert(Token child, int index) {
            try {
                for (Node node : children) {
                    if (node.getLetter() == child.getName().charAt(index)) {
                        if (child.getName().length() == index + 1) {
                            node.setToken(child);
                        } else {
                            node.insert(child, index + 1);
                        }
                        return;
                    }
                }
                Node childNode;
                if (child.getName().length() == index + 1) {
                    childNode = new Node(child.getName().charAt(index), child, this);
                } else {
                    childNode = new Node(child.getName().charAt(index), null, this);
                }
                children.add(childNode);
                if (child.getName().length() > index + 1) {
                    childNode.insert(child, index + 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(child.getName());
            }
        }

        public Token get(String name, int index) {
            for (Node node : children) {
                if (node.getLetter() == name.charAt(index)) {
                    if (name.length() == index + 1) {
                        return node.getToken();
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

        public Token get(String name) {
            return this.get(name, 0);
        }

        public void insert(Token keyword) {
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

    public Token get(String name) {
        return root.get(name);
    }

    public int getIndex(String word) {
        return root.getIndex(word);
    }
}
