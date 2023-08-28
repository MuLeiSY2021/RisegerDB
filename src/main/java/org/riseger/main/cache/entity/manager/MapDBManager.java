package org.riseger.main.cache.entity.manager;

import org.riseger.main.cache.entity.component.db.Database_c;
import org.riseger.main.cache.entity.component.map.MapDB_c;
import org.riseger.protoctl.struct.entity.MapDB;

import java.util.HashMap;
import java.util.Map;

public class MapDBManager {
    private final Map<String, MapDB_c> maps = new HashMap<>();

    private final Database_c database;

    public MapDBManager(Database_c database) {
        this.database = database;
    }

    public MapDB_c getMap(String name) {
        return maps.get(name);
    }

    public void addMap(int nodeSize, double threshold, MapDB map) {
        maps.put(map.getName(), MapDB_c.mapBuilder(map,nodeSize,threshold,this));
    }

    public Database_c getDatabase() {
        return database;
    }
}
