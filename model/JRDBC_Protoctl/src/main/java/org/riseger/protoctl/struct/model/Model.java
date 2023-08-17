package org.riseger.protoctl.struct.model;

import lombok.Data;
import org.riseger.protoctl.struct.config.Option;

import java.util.HashMap;
import java.util.Map;

@Data
public class Model {
    protected String name;

    protected String parent;

    protected Map<String, Type> parameters = new HashMap<String, Type>();

    protected Map<Option, String> configs = new HashMap<>();

    public Model(String name, Database database, String parent, Map<String, Type> parameters, Map<Option, String> configs) {
        this.name = name;
        database.addModel(this);
        this.parent = parent;
        this.parameters = parameters;
        this.configs = configs;
    }

    protected Model(String name) {
        this.name = name;
    }

    public void addParameter(String name, Type type) {
        this.parameters.put(name, type);
    }

}
