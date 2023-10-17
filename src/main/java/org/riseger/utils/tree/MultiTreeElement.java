package org.riseger.utils.tree;

public interface MultiTreeElement<C, E> {
    C next(int index);

    E get();

    boolean isTail(int index);
}
