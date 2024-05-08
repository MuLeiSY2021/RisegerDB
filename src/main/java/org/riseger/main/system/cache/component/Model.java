package org.riseger.main.system.cache.component;

import lombok.Data;
import org.riseger.protocol.struct.entity.Model_p;
import org.riseger.protocol.struct.entity.Type;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class Model {
    protected final String name;
    protected final Map<String, Type> parameters = new ConcurrentHashMap<>();
    protected String parent;

    public Model(Model_p model) {
        this.name = model.getName();
        this.parent = model.getParent();
        this.parameters.putAll(model.getParameters());

    }

    protected Model(String name) {
        this.name = name;
    }

    public void addParameter(String name, Type type) {
        this.parameters.put(name, type);
    }

    public Type getType(String value) {
        return parameters.get(value);
    }

    public boolean isLegal(Element element) {
        for (Map.Entry<String, Object> entry : element.getAttributes().entrySet()) {
            if (!this.parameters.get(entry.getKey()).isConvertable(entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    public boolean isObjectLegal(String key, Object value) {
        return parameters.get(key).isConvertable(value);
    }

    public boolean isKey(String attribute) {
        return this.parameters.get(attribute).isKey();
    }
}
