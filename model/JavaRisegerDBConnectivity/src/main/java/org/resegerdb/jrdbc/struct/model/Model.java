package org.resegerdb.jrdbc.struct.model;

import lombok.Data;
import org.resegerdb.jrdbc.struct.config.Option;

import java.util.Map;

@Data
public class Model {
    protected String name;

    protected ParentModel parent;

    protected Map<String,Type> parameters;

    protected Map<Option,String> configs;

    public Model(String name) {
        this.name = name;
    }

    public void addParameter(String name, Type type) {
        this.parameters.put(name, type);
    }

    public void addConfig(Option option, String value) {
        this.configs.put(option, value);
    }
}
