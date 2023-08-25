package org.riseger.main.cache.entity.component.map;

import org.riseger.main.cache.entity.component.mbr.MBRectangle_c;
import org.riseger.main.cache.entity.manager.LayerManager;
import org.riseger.protoctl.struct.config.Config;
import org.riseger.protoctl.struct.entity.Element;
import org.riseger.protoctl.struct.entity.MapDB;
import org.riseger.protoctl.struct.entity.Submap;

import java.util.HashMap;
import java.util.Map;

public class MapDB_c extends MBRectangle_c {
    private String name;

    private final LayerManager layers;

    public MapDB_c(Map<String, String> map, double threshold, String name) {
        super(map, threshold);
        this.name = name;
        this.layers = new LayerManager(this);
    }

    public static MapDB_c mapBuilder(MapDB map, Config thresholdConfig) {
        double thr = Double.parseDouble(thresholdConfig.getValue());
        MapDB_c mapDB = new MapDB_c(null, thr, map.getName());
        for (Submap submap:map.getSubmaps()) {
            mapDB.createSubmap(submap);
        }

        for (Element e:map.getElements()) {
            mapDB.addElement(e);
        }
    }

    public void createSubmap(Submap map) {
        layers.addSubmap(map);
    }

    public void addElement(Element e) {
        layers.addElement(e);
    }

    public void updateBroundary() {

    }
}
