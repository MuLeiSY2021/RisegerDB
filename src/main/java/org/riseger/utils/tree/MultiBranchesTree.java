package org.riseger.utils.tree;

import lombok.Getter;

import java.util.LinkedList;

public class MultiBranchesTree<C, E extends MultiTreeElement<C, E>> {
    private final Node root = new Node(null, null, null);

    public void insert(E element) {
        this.root.insert(element, 0);
    }

    @Getter
    class Node implements TreeIterable<E> {
        private final Node parent;

        private final LinkedList<Node> children = new LinkedList<>();

        private final E element;

        private final C checkElement;

        public Node(Node parent, E element, C checkElement) {
            this.parent = parent;
            this.element = element;
            this.checkElement = checkElement;
        }

        public void insert(E element, int index) {
            C checkElement = element.next(++index);
            if (checkElement == null) {
                return;
            }

            for (Node node : children) {
                if (checkElement.equals(node.getCheckElement())) {
                    node.insert(element, index);
                }
            }
            Node tmp;
            if (element.isTail(index)) {
                tmp = new Node(this, element, checkElement);
            } else {
                tmp = new Node(this, null, checkElement);
                tmp.insert(element, index);
            }
            children.add(tmp);

        }

        @Override
        public int length() {
            return this.children.size();
        }

        @Override
        public TreeIterable<E> shallower() {
            return this.parent;
        }

        @Override
        public E get() {
            return this.element;
        }

        @Override
        public TreeIterable<E> deeper(int index) {
            return this.children.get(index);
        }
    }
}
