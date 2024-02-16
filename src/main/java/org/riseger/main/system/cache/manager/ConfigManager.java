package org.riseger.main.system.cache.manager;

import org.riseger.main.system.cache.CacheEntity;
import org.riseger.main.system.cache.component.Config_c;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigManager extends CacheEntity {
    private final Map<String, Config_c> configs = new ConcurrentHashMap<>();

    public ConfigManager() {
    }

    public ConfigManager(Map<String, Config_c> configs) {
        this.configs.putAll(configs);
    }

    public void addConfig(Config_c config, String value) {
        configs.put(value, config);
    }

    public void addAll(Map<String, Config_c> configManager) {
        configs.putAll(configManager);
    }

    public int getInt(String config) {
        return configs.get(config).getIntValue();
    }

    public double geDouble(String config) {
        return configs.get(config).getDoubleValue();
    }
}
