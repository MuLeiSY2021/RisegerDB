package org.riseger.main.cache.entity.component;

import lombok.Data;
import org.riseger.protoctl.struct.entity.Model;
import org.riseger.protoctl.struct.entity.Type;

import java.util.HashMap;
import java.util.Map;

@Data
public class Model_c {
    protected final String name;
    protected final Map<String, Type> parameters = new HashMap<>();
    protected String parent;

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

    public Type getType(String value) {
        return parameters.get(value);
    }
}
