package org.riseger.protoctl.struct.model;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class MapDB {
    private String name;

    private List<Submap> submaps = new LinkedList<>();

    private List<Element> elements = new LinkedList<>();

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
}

