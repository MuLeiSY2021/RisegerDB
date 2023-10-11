package org.riseger.protoctl.struct.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Model {
    protected String name;

    protected String parent;

    protected Map<String, Type> parameters = new HashMap<>();


    public Model(String name, Database database, String parent, Map<String, Type> parameters) {
        this.name = name;
        database.addModel(this);
        this.parent = parent;
        this.parameters = parameters;
    }

    protected Model(String name) {
        this.name = name;
    }

    public void addParameter(String name, Type type) {
        this.parameters.put(name, type);
    }

}
