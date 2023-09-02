package org.riseger.main.storage;

import org.riseger.main.storage.filesystem.PreloadFSM;

public class FileSystemManagers {
    public String rootPath;

    public static FileSystemManagers DEFAULT;

    public PreloadFSM preloadFSM;



    public FileSystemManagers(String rootPath) {
        this.rootPath = rootPath;
        this.preloadFSM = new PreloadFSM(rootPath);
    }
}
