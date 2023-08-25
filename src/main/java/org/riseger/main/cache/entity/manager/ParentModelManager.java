package org.riseger.main.cache.entity.manager;

import org.riseger.main.cache.entity.component.db.Model_c;

import java.util.HashMap;
import java.util.Map;

public class ParentModelManager {
    public ParentModelManager INSTANCE = new ParentModelManager();

    private final Map<String, Model_c> parentModel = new HashMap<>();

    public Model_c getModel(String name) {
        return parentModel.get(name);
    }

    public void addModel(String name, Model_c model) {
        parentModel.put(name, model);
    }
}
