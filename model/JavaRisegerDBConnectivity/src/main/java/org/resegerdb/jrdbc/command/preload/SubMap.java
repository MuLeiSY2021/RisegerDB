package org.resegerdb.jrdbc.command.preload;

import lombok.Data;

import java.util.List;

public class SubMap extends MapDB{
    private Scope scope;

    public SubMap(String name, Scope scope, Database database) {
        super(name, database);
        this.scope = scope;
    }
}

