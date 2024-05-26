package org.riseger.main.system.cache.builder;

import lombok.Data;
import org.riseger.main.constant.Constant;
import org.riseger.main.system.cache.component.Database;
import org.riseger.main.system.cache.component.GeoMap;
import org.riseger.main.system.cache.manager.ConfigManager;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Data
public class MapInitBuilder implements MapBuilder {
    ConfigManager configs;

    String name;

    Database database;

    List<SubmapInitBuilder> submaps = new LinkedList<>();

    List<File> smp_layer = new LinkedList<>();

    List<File> md_layer = new LinkedList<>();

    public MapInitBuilder() {

    }

    public GeoMap build() throws IOException {
        GeoMap map = new GeoMap(configs, name, database);
        map.initAllSmp(smp_layer);
        map.initAllMd(md_layer);
        return map;
    }

    public void addLayer(File submap) {
        if (submap.getName().startsWith(Constant.SUBMAP_PREFIX)) {
            smp_layer.add(submap);
        } else {
            md_layer.add(submap);
        }
    }
}
