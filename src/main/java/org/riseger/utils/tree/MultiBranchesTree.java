package org.riseger.utils.tree;

import lombok.Getter;
import org.apache.log4j.Logger;
import org.riseger.main.system.compile.semantic.CopyableIterator;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class MultiBranchesTree<E> {
    private final Node root = new Node(null, null, null);

    private static final Logger LOG = Logger.getLogger(MultiBranchesTree.class);

    public void insert(MultiTreeElement<E> multiTreeElement) throws Exception {
        this.root.insert(multiTreeElement, 0);
    }

    public E search(Collection<Equable> equableCollection) throws Exception {
        return this.root.search(equableCollection.iterator());
    }

    public TreeFreelyIterator<E> getIterator() {
        return new TreeFreelyIterator<>(root);
    }

    public E find(Collection<Equable> equableCollection) throws Exception {
        return this.root.find(equableCollection.iterator());
    }

    public E find(CopyableIterator<? extends Equable> equableCopyableIterator) throws Exception {
        return this.root.find((CopyableIterator<Equable>) equableCopyableIterator);
    }

    @Getter
    class Node implements TreeIterable<E> {
        private final Node parent;

        private final LinkedList<Node> children = new LinkedList<>();

        private E element;

        private final Equable equable;

        public Node(Node parent, E element, Equable equable) {
            this.parent = parent;
            this.element = element;
            this.equable = equable;
        }

        public void insert(MultiTreeElement<E> multiTreeElement, int index) throws Exception {
            //TODO: insert插入元素出问题了
            Equable checkElement = multiTreeElement.next(index);
            if (checkElement == null) {
                return;
            }

            for (Node node : children) {
                if (checkElement.equal(node.getEquable())) {
                    if (multiTreeElement.isTail(index)) {
                        node.element = multiTreeElement.get();
                    } else {
                        node.insert(multiTreeElement, ++index);
                    }
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

        public E search(Iterator<Equable> equableIterator) throws Exception {
            if (equableIterator.hasNext()) {
                Equable equable = equableIterator.next();
                for (Node node : this.children) {
                    if (node.equal(equable)) {
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

        private boolean equal(Equable equable) throws Exception {
            return equable.equal(this.equable);
        }

        public E find(Iterator<Equable> iterator) throws Exception {
            if (iterator.hasNext()) {
                Equable equable = iterator.next();
                for (Node node : this.children) {
                    if (node.equal(equable)) {
                        E e = node.find(iterator);
                        if (e == null && node.element != null && iterator instanceof ListIterator) {
                            ((ListIterator<Equable>) iterator).previous();
                        }
                        return e == null ? node.element : e;
                    }
                }
                return null;
            } else {
                return this.element;
            }
        }

        public E find(CopyableIterator<Equable> equableCopyableIterator) throws Exception {
            if (equableCopyableIterator.hasNext() && !this.children.isEmpty()) {
                CopyableIterator<Equable> copyableIterator = equableCopyableIterator.copy();
                for (Node node : this.children) {
                    Equable equable = equableCopyableIterator.next();
                    if (node.equal(equable)) {
                        E e = node.find(equableCopyableIterator);
                        return e == null ? node.element : e;
                    }
                    equableCopyableIterator.back(copyableIterator);
                }
                if (this.element != null) {
                    return this.element;
                }
                return null;
            } else {
                return this.element;
            }
        }
    }
}
