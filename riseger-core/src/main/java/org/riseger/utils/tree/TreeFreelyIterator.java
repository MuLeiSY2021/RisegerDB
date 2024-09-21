package org.riseger.utils.tree;

import java.util.Stack;

public class TreeFreelyIterator<E> {
    private final Stack<Integer> indexes = new Stack<>();

    private int level = 0;

    private TreeIterable<E> root;

    public TreeFreelyIterator(TreeIterable<E> root) {
        this.root = root;
    }

    public E getE() {
        return root.get();
    }

    public Object getEqual() {
        return root.getEqual();
    }

    public void right() {
        if (this.root == null) {
            throw new NullPointerException();
        }
        if (root.shallower() == null) {
            throw new IndexOutOfBoundsException();
        }
        int index;
        if (this.level == this.indexes.size()) {
            index = 0;
        } else {
            index = indexes.pop();
        }
        this.root = this.root.shallower().deeper(++index);
        this.indexes.push(index);
    }

    public boolean rightble() {
        if (this.root == null) {
            return false;
        }
        return root.shallower() != null &&
                (this.level == this.indexes.size() ||
                        this.root.shallower().length() != this.indexes.peek());
    }


    public boolean leftble() {
        if (this.root == null ||
                this.level == this.indexes.size() ||
                root.shallower() == null) {
            return false;
        }
        int index = indexes.pop();
        return this.root.shallower().length() != index;
    }

    public void left() {
        if (this.root == null) {
            throw new NullPointerException();
        }
        if (indexes.size() == level) {
            if (this.level == this.indexes.capacity() || root.shallower() == null) {
                throw new IndexOutOfBoundsException();
            }
            int index = indexes.pop();
            if (this.root.shallower().length() == index) {
                this.indexes.push(index);
                throw new IndexOutOfBoundsException();
            }
            this.root = this.root.shallower().deeper(++index);
            this.indexes.push(index);
        }
    }

    public void up() {
        if (this.root == null) {
            throw new NullPointerException();
        }
        if (this.indexes.isEmpty()) {
            throw new IndexOutOfBoundsException();
        }
        this.indexes.pop();
        this.root = this.root.shallower();
        this.level--;
    }

    public boolean uppble() {
        return !(this.indexes.isEmpty() || this.root == null);
    }

    public Object down() {
        if (this.root == null) {
            throw new NullPointerException();
        }
        if (this.root.length() == 0) {
            throw new IndexOutOfBoundsException();
        }
        this.root = this.root.deeper(0);
        this.level++;
        return this.root.getEqual();
    }

    public boolean downable() {
        return this.root != null && this.root.length() != 0;
    }
}
