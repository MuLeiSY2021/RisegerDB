package org.riseger.main.system.cache.manager;

import org.riseger.main.system.cache.HolisticStorageEntity;
import org.riseger.main.system.cache.HolisticStorageEntity_i;
import org.riseger.main.system.cache.LockableEntity;
import org.riseger.main.system.cache.component.Config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigManager extends LockableEntity implements HolisticStorageEntity_i {
    private final Map<String, Config> configs = new ConcurrentHashMap<>();

    private final HolisticStorageEntity entity = new HolisticStorageEntity();

    public ConfigManager() {
    }

    public ConfigManager(Map<String, Config> configs) {
        this.configs.putAll(configs);
    }

    public void addConfig(Config config, String value) {
        super.write();
        configs.put(value, config);
        super.unwrite();
    }

    public void addAll(Map<String, Config> configManager) {
        super.write();
        configs.putAll(configManager);
        super.unwrite();
    }

    public int getInt(String config) {
        super.read();
        int value = configs.get(config).getIntValue();
        super.unread();
        return value;
    }

    public double getDouble(String config) {
        super.read();
        double value = configs.get(config).getDoubleValue();
        super.unread();
        return value;
    }

    public double getThreshold() {
        super.read();
        double threshold = this.getDouble("threshold");
        super.unread();
        return threshold;
    }

    @Override
    public void changeEntity() {
        entity.changeEntity();
    }

    @Override
    public void resetChanged() {
        entity.resetChanged();
    }

    @Override
    public boolean isChanged() {
        return entity.isChanged();
    }

    public void addAll(ConfigManager configManager) {
        this.configs.putAll(configManager.configs);
    }
}
