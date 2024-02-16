package org.riseger.main.system.cache.builder;

import lombok.Data;
import org.riseger.main.constant.Constant;
import org.riseger.main.system.cache.component.Config_c;
import org.riseger.main.system.cache.component.Database_c;
import org.riseger.main.system.cache.component.Map_c;
import org.riseger.main.system.cache.manager.ElementManager;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class SubmapInitBuilder implements MapBuilder {
    String name;

    Map<String, Config_c> configs;

    Database_c database;

    ElementManager em;

    List<File> smp_layer = new LinkedList<>();

    List<File> md_layer = new LinkedList<>();

    public Map_c build() {
        Map_c map = new Map_c(configs, name, database, em);
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
