package org.riseger.protoctl.struct.model;

import lombok.Data;
import org.riseger.protoctl.struct.config.Config;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class Database {
    private String name;

    private Map<Config, String> configs = new HashMap<>();

    private List<MapDB> maps = new LinkedList<>();

    private List<Model> models = new LinkedList<>();

    private List<Scope> scopes = new LinkedList<>();

    public Database(String name) {
        this.name = name;
    }

    public void addMap(MapDB map) {
        maps.add(map);
    }

    public void addModel(Model model) {
        models.add(model);
    }

    public void addConfig(Config config, String value) {
        configs.put(config, value);
    }

    public void addScope(Scope scope) {
        scopes.add(scope);
    }
}
