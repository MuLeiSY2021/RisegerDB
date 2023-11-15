package org.riseger.main.compiler.compoent;

import org.riseger.main.Constant;

import java.util.HashMap;
import java.util.Map;

public class SearchMemory {

    final Object[] stackMemory = new Object[Constant.DEFAULT_MEMORYSIZE];

    final Map<Integer, Object> mapMemory = new HashMap<>();

    private int variableIndex;

    public Object getVar() {
        if (variableIndex == 0) {
            throw new IndexOutOfBoundsException("Not enough variable");
        }
        return this.stackMemory[--variableIndex];
    }

    public void setVar(Object object) {
        if (variableIndex == Constant.DEFAULT_MEMORYSIZE) {
            throw new IndexOutOfBoundsException("Stack over flow size:" + Constant.DEFAULT_MEMORYSIZE * 8 + "bytes");
        }
        this.stackMemory[variableIndex++] = object;
    }

    public void setMapValue(Object mapValue, MemoryConstant key) {
        this.mapMemory.put(key.hashCode(), mapValue);
    }

    public Object getMapValue(MemoryConstant key) {
        return this.mapMemory.get(key.hashCode());
    }

    public boolean hasMapValue(MemoryConstant constant) {
        return this.mapMemory.containsKey(constant.hashCode());
    }

    public void removeMapValue(MemoryConstant memoryConstant) {
        this.mapMemory.remove(memoryConstant.hashCode());
    }
}






