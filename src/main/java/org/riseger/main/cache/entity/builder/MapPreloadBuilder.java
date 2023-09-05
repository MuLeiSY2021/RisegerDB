package org.riseger.main.cache.entity.builder;

import lombok.Data;
import org.riseger.main.cache.entity.component.Database_c;
import org.riseger.main.cache.entity.component.MapDB_c;
import org.riseger.protoctl.struct.entity.Element;
import org.riseger.protoctl.struct.entity.MapDB;
import org.riseger.protoctl.struct.entity.Submap;

@Data
public class MapPreloadBuilder implements MapBuilder {
    MapDB map;

    int nodeSize;

    double threshold;

    Database_c database;

    public MapPreloadBuilder() {

    }

    public MapDB_c build() {
        MapDB_c mapDB = new MapDB_c(null, nodeSize, threshold, map.getName(), database);
        for (Submap submap : map.getSubmaps()) {
            mapDB.preloadSubmap(submap);
        }

        for (Element e : map.getElements()) {
            mapDB.preloadElement(e);
        }
        return mapDB;
    }

}
