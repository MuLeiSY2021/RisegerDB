package org.riseger.main.system.cache.entity.builder;

import lombok.Data;
import org.riseger.main.system.cache.entity.component.Database_c;
import org.riseger.main.system.cache.entity.component.MapDB_c;
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
        MapDB_c mapDB = new MapDB_c(nodeSize, threshold, map.getName(), database);
        for (Element e : map.getElements()) {
            mapDB.preloadElement(e);
        }

        for (Submap submap : map.getSubmaps()) {
            mapDB.preloadSubmap(submap);
        }


        return mapDB;
    }

}
