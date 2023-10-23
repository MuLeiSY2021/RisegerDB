package org.riseger.main.compiler.syntax;

import lombok.Data;
import org.apache.log4j.Logger;
import org.riseger.main.compiler.token.Token;
import org.riseger.utils.tree.TreeFreelyIterator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SyntaxStructureTree {

    private final Logger LOG = Logger.getLogger(SyntaxStructureTree.class);
    private final Node root = new Node(null);

    public SyntaxStructureTree(List<Token> tokenList, SyntaxForest forest) {
        Iterator<Token> tokenIterator = tokenList.iterator();
        suit(root, tokenIterator, forest);
    }

    private List<Object[]> suitOneBranche(Node node, Iterator<Token> tokenIterator, SyntaxForest forest) {

    }

    private boolean suit(Node node, Iterator<Token> tokenIterator, SyntaxForest forest) {
        TreeFreelyIterator<Syntax> treeIterator_entry = forest.getSyantaxTree(forest.getEntry()).getIterator();
        if (treeIterator_entry.downable()) {
            treeIterator_entry.down();
        } else {
            LOG.fatal("SyntaxTree Empty");
        }
        Token tmp = tokenIterator.next();
        for (; tokenIterator.hasNext(); tmp = tokenIterator.next()) {
            if (treeIterator_entry.get().equals(tmp)) {
                node.add(new Node(node, ))
            }
            List<Token> tmpList = new LinkedList<>();
        }
    }

    @Data
    private static class Node {
        private Node parent;

        private List<Node> chlidren = new LinkedList<>();

        private Syntax element;

        public Node(Node parent) {
            this.parent = parent;
        }
    }

}
