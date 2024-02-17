package org.riseger.main.system.cache.component;

import lombok.Getter;
import org.riseger.main.system.cache.HolisticStorageEntity;

public class Config_c extends HolisticStorageEntity {
    private final String configName;

    @Getter
    private final String value;

    public Config_c(String configName, String value) {
        this.configName = configName;
        this.value = value;
    }

    public int getIntValue() {
        return Integer.parseInt(value);
    }

    public double getDoubleValue() {
        return Double.parseDouble(value);
    }
}
