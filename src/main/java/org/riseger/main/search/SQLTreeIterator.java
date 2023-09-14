package org.riseger.main.search;

import org.riseger.protoctl.search.function.FUNCTION;

public class SQLTreeIterator {
    public SQLTree.SQLNode tail;

    public int index;

    public SQLTreeIterator(SQLTree.SQLNode root) {
        while (root.getSqlList().size() != 0) {
            root = root.getSqlList().get(0);
        }
        this.tail = root;
        this.index = 0;
    }

    public FUNCTION next() {
        FUNCTION r = tail.getFunction();
        if (tail.getParent() != null) {
            while (tail.getParent().getSqlList().size() > index) {
                tail = tail.getParent();
                index = 0;
            }
            index++;
            tail = tail.getParent().getSqlList().get(index);
        } else {
            tail = null;
        }
        return r;
    }

    public boolean hasNext() {
        return tail == null;
    }
}
