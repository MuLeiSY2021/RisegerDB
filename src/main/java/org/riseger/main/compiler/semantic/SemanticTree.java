package org.riseger.main.compiler.semantic;

import org.apache.log4j.Logger;
import org.riseger.protoctl.search.function.Function_f;

import java.util.LinkedList;
import java.util.List;

public class SemanticTree {
    public static final Logger LOG = Logger.getLogger(SemanticTree.class);
    private final Node root = new Node(null);

    public SemanticTree(Sy) {
    }

    private static class Node {
        public static final Logger LOG = Logger.getLogger(Node.class);


        private Function_f function;

        private boolean sortble = false;

        private List<Node> nodeList = new LinkedList<>();

        public Node(Class<? extends Function_f> function) {
            if (function != null) {
                try {
                    this.function = function.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    LOG.error("function not have empty instance function", e);
                }
            }
        }
    }
}
