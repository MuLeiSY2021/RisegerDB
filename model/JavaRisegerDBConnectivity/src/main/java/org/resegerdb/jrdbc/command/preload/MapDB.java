package org.resegerdb.jrdbc.command.preload;

import lombok.Data;

import java.util.List;

@Data
public class MapDB {
    private String name;

    private Database database;


    private List<SubMap> subMaps;

    private List<Element> elements;



    public MapDB(String name, Database database) {
        this.name = name;
        this.database = database;
    }

    public MapDB(String name, Scope scope, Database database) {
        this.name = name;
        this.database = database;
    }

    public void addElement(Element element) {
        elements.add(element);
    }

    public void addSubMap(SubMap map) {
        subMaps.add(map);
    }
}

