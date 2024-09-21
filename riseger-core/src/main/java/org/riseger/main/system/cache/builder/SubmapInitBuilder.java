package org.riseger.main.system.cache.builder;

import lombok.Data;
import org.riseger.main.constant.Constant;
import org.riseger.main.system.cache.component.Database;
import org.riseger.main.system.cache.component.GeoMap;
import org.riseger.main.system.cache.manager.ConfigManager;
import org.riseger.main.system.cache.manager.ElementManager;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Data
public class SubmapInitBuilder implements MapBuilder {
    String name;

    ConfigManager configs;

    Database database;

    ElementManager em;

    List<File> smp_layer = new LinkedList<>();

    List<File> md_layer = new LinkedList<>();

    public GeoMap build() throws IOException {
        GeoMap map = new GeoMap(configs, name, database, em);
        map.initAllMd(md_layer);
        map.initAllSmp(smp_layer);
        return map;
    }

    public void addLayer(File layer) {
        if (layer.getName().startsWith(Constant.SUBMAP_PREFIX)) {
            smp_layer.add(layer);
        } else {
            md_layer.add(layer);
        }
    }
}
