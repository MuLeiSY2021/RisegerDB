package org.riseger.main.cache.entity.builder;

import lombok.Data;
import org.riseger.main.cache.entity.component.Database_c;
import org.riseger.main.cache.entity.component.MapDB_c;
import org.riseger.main.cache.manager.ElementManager;
import org.riseger.protoctl.struct.config.Config;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class SubmapInitBuilder implements MapBuilder {
    String name;

    Map<String, Config> configs;


    Database_c database;

    ElementManager em;

    List<SubmapInitBuilder> submaps = new LinkedList<>();

    List<File> layers = new LinkedList<>();

    public MapDB_c build() throws Exception {
        MapDB_c map = new MapDB_c(null, configs, name, database, em);
        map.addAll(submaps);
        map.addAllLayer(layers);
        return map;
    }

    public void addMap(SubmapInitBuilder subMap) {
        submaps.add(subMap);
    }

    public void addLayer(File layer) {
        layers.add(layer);
    }
}
