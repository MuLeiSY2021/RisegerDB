package org.riseger.main.cache.entity.component;

import org.riseger.protoctl.struct.entity.Model;
import org.riseger.protoctl.struct.entity.Type;

import java.util.HashMap;
import java.util.Map;

public class Model_c {
    protected String name;

    protected String parent;

    protected Map<String, Type> parameters = new HashMap<String, Type>();

    public Model_c(Model model) {
        this.name = model.getName();
        this.parent = model.getParent();
        this.parameters.putAll(model.getParameters());

    }

    protected Model_c(String name) {
        this.name = name;
    }

    public void addParameter(String name, Type type) {
        this.parameters.put(name, type);
    }
}
