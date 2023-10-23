package org.riseger.main.compiler.compoent;

import org.riseger.main.sql.function.type.Function_c;

import java.util.Queue;

public class CommandList {
    public Function_c[] functionList;

    private int index = 0;

    public CommandList() {
    }

    public void setFunctionList(Queue<Function_c> functionQueue) {
        this.functionList = functionQueue.toArray(new Function_c[0]);
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
