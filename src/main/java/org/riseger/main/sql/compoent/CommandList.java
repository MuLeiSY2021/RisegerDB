package org.riseger.main.sql.compoent;

import org.riseger.main.sql.function.type.Function_c;

public class CommandList {
    public Function_c[] functionList;

    private int index;

    public CommandList(int size) {
        this.functionList = new Function_c[size];
        this.index = 0;
    }

    public void jump(int index) {
        this.index = index;
    }

    public int index() {
        return this.index;
    }

    public Function_c next() {
        return this.functionList[this.index++];
    }

    public void putFunction(Function_c function) {
        this.functionList[this.index++] = function;
    }

    public void cover() {
        this.index = 0;
    }
}
