package org.riseger.main.system.cache.component;

import lombok.Getter;

public class Config {
    private final String configName;

    @Getter
    private final String value;

    public Config(String configName, String value) {
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
