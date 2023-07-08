package org.risegerdb.compile.syntax;

import lombok.Data;
import org.risegerdb.compile.lextcal.Function;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SyntaxTree {
    private final Node root = new Node();

    private SyntaxTree() {
        for (Function function:FUNCTIONS.values()) {
            if(function.getSyntax() != null) {
                root.insert(function);
            }
        }
    }
    //不定参的paramCount = -1

    public static final Map<String, Function> FUNCTIONS = Function.FUNCTIONS_BY_KEY;;

    public static final SyntaxTree INSTANCE = new SyntaxTree();

    @Data
    private static class Node {
        private String word;

        private Function function;

        private List<Node> children = new LinkedList<>();

        private Node parent;

        public Node() {
        }

        public Node(String word, Node parent) {
            this.word = word;
            this.parent = parent;
        }

        public void insert(Function child) {
            this.insert(child.getSyntaxList(),0,child);
        }

        public void insert(List<String> words,int index,Function function) {
            if(index > words.size() - 1) {
                return;
            }
            Node child = new Node(words.get(index),this);
            if(words.size() == index) {
                child.setFunction(function);
            }
             child = this.insert(child);
            child.insert(words,index + 1,function);
        }

        private Node insert(Node node) {
            for (Node node1:this.children) {
                if(node1.getWord().equals(node.word)) {
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
    }

}
