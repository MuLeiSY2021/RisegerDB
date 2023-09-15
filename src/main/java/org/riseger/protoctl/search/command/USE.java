package org.riseger.protoctl.search.command;

import lombok.Getter;
import org.riseger.protoctl.search.function.type.RECTANGLE_FUNCTIONBLE;

import java.util.LinkedList;
import java.util.List;

@Getter
public class USE extends SQL {
    private String database;

    private RECTANGLE_FUNCTIONBLE scope;

    private String map;

    private SEARCH search;

    private final List<String> models = new LinkedList<>();


    public USE useMap(String map) {
        this.map = map;
        return this;
    }

    public USE useDatabase(String database) {
        this.database = database;
        return this;
    }

    public USE useScope(RECTANGLE_FUNCTIONBLE rectangle) {
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
