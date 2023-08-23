package org.riseger.main.cache.entity.mamager;

import lombok.Data;

@Data
public class CacheMaster {
    public static CacheMaster INSTANCE;


    private DatabaseManager databaseManager;

    public static void setINSTANCE(CacheMaster INSTANCE) {
        CacheMaster.INSTANCE = INSTANCE;
    }
}
