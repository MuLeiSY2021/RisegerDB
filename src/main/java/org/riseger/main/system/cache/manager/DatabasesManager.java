package org.riseger.main.system.cache.manager;

import org.riseger.main.system.StorageSystem;
import org.riseger.main.system.cache.component.Config;
import org.riseger.main.system.cache.component.Database;
import org.riseger.protocol.struct.entity.Database_p;
import org.riseger.protocol.struct.entity.GeoMap_p;
import org.riseger.protocol.struct.entity.Model_p;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class DatabasesManager {
    private final Map<String, Database> databases;

    public DatabasesManager() {
        this.databases = new ConcurrentHashMap<>();
    }

    public void preloadDatabase(Database_p database) throws Throwable {
        Database db = new Database(database.getName());
        this.databases.put(database.getName(), db);
        //添加Config
        for (Map.Entry<Config, String> entry : database.getConfigs().entrySet()) {
            db.addConfig(entry.getKey(), entry.getValue());
        }

        //添加Model
        for (Model_p model : database.getModels()) {
            db.addModel(model);
        }

        //
        for (GeoMap_p map : database.getMaps()) {
            db.addMap(map);
        }

        StorageSystem.DEFAULT.writeDatabase(db);
        db.active();
    }


    public void addDatabase(Database database) {
        this.databases.put(database.getName(), database);
    }

    public Database getDatabase(String database) {
        return this.databases.get(database);
    }

    public int size() {
        return this.databases.size();
    }

    public List<String> getDatabasesName() {
        List<String> databases = new ArrayList<>(this.size());
        databases.addAll(this.databases.keySet());
        return databases;
    }

    public List<Database> getDatabases() {
        return new LinkedList<>(databases.values());
    }
}
