package org.riseger.main.cache.entity.component;

import lombok.Data;
import org.riseger.main.cache.manager.MapDBManager;
import org.riseger.main.cache.manager.ModelManager;
import org.riseger.main.cache.other.Status;
import org.riseger.protoctl.struct.config.Config;
import org.riseger.protoctl.struct.entity.MapDB;
import org.riseger.protoctl.struct.entity.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Database_c {
    private String name;

    private Status status = Status.LOADING;

    private MapDBManager mapDBManager = new MapDBManager(this);

    private Map<String, Config> configs = new HashMap<>();

    private ModelManager modelManager = new ModelManager(this);

    public Database_c(String name) {
        this.name = name;
    }

    public void addConfig(Config config, String value) {
        configs.put(value, config);
    }

    public void addModel(Model model) {
        modelManager.addModel(model);
    }

    public void addMap(MapDB map) {
        mapDBManager.addMap(Integer.parseInt(map.getConfig("nodeSize")),
                Double.parseDouble(map.getConfig("threshold")),
                map);
    }

    public void active() {
        this.status = Status.ACTIVE;
    }

    public List<MapDB_c> getMapDBs() {
        return mapDBManager.getMapDBs();
    }

    public void initMap(MapDB_c map) {
        mapDBManager.initMap(map);
    }

    public MapDB_c getMap(String map) {
        return this.mapDBManager.getMap(map);
    }

    public List<Model_c> getModels() {
        return this.modelManager.getModels();
    }
}
