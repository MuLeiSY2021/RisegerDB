package org.resegerdb.jrdbc.utils;

import com.google.gson.Gson;
import org.resegerdb.jrdbc.command.preload.builder.DatabaseBuilder;
import org.riseger.protoctl.request.PreloadRequest;
import org.riseger.protoctl.struct.entity.Database;
import org.riseger.utils.Utils;

import java.util.LinkedList;
import java.util.List;


public class PreloadBuilder {
    private final List<Database> databases = new LinkedList<Database>();


    public PreloadBuilder() {
    }

    public void write() {
        Utils.writeToFile(new Gson().toJson(new PreloadRequest(this.databases)), "src/main/resources/dataSource/0.json");
    }

    public DatabaseBuilder buildDatabase() {
        return new DatabaseBuilder(this);
    }

    public void addDatabase(Database database) {
        this.databases.add(database);
    }
}
