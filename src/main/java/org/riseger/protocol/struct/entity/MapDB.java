package org.riseger.protocol.struct.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class MapDB {
    private String name;

    private List<Submap> submaps = new LinkedList<>();

    private List<Element_p> elements = new LinkedList<>();

    private Map<String, String> configs = new HashMap<>();

    public MapDB(String name, Database database, Map<String, String> configs) {
        this.name = name;
        database.addMap(this);
        this.configs = configs;
    }

    protected MapDB(String name) {
        this.name = name;
    }

    public void addElement(Element_p element) {
        elements.add(element);
    }

    public void addSubMap(Submap map) {
        submaps.add(map);
    }

    public String getConfig(String key) {
        return configs.get(key);
    }

    public void setNodeSize(int size) {
        this.configs.put("nodeSize", String.valueOf(size));
    }

    public void setThreshold(double threshold) {
        this.configs.put("threshold", String.valueOf(threshold));
    }
}

