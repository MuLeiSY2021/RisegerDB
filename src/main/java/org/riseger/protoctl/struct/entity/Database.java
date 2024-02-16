package org.riseger.protoctl.struct.entity;

import lombok.Data;
import org.riseger.main.system.cache.component.Config_c;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class Database {
    private String name;

    private Map<Config_c, String> configs = new HashMap<>();

    private List<MapDB> maps = new LinkedList<>();

    private List<Model> models = new LinkedList<>();

    public Database(String name) {
        this.name = name;
    }

    public void addMap(MapDB map) {
        maps.add(map);
    }

    public void addModel(Model model) {
        models.add(model);
    }

    public void addConfig(Config_c config, String value) {
        configs.put(config, value);
    }

}
