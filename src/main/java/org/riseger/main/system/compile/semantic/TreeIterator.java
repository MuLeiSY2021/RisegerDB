package org.riseger.main.system.compile.semantic;

public interface TreeIterator<E> {
    TreeIterator<E> copy(TreeIterator<E> iterator);

    void add(TreeIterator<E> iterator);

    void set(E e);
}
