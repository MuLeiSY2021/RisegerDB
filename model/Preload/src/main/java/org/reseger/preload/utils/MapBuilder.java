package org.reseger.preload.utils;

import org.riseger.protocol.struct.entity.Database;
import org.riseger.protocol.struct.entity.MapDB;

import java.util.HashMap;
import java.util.Map;

public class MapBuilder {
    private final Database database;
    private final Map<String, String> configs = new HashMap<>();
    private MapDB map;
    private String name;


    public MapBuilder(Database database) {
        this.database = database;
    }

    public MapBuilder name(String name) {
        this.name = name;
        return this;
    }

    public void build() {
        this.map = new MapDB(name, database, configs);
    }


    public SubmapBuilder buildSubMap() {
        if (this.map == null) {
            throw new NullPointerException("map is null");
        }
        return new SubmapBuilder(this.map);
    }

    public MapBuilder nodeSize(int size) {
        this.configs.put("nodeSize", String.valueOf(size));
        return this;
    }

    public MapBuilder threshold(double threshold) {
        this.configs.put("threshold", String.valueOf(threshold));
        return this;
    }
}
