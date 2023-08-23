package org.riseger.main.cache.entity.element;

import org.riseger.main.cache.entity.mamager.LayerManager;
import org.riseger.protoctl.struct.entity.Element;
import org.riseger.protoctl.struct.entity.MapDB;

public class MapDB_c {
    private String name;

    private LayerManager layers;

    public MapDB_c(MapDB map) {
        this.name = map.getName();
        for (Element e:map.getElements()) {
            layers.addElement(e);
        }
    }
}
