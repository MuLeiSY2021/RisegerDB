package org.riseger.main.cache.manager;

import org.riseger.main.cache.entity.component.Model_c;

import java.util.HashMap;
import java.util.Map;

public class ParentModelManager {
    private final Map<String, Model_c> parentModel = new HashMap<>();
    public ParentModelManager INSTANCE = new ParentModelManager();

    public Model_c getModel(String name) {
        return parentModel.get(name);
    }

    public void addModel(String name, Model_c model) {
        parentModel.put(name, model);
    }
}
