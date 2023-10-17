package org.riseger.utils.tree;

import java.util.Stack;

public class TreeIterator<E> {
    private final Stack<Integer> indexes = new Stack<>();
    private final MultiBranchesTreeIterStrategy strategy;
    private TreeIterable<E> root;

    public TreeIterator(MultiBranchesTreeIterStrategy strategy, TreeIterable<E> root) {
        this.strategy = strategy;
        this.root = root;
        if (strategy == MultiBranchesTreeIterStrategy.DEPTH_FIRST) {
            while (root.length() > 0) {
                indexes.push(1);
                this.root = root.shallower();
            }
        }
    }

    public E next() throws IndexOutOfBoundsException {
        TreeIterable<E> tmp_root;
        E e;
        Integer index;
        if (root == null) {
            throw new IndexOutOfBoundsException("root cannot be null");
        }
        switch (strategy) {
            case BREADTH_FIRST:
                e = root.get();
                if (indexes.isEmpty()) {
                    index = 0;
                } else {
                    index = indexes.pop();
                }
                tmp_root = root.deeper(index);
                indexes.push(index + 1);
                this.root = tmp_root;
                break;

            case DEPTH_FIRST:
                e = root.get();
                index = indexes.pop();
                tmp_root = root.shallower();
                if (tmp_root == null) {
                    break;
                }
                if (tmp_root.length() > index) {
                    tmp_root = tmp_root.deeper(++index);
                    indexes.push(index);
                }
                root = tmp_root;
                break;

            default:
                return null;
        }
        return e;
    }


    @Deprecated
    public E previous() {
        return null;
    }

    boolean hasNext() {
        switch (strategy) {
            default:
                throw new UnsupportedOperationException();

            case BREADTH_FIRST:
                return root.length() >= indexes.peek() + 1;

            case DEPTH_FIRST:
                return indexes.peek() != -1;
        }
    }
}
