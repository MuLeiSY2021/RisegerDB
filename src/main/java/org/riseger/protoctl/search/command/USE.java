package org.riseger.protoctl.search.command;

import lombok.Getter;

@Getter
public class USE extends SQL {
    private String database;

    private String scope;

    private String map;

    private SEARCH search;


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
        this.search = new SEARCH();
        return this.search;
    }

}
