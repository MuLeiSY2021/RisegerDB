package org.riseger.main.compiler.semantic;

public interface TreeIterator<E> {
    TreeIterator<E> copy(TreeIterator<E> iterator);

    void add(TreeIterator<E> iterator);

    void set(E e);
}
