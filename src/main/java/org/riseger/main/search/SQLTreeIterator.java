package org.riseger.main.search;

import org.riseger.main.search.function.Function_c;

public class SQLTreeIterator {
    private final boolean isSub;

    public SQLTree.SQLNode tail;

    public int index;

    public SQLTreeIterator(SQLTree.SQLNode root, boolean isSub) {
        this.isSub = isSub;
        this.tail = root;
        this.index = 0;

        while (tail.getSqlList().size() != 0) {
            for (int i = 0; i < tail.getSqlList().size(); i++) {
                if (!isSub || !tail.getSqlList().get(i).isCanSkip()) {
                    tail = tail.getSqlList().get(i);
                    index = i;
                }
            }
        }
    }

    public Function_c next() {
        Function_c result = tail.getFunction();
        index++;

        while (tail.getParent().getSqlList().size() <= index || (tail.getSqlList().get(index).isCanSkip() && isSub)) {
            if (tail.getParent().getSqlList().size() <= index) {
                tail = tail.getParent();
                index = 0;
            } else {
                index++;
            }
        }
        tail = tail.getParent().getSqlList().get(index);
        return result;
    }

    public boolean hasNext() {
        return tail == null;
    }
}
