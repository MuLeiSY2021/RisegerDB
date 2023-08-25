package org.riseger.protoctl.struct.config;

public class Config {
    private final String configName;

    private String value;

    public Config(String configName, String value) {
        this.configName = configName;
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
