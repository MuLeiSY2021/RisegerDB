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

    public int getIntValue() {
        return Integer.parseInt(value);
    }

    public double getDoubleValue() {
        return Double.parseDouble(value);
    }
}
