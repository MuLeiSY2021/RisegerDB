package org.riseger.protocol.compiler.command;

import lombok.Getter;
import org.riseger.protocol.compiler.function.type.rectangleFunctional;

import java.util.LinkedList;
import java.util.List;

@Getter
public class USE extends SQL {
    private final List<String> models = new LinkedList<>();
    private String database;
    private rectangleFunctional scope;
    private String map;
    private SEARCH search;

    public USE useMap(String map) {
        this.map = map;
        return this;
    }

    public USE useDatabase(String database) {
        this.database = database;
        return this;
    }

    public USE useScope(rectangleFunctional rectangle) {
        this.scope = rectangle;
        return this;
    }

    public USE useModel(String model) {
        this.models.add(model);
        return this;
    }

    public SEARCH search() {
        this.search = new SEARCH();
        return this.search;
    }

}
