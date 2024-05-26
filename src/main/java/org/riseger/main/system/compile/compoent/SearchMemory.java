package org.riseger.main.system.compile.compoent;

import org.riseger.main.constant.Config;

import java.util.HashMap;
import java.util.Map;

public class SearchMemory {

    final Object[] stackMemory = new Object[Config.DEFAULT_MEMORYSIZE];

    final Map<Integer, Object> mapMemory = new HashMap<>();

    private final SearchSession searchSession;

    private int variableIndex;

    public SearchMemory(SearchSession searchSession) {
        this.searchSession = searchSession;
    }

    public Object poll() {
        if (variableIndex == 0) {
            throw new IndexOutOfBoundsException("Not enough variable");
        }
        return this.stackMemory[--variableIndex];
    }

    public void push(Object object) {
        if (variableIndex == Config.DEFAULT_MEMORYSIZE) {
            throw new IndexOutOfBoundsException("Stack over flow size:" + Config.DEFAULT_MEMORYSIZE * 8 + "bytes");
        }
        this.stackMemory[variableIndex++] = object;
    }

    public void setMap(Object mapValue, MemoryConstant key) {
        this.mapMemory.put(key.hashCode(), mapValue);
    }

    public Object get(MemoryConstant key) {
        return this.mapMemory.get(key.hashCode());
    }

    public boolean hasMap(MemoryConstant constant) {
        return this.mapMemory.containsKey(constant.hashCode());
    }

    public void removeMapValue(MemoryConstant memoryConstant) {
        this.mapMemory.remove(memoryConstant.hashCode());
    }

    public int getSessionId() {
        return searchSession.getSessionId();
    }
}






