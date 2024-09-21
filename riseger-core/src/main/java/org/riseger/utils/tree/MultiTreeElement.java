package org.riseger.utils.tree;

public interface MultiTreeElement<E> {
    Equable next(int index);

    E get();

    boolean isTail(int index);
}
