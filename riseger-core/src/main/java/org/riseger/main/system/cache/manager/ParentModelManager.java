package org.riseger.main.system.cache.manager;

import org.riseger.main.system.cache.component.Model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParentModelManager {
    private final Map<String, Model> parentModel = new ConcurrentHashMap<>();
    public ParentModelManager INSTANCE = new ParentModelManager();

    public Model getModel(String name) {
        return parentModel.get(name);
    }

    public void addModel(String name, Model model) {
        parentModel.put(name, model);
    }
}
