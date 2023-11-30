package org.riseger.main.system.cache.manager;

import org.riseger.main.system.cache.entity.component.Model_c;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParentModelManager {
    private final Map<String, Model_c> parentModel = new ConcurrentHashMap<>();
    public ParentModelManager INSTANCE = new ParentModelManager();

    public Model_c getModel(String name) {
        return parentModel.get(name);
    }

    public void addModel(String name, Model_c model) {
        parentModel.put(name, model);
    }
}
