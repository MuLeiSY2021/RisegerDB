package org.riseger.main.cache.manager;

import org.riseger.main.cache.entity.component.db.Database_c;
import org.riseger.main.cache.entity.component.map.MapDB_c;
import org.riseger.protoctl.struct.entity.MapDB;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MapDBManager {
    private final ConcurrentMap<String, MapDB_c> maps = new ConcurrentHashMap<>();

    private final Database_c database;

    public MapDBManager(Database_c database) {
        this.database = database;
    }

    public MapDB_c getMap(String name) {
        return maps.get(name);
    }

    public void addMap(int nodeSize, double threshold, MapDB map) {
        maps.put(map.getName(), MapDB_c.mapBuilder(map,nodeSize,threshold,database));
    }

    public Database_c getDatabase() {
        return database;
    }

    public MapDB_c[] toList() {
        return maps.values().toArray(new MapDB_c[0]);
    }
}
