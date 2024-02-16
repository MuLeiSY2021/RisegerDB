package org.riseger.main.system.cache.manager;

import org.riseger.main.system.cache.Entity;
import org.riseger.main.system.cache.component.Model_c;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParentModelManager extends Entity {
    private final Map<String, Model_c> parentModel = new ConcurrentHashMap<>();
    public ParentModelManager INSTANCE = new ParentModelManager();

    public Model_c getModel(String name) {
        return parentModel.get(name);
    }

    public void addModel(String name, Model_c model) {
        parentModel.put(name, model);
    }
}
