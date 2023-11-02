package org.riseger.utils.tree;

import lombok.Getter;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class MultiBranchesTree<E> {
    private final Node root = new Node(null, null, null);

    private static final Logger LOG = Logger.getLogger(MultiBranchesTree.class);

    public void insert(MultiTreeElement<E> multiTreeElement) {
        this.root.insert(multiTreeElement, 0);
    }

    public E search(Collection<Equable> equableCollection) {
        return this.root.search(equableCollection.iterator());
    }

    public TreeFreelyIterator<E> getIterator() {
        return new TreeFreelyIterator<>(root);
    }

    public E find(Collection<Equable> equableCollection) {
        return this.root.find(equableCollection.iterator());
    }

    @Getter
    class Node implements TreeIterable<E> {
        private final Node parent;

        private final LinkedList<Node> children = new LinkedList<>();

        private final E element;

        private final Equable equable;

        public Node(Node parent, E element, Equable equable) {
            this.parent = parent;
            this.element = element;
            this.equable = equable;
        }

        public void insert(MultiTreeElement<E> multiTreeElement, int index) {
            Equable checkElement = multiTreeElement.next(index);
            if (checkElement == null) {
                return;
            }

            for (Node node : children) {
                if (checkElement.equals(node.getEquable())) {
                    node.insert(multiTreeElement, ++index);
                    return;
                }
            }
            Node tmp;
            if (multiTreeElement.isTail(index)) {
                tmp = new Node(this, multiTreeElement.get(), checkElement);
            } else {
                tmp = new Node(this, null, checkElement);
                tmp.insert(multiTreeElement, ++index);
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

        public E search(Iterator<Equable> equableIterator) {
            if (equableIterator.hasNext()) {
                Equable equable = equableIterator.next();
                for (Node node : this.children) {
                    if (node.equals(equable)) {
                        return node.search(equableIterator);
                    }
                }
                return null;
            } else {
                return this.element;
            }
        }

        @Override
        public Equable getEqual() {
            return this.equable;
        }

        private boolean equals(Equable equable) {
            return this.equable.equals(equable);
        }

        public E find(Iterator<Equable> iterator) {
            if (iterator.hasNext()) {
                Equable equable = iterator.next();
                for (Node node : this.children) {
                    if (node.equals(equable)) {
                        E e = node.find(iterator);
                        return e == null ? node.element : e;
                    }
                }
                return null;
            } else {
                return this.element;
            }
        }
    }
}
