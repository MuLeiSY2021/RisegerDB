package org.riseger.protoctl.struct.entity;

import lombok.Getter;

@Getter
public class Submap extends MapDB {
    private final String scopePath;

    public Submap(String name, String scopePath, MapDB parent) {
        super(name);
        this.scopePath = scopePath;
        parent.addSubMap(this);
    }
}

