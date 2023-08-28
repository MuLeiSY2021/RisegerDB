package org.riseger.main.cache.entity.manager;

import org.riseger.main.cache.entity.component.db.Database_c;
import org.riseger.protoctl.struct.config.Config;
import org.riseger.protoctl.struct.entity.Database;
import org.riseger.protoctl.struct.entity.MapDB;
import org.riseger.protoctl.struct.entity.Model;

import java.util.HashMap;
import java.util.Map;


public class DatabaseManager {
    private final Map<String,Database_c> databases ;

    public DatabaseManager() {
        this.databases = new HashMap<>();
    }

    //TODO:把handler实现化

    public Database_c preloadDatabase(Database database) {
        Database_c db = addDatabase(database);

        //添加Config
        for (Map.Entry<Config,String> entry:database.getConfigs().entrySet()) {
            db.addConfig(entry.getKey(), entry.getValue());
        }

        //添加Model
        for (Model model:database.getModels()) {
            db.addModel(model);
        }

        //
        for (MapDB map : database.getMaps()) {
            db.addMap(map);
        }
        return db;
    }

    public Database_c addDatabase(Database database) {
        Database_c res = new Database_c(database.getName());
        this.databases.put(database.getName(),res);
        return res ;
    }
}
