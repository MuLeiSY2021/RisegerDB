package org.riseger.main.cache.manager;

import org.riseger.main.cache.entity.component.Database_c;
import org.riseger.main.storage.FileSystemManagers;
import org.riseger.protoctl.struct.config.Config;
import org.riseger.protoctl.struct.entity.Database;
import org.riseger.protoctl.struct.entity.MapDB;
import org.riseger.protoctl.struct.entity.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DatabaseManager {
    private final Map<String, Database_c> databases;

    public DatabaseManager() {
        this.databases = new HashMap<>();
    }

    public void preloadDatabase(Database database) throws IOException {
        Database_c db = new Database_c(database.getName());
        this.databases.put(database.getName(), db);
        //添加Config
        for (Map.Entry<Config, String> entry : database.getConfigs().entrySet()) {
            db.addConfig(entry.getKey(), entry.getValue());
        }

        //添加Model
        for (Model model : database.getModels()) {
            db.addModel(model);
        }

        //
        for (MapDB map : database.getMaps()) {
            db.addMap(map);
        }

        FileSystemManagers.DEFAULT.preloadFSM.saveDatabase(db);
        db.active();
    }


    public void addDatabase(Database_c database) {
        this.databases.put(database.getName(), database);
    }

    public Database_c getDatabase(String database) {
        return this.databases.get(database);
    }

    public int size() {
        return this.databases.size();
    }

    public List<String> getDatabases() {
        List<String> databases = new ArrayList<>(this.size());
        databases.addAll(this.databases.keySet());
        return databases;
    }
}
