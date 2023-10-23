package org.riseger.main.init;

import org.apache.log4j.Logger;
import org.riseger.main.cache.entity.component.Database_c;
import org.riseger.main.storage.FileSystemManagers;

import java.util.List;

public class StorageInitializer implements Initializer {
    public static final Logger LOG = Logger.getLogger(StorageInitializer.class);

    private final String rootPath;

    public StorageInitializer(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public void init() {
        FileSystemManagers.DEFAULT = new FileSystemManagers(rootPath);
    }

    public List<Database_c> initDatabases() {
        try {
            return FileSystemManagers.DEFAULT.initDatabases();
        } catch (Exception e) {
            LOG.error("Error ", e);
        }
        return null;
    }
}
