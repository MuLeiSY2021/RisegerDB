package org.riseger.main.cache.manager;

import org.riseger.main.cache.entity.component.db.Database_c;
import org.riseger.main.cache.entity.component.db.Model_c;
import org.riseger.protoctl.struct.entity.Element;
import org.riseger.protoctl.struct.entity.Model;

import java.util.HashMap;
import java.util.Map;

public class ModelManager {
    private transient final Database_c database;
    private final Map<String, Model_c> models = new HashMap<>();

    public ModelManager(Database_c database) {
        this.database = database;
    }

    public void addModel(Model model) {
        models.put(model.getName(), new Model_c(model));
    }

    public Model_c getModel(String name) {
        return models.get(name);
    }

    //TODO: 准备写个适配查询接口
    public boolean isSuiteModel(Element element) {
        return false;
    }
}
