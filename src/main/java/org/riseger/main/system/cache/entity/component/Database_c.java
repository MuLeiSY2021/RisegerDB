package org.riseger.main.system.cache.entity.component;

import lombok.Data;
import org.riseger.main.constant.Status;
import org.riseger.main.system.cache.manager.MapDBManager;
import org.riseger.main.system.cache.manager.ModelManager;
import org.riseger.protoctl.struct.config.Config;
import org.riseger.protoctl.struct.entity.MapDB;
import org.riseger.protoctl.struct.entity.Model;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class Database_c {
    private String name;

    private Status status = Status.LOADING;

    private MapDBManager mapDBManager = new MapDBManager(this);

    private Map<String, Config> configs = new ConcurrentHashMap<>();

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
