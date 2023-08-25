package org.riseger.main.cache.entity.manager;

import org.riseger.main.cache.entity.component.db.Database_c;
import org.riseger.protoctl.struct.config.Config;

import java.util.HashMap;
import java.util.Map;

public class DBConfigManager {
    private Database_c database;

    private Map<Config,String> configMap = new HashMap<>();

    public DBConfigManager(Database_c database) {
        this.database = database;
    }

    public void addConfig(Config config,String value) {
        configMap.put(config,value);
    }
}
