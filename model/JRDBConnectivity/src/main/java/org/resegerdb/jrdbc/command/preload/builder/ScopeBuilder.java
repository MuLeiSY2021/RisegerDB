package org.resegerdb.jrdbc.command.preload.builder;

import org.riseger.protoctl.struct.model.Database;
import org.riseger.protoctl.struct.model.Scope;

public class ScopeBuilder {
    private final Database database;

    private Scope parent;

    private String name;

    private Scope scope;

    public ScopeBuilder(Database database) {
        this.database = database;
    }

    public ScopeBuilder(Database database, Scope parent) {
        this.database = database;
        this.parent = parent;
    }

    public ScopeBuilder name(String name) {
        this.name = name;
        return this;
    }

    public Scope build() {
        if (parent == null) {
            this.scope = new Scope(name, database);
        } else {
            this.scope = new Scope(name, parent);
        }
        return this.scope;
    }

    public ScopeBuilder child() {
        if (this.scope != null) {
            return new ScopeBuilder(database, this.scope);
        } else {
            throw new NullPointerException();
        }
    }


}
