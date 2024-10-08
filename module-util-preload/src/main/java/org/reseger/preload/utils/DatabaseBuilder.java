package org.reseger.preload.utils;

import org.riseger.protocol.struct.entity.Database;

public class DatabaseBuilder {
    private final PreloadBuilder parent;

    private Database database;

    private String name;

    public DatabaseBuilder(PreloadBuilder parent) {
        this.parent = parent;
    }

    public MapBuilder buildMap() {
        if (this.database == null) {
            throw new NullPointerException("database is null");
        }
        return new MapBuilder(this.database);
    }

    public ModelBuilder buildModel() {
        if (this.database == null) {
            throw new NullPointerException("database is null");
        }
        return new ModelBuilder(this.database);
    }


    public DatabaseBuilder name(String name) {
        this.name = name;
        return this;
    }

    public void build() {
        this.database = new Database(name);
        this.parent.addDatabase(this.database);
    }
}
