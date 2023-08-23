package org.riseger.main.cache.entity.mamager;

import org.riseger.main.cache.entity.element.MapDB_c;
import org.riseger.protoctl.struct.entity.MapDB;

import java.util.HashMap;
import java.util.Map;

public class MapDBManager {
    private Map<String, MapDB_c> maps = new HashMap<>();

    public MapDB_c getMap(String name) {
        return maps.get(name);
    }

    public void addMap(String name, MapDB map) {
        maps.put(name, new MapDB_c(map));
    }
}
