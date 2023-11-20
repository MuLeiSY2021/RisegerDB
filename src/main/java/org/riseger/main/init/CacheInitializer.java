package org.riseger.main.init;

import org.apache.log4j.Logger;
import org.riseger.main.cache.entity.component.Database_c;
import org.riseger.main.cache.manager.CacheMaster;
import org.riseger.main.cache.manager.DatabaseManager;

import java.util.List;

public class CacheInitializer extends Initializer {
    private static final Logger LOG = Logger.getLogger(CacheInitializer.class);

    private final StorageInitializer storageInitializer;

    public CacheInitializer(String rootPath, StorageInitializer storageInitializer) {
        super(rootPath);
        this.storageInitializer = storageInitializer;
    }

    public boolean init() {
        try {
            CacheMaster.setINSTANCE(new CacheMaster());
            DatabaseManager dbm = CacheMaster.INSTANCE.getDatabaseManager();
            List<Database_c> databases = storageInitializer.initDatabases();
            if (databases != null) {
                for (Database_c database : databases) {
                    dbm.addDatabase(database);
                }
            }
            return true;
        } catch (Exception e) {
            LOG.error("Failed to initialize cache", e);
            return false;
        }

    }

}
