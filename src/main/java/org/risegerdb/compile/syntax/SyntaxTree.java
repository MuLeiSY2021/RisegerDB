package org.risegerdb.compile.syntax;

import lombok.Data;
import org.risegerdb.compile.config.CompileConfig;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SyntaxTree {
    private final Node root = new Node();

    private SyntaxTree() {
        for (Function function:FUNCTIONS.values()) {
            root.insert(function);
        }
    }
    //不定参的paramCount = -1

    public static final Map<String, Function> FUNCTIONS = Function.FUNCTIONS;;

    public static final SyntaxTree INSTANCE = new SyntaxTree();

    @Data
    private static class Node {
        private String word;

        private Function function;

        private List<Node> children = new LinkedList<>();

        private Node parent;

        public Node() {
        }

        public Node(String word) {
            this.word = word;
        }

        public void insert(Function child) {
            this.insert(child.getSyntax(),0);
        }

        public void insert(List<String> words,int index) {
            if(index > words.size() - 1) {
                return;
            }
            Node child = new Node(words.get(index));
            this.insert(child);
            child.insert(words,index + 1);
        }

        private void insert(Node node) {
            for (Node node1:this.children) {
                if(node1.getWord().equals(node1.word)) {
                    for (Node child:node.children) {
                        node1.insert(child);
                    }
                    return;
                }
            }
            this.children.add(node);
        }

        @Override
        public String toString() {
            return "Node{" +
                    "letter='" + this.word + "'" +
                    '}';
        }
    }

}
