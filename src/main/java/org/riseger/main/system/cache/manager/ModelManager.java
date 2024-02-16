package org.riseger.main.system.cache.manager;

import org.riseger.main.system.cache.Entity;
import org.riseger.main.system.cache.component.Database_c;
import org.riseger.main.system.cache.component.Model_c;
import org.riseger.protoctl.struct.entity.Model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModelManager extends Entity {
    private transient final Database_c database;
    private final Map<String, Model_c> models = new ConcurrentHashMap<>();

    public ModelManager(Database_c database) {
        this.database = database;
    }

    public void addModel(Model model) {
        models.put(model.getName(), new Model_c(model));
    }

    public Model_c getModel(String name) {
        return models.get(name);
    }

    public List<Model_c> getModels() {
        return new LinkedList<>(models.values());
    }
}
