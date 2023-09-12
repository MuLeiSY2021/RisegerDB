package org.resegerdb.jrdbc.command.search.command;

import org.resegerdb.jrdbc.driver.session.SearchSession;

public class USE extends SQL {
    private String database;

    private String scope;

    private String map;

    public USE(SearchSession session) {
        super(session);
    }


    public USE useMap(String map) {
        this.map = map;
        return this;
    }

    public USE useDatabase(String database) {
        this.database = database;
        return this;
    }

    public USE useScope(String scope) {
        this.scope = scope;
        return this;
    }

    public SEARCH search() {
        warp();
        return new SEARCH(this.session);
    }

}
