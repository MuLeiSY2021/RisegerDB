package org.resegerdb.jrdbc.command.preload.builder;

import org.resegerdb.jrdbc.driver.session.PreloadSession;
import org.riseger.protoctl.struct.model.Database;

public class DatabaseBuilder {
    private final PreloadSession session;

    private Database database;

    private String name;

    public DatabaseBuilder(PreloadSession session) {
        this.session = session;
    }

    public MapBuilder buildMap() {
        if (this.database == null) {
            throw new NullPointerException("database is null");
        }
        return new MapBuilder(this.database);
    }

    public ScopeBuilder buildScope() {
        if (this.database == null) {
            throw new NullPointerException("database is null");
        }
        return new ScopeBuilder(this.database);
    }

    public ModelBuilder buildModel() {
        if (this.database == null) {
            throw new NullPointerException("database is null");
        }
        return new ModelBuilder(this.database);
    }


    public DatabaseBuilder name(String name) {
        this.name = name;
        return this;
    }

    public DatabaseBuilder build() {
        this.database = new Database(name);
        this.session.addDatabase(this.database);
        return this;
    }
}
