package org.reseger.preload.utils;

import org.riseger.protocol.struct.entity.Database_p;
import org.riseger.protocol.struct.entity.GeoMap_p;

import java.util.HashMap;
import java.util.Map;

public class MapBuilder {
    private final Database_p database;
    private final Map<String, String> configs = new HashMap<>();
    private GeoMap_p map;
    private String name;


    public MapBuilder(Database_p database) {
        this.database = database;
    }

    public MapBuilder name(String name) {
        this.name = name;
        return this;
    }

    public void build() {
        this.map = new GeoMap_p(name, database, configs);
    }

    public MapBuilder nodeSize(int size) {
        this.configs.put("nodeSize", String.valueOf(size));
        return this;
    }

    public MapBuilder threshold(double threshold) {
        this.configs.put("threshold", String.valueOf(threshold));
        return this;
    }

    public FieldBuilder buildField() {
        return new FieldBuilder(this.map);
    }
}
