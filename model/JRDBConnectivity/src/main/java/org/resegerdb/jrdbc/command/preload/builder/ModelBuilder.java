package org.resegerdb.jrdbc.command.preload.builder;

import org.resegerdb.jrdbc.struct.model.Database;
import org.resegerdb.jrdbc.struct.model.Model;
import org.resegerdb.jrdbc.struct.model.ParentModel;
import org.resegerdb.jrdbc.struct.config.Option;
import org.resegerdb.jrdbc.struct.model.Type;

import java.util.HashMap;
import java.util.Map;

public class ModelBuilder {
    private final Database database;

    private String name;

    protected ParentModel parent;

    protected Map<String, Type> parameters = new HashMap<String, Type>();

    protected Map<Option,String> configs = new HashMap<Option, String>();

    private Model model;

    public ModelBuilder(Database database) {
        this.database = database;
    }

    public ModelBuilder name(String name) {
        this.name = name;
        return this;
    }

    public Model build() {
        this.model = new Model(name, database,parent,this.parameters,this.configs);
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
}
