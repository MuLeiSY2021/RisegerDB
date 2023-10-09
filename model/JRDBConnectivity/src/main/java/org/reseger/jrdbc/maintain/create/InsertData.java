package org.reseger.jrdbc.maintain.create;

import org.reseger.jrdbc.maintain.command.Command;
import org.reseger.jrdbc.maintain.command.Commands;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InsertData extends Command {
    String database;

    String map;

    String model;

    String scope;

    Map<String, String> parameters;

    public InsertData(Commands commands) {
        super(commands);
    }

    public InsertData database(String database) {
        this.database = database;
        return this;
    }

    public InsertData map(String map) {
        this.map = map;
        return this;
    }

    public InsertData model(String model) {
        this.model = model;
        return this;
    }

    public InsertData scope(String scope) {
        this.scope = scope;
        return this;
    }

    public InsertData parameter(String name, String value) {
        this.parameters.put(name, value);
        return this;
    }

    public List<String> build() {
        return new LinkedList<String>();
    }
}
