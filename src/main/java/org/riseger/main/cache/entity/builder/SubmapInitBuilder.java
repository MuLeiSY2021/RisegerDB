package org.riseger.main.cache.entity.builder;

import lombok.Data;
import org.riseger.main.Constant;
import org.riseger.main.cache.entity.component.Database_c;
import org.riseger.main.cache.entity.component.MapDB_c;
import org.riseger.main.cache.manager.ElementManager;
import org.riseger.protoctl.struct.config.Config;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class SubmapInitBuilder implements MapBuilder {
    String name;

    Map<String, Config> configs;

    Database_c database;

    ElementManager em;

    List<File> smp_layer = new LinkedList<>();

    List<File> md_layer = new LinkedList<>();

    public MapDB_c build() {
        MapDB_c map = new MapDB_c(null, configs, name, database, em);
        map.initAllSmp(smp_layer);
        map.initAllMd(md_layer);
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
