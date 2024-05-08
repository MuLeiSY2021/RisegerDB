package org.riseger.main.system.cache.manager;

import lombok.Getter;
import org.riseger.main.system.cache.HolisticStorageEntity;
import org.riseger.main.system.cache.component.Database;
import org.riseger.main.system.cache.component.GeoMap;
import org.riseger.protocol.struct.entity.Element_p;
import org.riseger.protocol.struct.entity.MapDB;
import org.riseger.protocol.struct.entity.Submap;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapManager extends HolisticStorageEntity {
    private final Map<String, GeoMap> maps = new ConcurrentHashMap<>();

    @Getter
    private final Database parent;

    public MapManager(Database parent) {
        this.parent = parent;
    }

    public GeoMap getMap(String name) {
        return maps.get(name);
    }

    public void addMap(int nodeSize, double threshold, MapDB map) {
        GeoMap mapDB = new GeoMap(nodeSize, threshold, map.getName(), parent);
        for (Element_p e : map.getElements()) {
            mapDB.preloadElement(e);
        }

        for (Submap submap : map.getSubmaps()) {
            mapDB.preloadSubmap(submap);
        }
        maps.put(map.getName(), mapDB);
    }

    public GeoMap[] toList() {
        return maps.values().toArray(new GeoMap[0]);
    }

    public void initMap(GeoMap map) {
        this.maps.put(map.getName(), map);
    }

    public List<GeoMap> getMaps() {
        return new LinkedList<>(maps.values());
    }
}
