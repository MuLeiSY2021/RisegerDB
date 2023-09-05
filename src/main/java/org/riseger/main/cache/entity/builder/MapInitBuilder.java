package org.riseger.main.cache.entity.builder;

import lombok.Data;
import org.riseger.main.cache.entity.component.Database_c;
import org.riseger.main.cache.entity.component.MapDB_c;
import org.riseger.protoctl.struct.config.Config;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class MapInitBuilder implements MapBuilder {
    Map<String, Config> configs;

    String name;

    Database_c database;

    List<SubmapInitBuilder> submaps = new LinkedList<>();

    public MapInitBuilder() {

    }

    public MapDB_c build() throws Exception {
        MapDB_c map = new MapDB_c(null, configs, name, database);
        map.addAll(submaps);
        return map;
    }

    public void addMap(SubmapInitBuilder subMap) {
        submaps.add(subMap);
    }
}
