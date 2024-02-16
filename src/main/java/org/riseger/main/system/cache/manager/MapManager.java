package org.riseger.main.system.cache.manager;

import lombok.Getter;
import org.riseger.main.system.cache.CacheEntity;
import org.riseger.main.system.cache.component.Database_c;
import org.riseger.main.system.cache.component.Map_c;
import org.riseger.protoctl.struct.entity.Element;
import org.riseger.protoctl.struct.entity.MapDB;
import org.riseger.protoctl.struct.entity.Submap;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapManager extends CacheEntity {
    private final Map<String, Map_c> maps = new ConcurrentHashMap<>();

    @Getter
    private final Database_c parent;

    public MapManager(Database_c parent) {
        this.parent = parent;
    }

    public Map_c getMap(String name) {
        return maps.get(name);
    }

    public void addMap(int nodeSize, double threshold, MapDB map) {
        Map_c mapDB = new Map_c(nodeSize, threshold, map.getName(), parent);
        for (Element e : map.getElements()) {
            mapDB.preloadElement(e);
        }

        for (Submap submap : map.getSubmaps()) {
            mapDB.preloadSubmap(submap);
        }
        maps.put(map.getName(), mapDB);
    }

    public Map_c[] toList() {
        return maps.values().toArray(new Map_c[0]);
    }

    public void initMap(Map_c map) {
        this.maps.put(map.getName(), map);
    }

    public List<Map_c> getMapDBs() {
        return new LinkedList<>(maps.values());
    }
}
