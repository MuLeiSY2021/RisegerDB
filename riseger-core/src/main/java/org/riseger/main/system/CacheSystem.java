package org.riseger.main.system;

import lombok.Data;
import org.riseger.main.system.cache.component.Database;
import org.riseger.main.system.cache.manager.DatabasesManager;

import java.util.List;

@Data
public class CacheSystem {
    public static CacheSystem INSTANCE;

    private DatabasesManager databasesManager;

    public CacheSystem() {
        this.databasesManager = new DatabasesManager();
    }

    public static void setINSTANCE(CacheSystem INSTANCE) {
        CacheSystem.INSTANCE = INSTANCE;

    }

    public Database getDatabase(String database) {
        return databasesManager.getDatabase(database);
    }

    public List<String> getDatabasesName() {
        return databasesManager.getDatabasesName();
    }

    public List<Database> getDatabases() {
        return databasesManager.getDatabases();
    }
}
