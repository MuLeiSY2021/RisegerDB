package org.resegerdb.jrdbc.driver;

import com.google.gson.Gson;
import org.resegerdb.jrdbc.command.preload.Database;
import org.resegerdb.jrdbc.driver.Connection;
import org.resegerdb.jrdbc.driver.Result;
import org.resegerdb.jrdbc.driver.Session;

import java.util.LinkedList;
import java.util.List;


public class PreloadSession implements Session {
    private final List<Database> databases = new LinkedList<Database>();

    private final Connection connection;

    public PreloadSession(Connection connection) {
        this.connection = connection;
    }

    public void addDatabase(Database database) {
        databases.add(database);
    }

    @Override
    public Result send() {
        connection.send(new Gson().toJson(databases).getBytes());
        return connection.getResponse();
    }

    public void addData(String data) {

    }

}
