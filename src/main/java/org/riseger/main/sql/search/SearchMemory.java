package org.riseger.main.sql.search;

import org.riseger.main.Constant;

public class SearchMemory {

    Object[] universalRegister = new Object[Constant.DEFAULT_MEMORYSIZE];

    private int index;

    public Object get() {
        return this.universalRegister[--index];
    }

    public void set(Object object) {
        this.universalRegister[index++] = object;
    }
}
