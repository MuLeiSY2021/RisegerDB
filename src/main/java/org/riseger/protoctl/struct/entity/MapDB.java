package org.riseger.protoctl.struct.entity;

import lombok.Data;
import org.riseger.protoctl.struct.config.Config;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class MapDB {
    private String name;

    private List<Submap> submaps = new LinkedList<>();

    private List<Element> elements = new LinkedList<>();

    private Map<String,Config> configs = new HashMap<>();

    public MapDB(String name, Database database) {
        this.name = name;
        database.addMap(this);
    }

    protected MapDB(String name) {
        this.name = name;
    }

    public void addElement(Element element) {
        elements.add(element);
    }

    public void addSubMap(Submap map) {
        submaps.add(map);
    }

    public Config getConfig(String key) {
        return configs.get(key);
    }
}

