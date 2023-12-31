package org.riseger.main.cache.manager;

import com.google.gson.Gson;
import org.riseger.main.cache.entity.component.Database_c;
import org.riseger.protoctl.struct.config.Config;

import java.util.HashMap;
import java.util.Map;

public class ConfigDBManager {
    private final Database_c database;

    private final Map<Config, String> configMap = new HashMap<>();

    public ConfigDBManager(Database_c database) {
        this.database = database;
    }

    public void addConfig(Config config, String value) {
        configMap.put(config, value);
    }

    public String toFile() {
        return new Gson().toJson(this);
    }
}
