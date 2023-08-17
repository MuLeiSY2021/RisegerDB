package org.resegerdb.jrdbc.command.preload.builder;

import org.riseger.protoctl.struct.model.Database;
import org.riseger.protoctl.struct.model.MapDB;

public class MapBuilder {
    private final Database database;

    private MapDB map;

    private String name;

    public MapBuilder(Database database) {
        this.database = database;
    }

    public MapBuilder name(String name) {
        this.name = name;
        return this;
    }

    public MapBuilder build() {
        this.map = new MapDB(name, database);
        return this;
    }


    public SubmapBuilder buildSubMap() {
        if (this.map == null) {
            throw new NullPointerException("map is null");
        }
        return new SubmapBuilder(this.map);
    }
}
