package org.riseger.main.search;

import java.util.HashMap;
import java.util.Map;

public class SearchMemory {

    Map<Integer, Object> universalRegister = new HashMap<>();


    public Object get(int index) {
        return universalRegister.get(index);
    }

    public void set(int index,Object object) {
        this.universalRegister.put(index,object);
    }
}
