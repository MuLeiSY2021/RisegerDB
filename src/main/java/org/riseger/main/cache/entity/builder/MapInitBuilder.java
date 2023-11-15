package org.riseger.main.cache.entity.builder;

import lombok.Data;
import org.riseger.main.Constant;
import org.riseger.main.cache.entity.component.Database_c;
import org.riseger.main.cache.entity.component.MapDB_c;
import org.riseger.protoctl.struct.config.Config;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class MapInitBuilder implements MapBuilder {
    Map<String, Config> configs;

    String name;

    Database_c database;

    List<SubmapInitBuilder> submaps = new LinkedList<>();

    List<File> smp_layer = new LinkedList<>();

    List<File> md_layer = new LinkedList<>();

    public MapInitBuilder() {

    }

    public MapDB_c build() {
        MapDB_c map = new MapDB_c(configs, name, database);
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
