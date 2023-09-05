package org.riseger.main.init;

import org.apache.log4j.Logger;
import org.riseger.main.cache.entity.component.Database_c;
import org.riseger.main.storage.FileSystemManagers;

import java.io.IOException;
import java.util.List;

public class StorageInitializer implements Initializer {
    private final String rootPath;

    private final Logger LOG = Logger.getLogger(StorageInitializer.class);

    public StorageInitializer(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public void init() {
        FileSystemManagers.DEFAULT = new FileSystemManagers(rootPath);
    }

    public List<Database_c> initDatabases() throws Exception {
        try {
            return FileSystemManagers.DEFAULT.initDatabases();
        }catch (Exception e) {
            LOG.error(e);
            throw e;
        }
    }
}
