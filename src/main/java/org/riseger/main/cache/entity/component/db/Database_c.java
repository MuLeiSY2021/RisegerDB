package org.riseger.main.cache.entity.component.db;

import com.google.gson.Gson;
import lombok.Data;
import org.riseger.main.cache.entity.builder.MapBuilder;
import org.riseger.main.cache.manager.MapDBManager;
import org.riseger.main.cache.manager.ModelManager;
import org.riseger.main.cache.other.Status;
import org.riseger.protoctl.struct.config.Config;
import org.riseger.protoctl.struct.entity.MapDB;
import org.riseger.protoctl.struct.entity.Model;

import java.util.HashMap;
import java.util.Map;

@Data
public class Database_c {
    String name;

    Status status = Status.LOADING;

    MapDBManager maps = new MapDBManager(this);

    Map<String,Config> configs = new HashMap<>();

    ModelManager models = new ModelManager(this);

    public Database_c(String name) {
        this.name = name;
    }

    public void addConfig(Config config,String value) {
        configs.put(value, config);
    }

    public void addModel(Model model) {
        models.addModel(model);
    }

    public void addMap(MapDB map) {
        maps.addMap(map.getConfig("nodeSize").getIntValue(),
                map.getConfig("threshold").getDoubleValue(),
                map);
    }

    public boolean isLoading() {
        return this.status == Status.LOADING;
    }

    public void active() {
        this.status = Status.ACTIVE;
    }

    public MapBuilder newMap(String map_name) {
        return new MapBuilder(map_name);
    }

    public String configToFile() {
        return new Gson().toJson(configs);
    }
}
