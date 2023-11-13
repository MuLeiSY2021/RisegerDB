package org.riseger.main.cache.manager;

import lombok.Getter;
import org.riseger.main.cache.entity.builder.MapPreloadBuilder;
import org.riseger.main.cache.entity.component.Database_c;
import org.riseger.main.cache.entity.component.MapDB_c;
import org.riseger.protoctl.struct.entity.MapDB;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MapDBManager {
    private final ConcurrentMap<String, MapDB_c> maps = new ConcurrentHashMap<>();

    @Getter
    private final Database_c parent;

    public MapDBManager(Database_c parent) {
        this.parent = parent;
    }

    public MapDB_c getMap(String name) {
        return maps.get(name);
    }

    public void addMap(int nodeSize, double threshold, MapDB map) {
        MapPreloadBuilder builder = new MapPreloadBuilder();
        builder.setMap(map);
        builder.setNodeSize(nodeSize);
        builder.setThreshold(threshold);
        builder.setDatabase(parent);
        maps.put(map.getName(), builder.build());
    }

    public MapDB_c[] toList() {
        return maps.values().toArray(new MapDB_c[0]);
    }

    public void initMap(MapDB_c map) {
        this.maps.put(map.getName(), map);
    }

    public List<MapDB_c> getMapDBs() {
        return new LinkedList<>(maps.values());
    }
}
