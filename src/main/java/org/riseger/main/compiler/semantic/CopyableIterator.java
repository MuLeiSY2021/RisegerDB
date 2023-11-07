package org.riseger.main.compiler.semantic;

import lombok.Getter;

import java.util.ArrayList;

public class CopyableIterator<E> {
    ArrayList<E> arrayList;

    @Getter
    int index;

    public CopyableIterator(ArrayList<E> arrayList) {
        this.arrayList = arrayList;
        index = 0;
    }

    private CopyableIterator(ArrayList<E> arrayList, int index) {
        this.arrayList = arrayList;
        this.index = index;
    }

    public E next() {
        return arrayList.get(index++);
    }

    public E previous() {
        return arrayList.get(--index);
    }

    public CopyableIterator<E> copy() {
        return new CopyableIterator<>(this.arrayList, this.index);
    }

    public boolean hasNext() {
        return this.arrayList.size() > index;
    }

    public void back(CopyableIterator<E> iterator) {
        this.index = iterator.getIndex();
    }

}
