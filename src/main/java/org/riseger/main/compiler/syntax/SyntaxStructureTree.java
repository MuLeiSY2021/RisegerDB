package org.riseger.main.compiler.syntax;

import org.riseger.main.compiler.lextcal.Keyword;
import org.riseger.main.compiler.token.Token;

import java.util.LinkedList;

public class SyntaxStructureTree {
    private final Node root = new Node(null, false, null, -1);

    public LayerIterator layerIterator() {
        return new LayerIterator(root);
    }

    static class Node {
        private final Token token;

        private final boolean isKeyword;

        private final Keyword keyword;

        private final int typeCode;

        private final LinkedList<Node> children = new LinkedList<>();


        public Node(Token token, boolean isKeyword, Keyword keyword, int typeCode) {
            this.token = token;
            this.isKeyword = isKeyword;
            this.keyword = keyword;
            this.typeCode = typeCode;
        }

        public Node get(int index) {
            return children.get(index);
        }

        public void add(Token token, boolean isKeyword, Keyword keyword, int typeCode) {
            this.children.add(new Node(token, isKeyword, keyword, typeCode));
        }

        public void addFirst(Token token, boolean isKeyword, Keyword keyword, int typeCode) {
            this.children.addFirst(new Node(token, isKeyword, keyword, typeCode));
        }
    }

}
