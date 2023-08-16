package org.riseger.jrdbc.struct.model;

import lombok.Data;

import java.util.List;

@Data
public class MapDB {
    private String name;

    private List<Submap> submaps;

    private List<Element> elements;

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

