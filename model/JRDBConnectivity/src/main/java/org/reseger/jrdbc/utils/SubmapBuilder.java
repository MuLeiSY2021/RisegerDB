package org.reseger.jrdbc.utils;

import org.riseger.protocol.struct.entity.GeoMap_p;

public class SubmapBuilder {
    private final GeoMap_p parent;

    private String scopePath;

    private String name;

    private Submap submap;

    public SubmapBuilder(GeoMap_p parent) {
        this.parent = parent;
    }

    public SubmapBuilder name(String name) {
        this.name = name;
        return this;
    }

    public SubmapBuilder scopePath(String scopePath) {
        this.scopePath = scopePath;
        return this;
    }

    public void build() {
        this.submap = new Submap(name, scopePath, parent);
    }

    public SubmapBuilder buildSubmap() {
        if (this.submap != null) {
            return new SubmapBuilder(this.submap);
        } else {
            throw new NullPointerException();
        }
    }


    public FieldBuilder buildField() {
        return new FieldBuilder(this.submap);
    }
}
