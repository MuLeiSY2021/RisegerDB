package org.riseger.main.system.cache.builder;

import lombok.Data;
import org.riseger.main.constant.Constant;
import org.riseger.main.system.cache.component.Config_c;
import org.riseger.main.system.cache.component.Database_c;
import org.riseger.main.system.cache.component.Map_c;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class MapInitBuilder implements MapBuilder {
    Map<String, Config_c> configs;

    String name;

    Database_c database;

    List<SubmapInitBuilder> submaps = new LinkedList<>();

    List<File> smp_layer = new LinkedList<>();

    List<File> md_layer = new LinkedList<>();

    public MapInitBuilder() {

    }

    public Map_c build() {
        Map_c map = new Map_c(configs, name, database);
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
