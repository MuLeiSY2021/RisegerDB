package org.riseger.main.init;

import org.apache.log4j.Logger;
import org.riseger.main.system.StorageSystem;
import org.riseger.main.system.cache.entity.component.Database_c;

import java.util.List;

public class StorageInitializer extends Initializer {
    public static final Logger LOG = Logger.getLogger(StorageInitializer.class);

    public StorageInitializer(String rootPath) {
        super(rootPath);
    }

    @Override
    public boolean init() {
        try {
            StorageSystem.DEFAULT = new StorageSystem(rootPath);
            return true;
        } catch (Exception e) {
            LOG.error("Failed to initialize storage", e);
            return false;
        }
    }

    public List<Database_c> initDatabases() throws Exception {
        return StorageSystem.DEFAULT.initDatabases();
    }
}
