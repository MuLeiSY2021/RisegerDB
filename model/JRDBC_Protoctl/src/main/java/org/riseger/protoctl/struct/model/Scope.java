package org.riseger.protoctl.struct.model;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class Scope {
    private String name;

    private List<Scope> children = new LinkedList<>();

    public Scope(String name, Database database) {
        this.name = name;
        database.addScope(this);
    }

    public Scope(String name, Scope parent) {
        this.name = name;
        parent.addChild(this);
    }

    public void addChild(Scope child) {
        children.add(child);
    }
}
