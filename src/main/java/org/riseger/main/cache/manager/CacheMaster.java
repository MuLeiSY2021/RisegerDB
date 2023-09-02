package org.riseger.main.cache.manager;

import lombok.Data;

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
}
