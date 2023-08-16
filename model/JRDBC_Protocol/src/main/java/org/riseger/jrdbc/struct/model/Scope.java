package org.riseger.jrdbc.struct.model;

import lombok.Data;

import java.util.List;

@Data
public class Scope {
    private String name;

    private Scope parent;

    private List<Scope> children;

    public Scope(String name, Database database) {
        this.name = name;
        database.addScope(this);
    }

    public Scope(String name, Scope parent) {
        this.name = name;
        this.parent = parent;
        parent.addChild(this);
    }

    public void addChild(Scope child) {
        children.add(child);
    }
}
