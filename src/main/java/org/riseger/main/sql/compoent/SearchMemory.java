package org.riseger.main.sql.compoent;

import org.riseger.main.Constant;

import java.util.HashMap;
import java.util.Map;

public class SearchMemory {

    final Object[] stackMemory = new Object[Constant.DEFAULT_MEMORYSIZE];

    final Map<Integer, Object> mapMemory = new HashMap<>();

    private int variableIndex;

    public Object getVar() {
        return this.stackMemory[--variableIndex];
    }

    public void setVar(Object object) {
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
}






