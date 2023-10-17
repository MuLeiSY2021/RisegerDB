package org.riseger.utils.tree;

public interface TreeIterable<E> {
    int length();

    E get();

    TreeIterable<E> deeper(int index);

    TreeIterable<E> shallower();
}
