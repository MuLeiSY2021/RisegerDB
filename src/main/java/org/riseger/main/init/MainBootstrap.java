package org.riseger.main.init;

import org.apache.log4j.Logger;

public class MainBootstrap {
    private static Logger LOG;

    public static void main(String[] args) {
        String rootPath = getRootPath();

        LogInitializer logInitializer = new LogInitializer();
        logInitializer.initLog();
        MainBootstrap.LOG = Logger.getLogger(MainBootstrap.class);
        LOG.info("Log initializer successfully");

        StorageInitializer storageInitializer = new StorageInitializer(rootPath);
        storageInitializer.init();
        LOG.info("Storage initialized successfully");

        CacheInitializer cacheInitializer = new CacheInitializer();
        cacheInitializer.init();
        LOG.info("Cache initialized successfully");

        RequestHandlerInitializer requestHandlerInitializer = new RequestHandlerInitializer();
        requestHandlerInitializer.init();
        LOG.info("RequestHandler initialized successfully");

        EntryInitializer entryInitializer = new EntryInitializer();
        entryInitializer.init();
        LOG.info("Entry initialized successfully");
    }

    public static String getRootPath() {
        String realPath = MainBootstrap.class.getClassLoader().getResource("")
                .getFile();
        java.io.File file = new java.io.File(realPath);
        realPath = file.getAbsolutePath();
        try {
            realPath = java.net.URLDecoder.decode(realPath, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return realPath;
    }
}
