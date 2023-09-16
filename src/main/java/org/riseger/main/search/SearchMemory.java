package org.riseger.main.search;

import org.riseger.main.Constant;

public class SearchMemory {

    Object[] universalRegister = new Object[Constant.DEFAULT_MEMORYSIZE];


    public Object get(int index) {
        return this.universalRegister[index];
    }

    public void set(int index,Object object) {
        this.universalRegister[index] = object;
    }
}
