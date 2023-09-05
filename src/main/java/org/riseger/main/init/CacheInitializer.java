package org.riseger.main.init;

import org.riseger.main.cache.entity.component.Database_c;
import org.riseger.main.cache.manager.CacheMaster;
import org.riseger.main.cache.manager.DatabaseManager;

import java.util.List;

public class CacheInitializer implements Initializer {

    private final StorageInitializer storageInitializer;

    public CacheInitializer(StorageInitializer storageInitializer) {
        this.storageInitializer = storageInitializer;
    }

    public void init() throws Exception {
        CacheMaster.setINSTANCE(new CacheMaster());
        DatabaseManager dbm = CacheMaster.INSTANCE.getDatabaseManager();
        List<Database_c> databases = storageInitializer.initDatabases();
        if( databases != null) {
            for (Database_c database : databases) {
                dbm.addDatabase(database);
            }
        }
    }

}
