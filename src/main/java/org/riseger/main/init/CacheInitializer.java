package org.riseger.main.init;

import org.apache.log4j.Logger;
import org.riseger.main.system.CacheSystem;
import org.riseger.main.system.cache.entity.component.Database_c;
import org.riseger.main.system.cache.manager.DatabasesManager;

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
            CacheSystem.setINSTANCE(new CacheSystem());
            DatabasesManager dbm = CacheSystem.INSTANCE.getDatabasesManager();
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
