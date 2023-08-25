package org.riseger.main.cache.entity.manager;

import org.riseger.main.cache.entity.component.db.Model_c;
import org.riseger.protoctl.struct.entity.Element;
import org.riseger.protoctl.struct.entity.Model;

import java.util.HashMap;
import java.util.Map;

public class ModelManager {
    private Map<String, Model_c> models = new HashMap<String, Model_c>();

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
