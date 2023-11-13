package org.riseger.main.cache.manager;

import lombok.Data;
import org.riseger.main.cache.entity.component.Database_c;

import java.util.List;

@Data
public class CacheMaster {
    public static CacheMaster INSTANCE;

    private DatabaseManager databaseManager;

    public CacheMaster() {
        this.databaseManager = new DatabaseManager();
    }

    public static void setINSTANCE(CacheMaster INSTANCE) {
        CacheMaster.INSTANCE = INSTANCE;

    }

    public Database_c getDatabase(String database) {
        return databaseManager.getDatabase(database);
    }

    public List<String> getDatabases() {
        return databaseManager.getDatabases();
    }
}
