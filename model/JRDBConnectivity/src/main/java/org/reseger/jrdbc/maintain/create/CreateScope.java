package org.reseger.jrdbc.maintain.create;

import org.reseger.jrdbc.maintain.command.Command;
import org.reseger.jrdbc.maintain.command.Commands;

import java.util.LinkedList;
import java.util.List;

public class CreateScope extends Command {

    String database;

    String map;

    String parent;

    String name;

    public CreateScope(Commands commands) {
        super(commands);
    }

    public CreateScope database(String database) {
        this.database = database;
        return this;
    }

    public CreateScope name(String name) {
        this.name = name;
        return this;
    }

    public CreateScope map(String map) {
        this.map = map;
        return this;
    }


    public CreateScope parent(String parent) {
        this.parent = parent;
        return this;
    }

    public List<String> build() {
        return new LinkedList<String>();
    }
}
