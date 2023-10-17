package org.riseger.main.sql.search;

import org.riseger.main.Constant;

import java.util.HashMap;
import java.util.Map;

public class SearchMemory {

    final Object[] staticMemory = new Object[Constant.DEFAULT_MEMORYSIZE];

    final Object[] stackMemory = new Object[Constant.DEFAULT_MEMORYSIZE];

    final Map<Integer, Object> mapMemory = new HashMap<>();

    private int variableIndex;

    public Object getVar() {
        return this.stackMemory[--variableIndex];
    }

    public void setVar(Object object) {
        this.stackMemory[variableIndex++] = object;
    }

    public Object getConstant(int index) {
        return this.staticMemory[index];
    }

    public void setConstant(Object constant, int index) {
        this.staticMemory[index] = constant;
    }

    public void setMapValue(Object mapValue, MemoryConstant key) {
        this.mapMemory.put(key.hashCode(), mapValue);
    }

    public Object getMapValue(MemoryConstant key) {
        return this.mapMemory.get(key.hashCode());
    }
}






