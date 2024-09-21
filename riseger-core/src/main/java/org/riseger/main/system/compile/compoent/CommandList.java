package org.riseger.main.system.compile.compoent;

import lombok.Data;
import org.riseger.main.system.compile.function.Function_c;

import java.util.Queue;

@Data
public class CommandList {
    public Function_c[] functionList;

    private int index = 0;

    public CommandList() {
    }

    public CommandList(Function_c[] functionList) {
        this.functionList = functionList;
    }

    public void setFunctionList(Queue<Function_c> functionQueue) {
        this.functionList = functionQueue.toArray(new Function_c[0]);
    }

    public void jumpTo(int index) {
        this.index = index;
    }

    public int index() {
        return this.index;
    }

    public Function_c next() {
        return this.functionList[this.index++];
    }

    public boolean hasNext() {
        return this.index < this.functionList.length;
    }

    public boolean isEmpty() {
        return this.functionList.length == 0;
    }

    public int size() {
        return this.functionList.length;
    }
}
