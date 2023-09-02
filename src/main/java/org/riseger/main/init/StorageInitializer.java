package org.riseger.main.init;

import org.riseger.main.storage.FileSystemManagers;

public class StorageInitializer implements Initializer{
    private final String rootPath;

    public StorageInitializer(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public void init() {
        FileSystemManagers.DEFAULT = new FileSystemManagers(rootPath);
    }
}
