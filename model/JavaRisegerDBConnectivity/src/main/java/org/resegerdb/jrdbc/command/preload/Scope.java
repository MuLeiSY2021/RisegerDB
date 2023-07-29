package org.resegerdb.jrdbc.command.preload;

import lombok.Data;

@Data
public class Scope {
    private String name;

    private Scope parent;


    private Database database;


    public Scope(String name, Database database) {
        this.name = name;
        this.database = database;
    }

    public Scope(String name, Scope parent, Database database) {
        this.name = name;
        this.parent = parent;
        this.database = database;
    }
}
