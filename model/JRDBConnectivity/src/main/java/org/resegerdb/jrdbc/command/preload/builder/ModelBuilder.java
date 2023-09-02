package org.resegerdb.jrdbc.command.preload.builder;

import org.riseger.protoctl.struct.config.Option;
import org.riseger.protoctl.struct.entity.Database;
import org.riseger.protoctl.struct.entity.Model;
import org.riseger.protoctl.struct.entity.Type;

import java.util.HashMap;
import java.util.Map;

public class ModelBuilder {
    private final Database database;

    private String name;

    private String parent;

    protected Map<String, Type> parameters = new HashMap<>();

    protected Map<Option, String> configs = new HashMap<>();

    private Model model;

    public ModelBuilder(Database database) {
        this.database = database;
    }

    public ModelBuilder name(String name) {
        this.name = name;
        return this;
    }

    public Model build() {
        this.model = new Model(name, database, parent, this.parameters);
        return this.model;
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
