package org.riseger.protocol.struct.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Model_p {
    protected String name;

    protected String parent;

    protected Map<String, Type> parameters = new HashMap<>();


    public Model_p(String name, Database database, String parent, Map<String, Type> parameters) {
        this.name = name;
        database.addModel(this);
        this.parent = parent;
        this.parameters = parameters;
    }

    protected Model_p(String name) {
        this.name = name;
    }

    public void addParameter(String name, Type type) {
        this.parameters.put(name, type);
    }

}
