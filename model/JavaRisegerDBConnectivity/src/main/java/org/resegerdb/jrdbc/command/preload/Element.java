package org.resegerdb.jrdbc.command.preload;

import lombok.Data;

import java.util.Map;

@Data
public class Element {
    private Database database;

    private MapDB map;

    private Model model;

    private Scope scope;

    private Map<String, String> attributes;

    public Element(Database database, MapDB map, Model model, Scope scope) {
        this.database = database;
        this.map = map;
        this.model = model;
        this.scope = scope;
    }

    public void setValue(String name,String value) {
        this.attributes.put(name,value);
    }

}
