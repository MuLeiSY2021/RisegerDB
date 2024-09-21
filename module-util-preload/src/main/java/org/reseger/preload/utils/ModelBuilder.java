package org.reseger.preload.utils;

import org.riseger.protocol.struct.config.Option;
import org.riseger.protocol.struct.entity.Database;
import org.riseger.protocol.struct.entity.Model_p;
import org.riseger.protocol.struct.entity.Type;

import java.util.HashMap;
import java.util.Map;

public class ModelBuilder {
    protected final Map<String, Type> parameters = new HashMap<>();
    protected final Map<Option, String> configs = new HashMap<>();
    private final Database database;
    private String name;
    private String parent;
    private Model_p model;

    public ModelBuilder(Database database) {
        this.database = database;
    }

    public ModelBuilder name(String name) {
        this.name = name;
        return this;
    }

    public void build() {
        this.model = new Model_p(name, database, parent, this.parameters);
    }

    public ModelBuilder parameter(String name, Type type) {
        this.parameters.put(name, type);
        return this;
    }

    public ModelBuilder config(Option option, String value) {
        this.configs.put(option, value);
        return this;
    }

    public ModelBuilder parent(String parent) {
        this.parent = parent;
        return this;
    }


}
