package org.riseger.main.init;

import org.apache.log4j.Logger;

public class MainBootstrap {
    private static Logger LOG;

    public static void main(String[] args) throws Exception {
        String rootPath = args[0];

        LogInitializer logInitializer = new LogInitializer(rootPath);
        logInitializer.initLog();
        MainBootstrap.LOG = Logger.getLogger(MainBootstrap.class);
        LOG.info("Log initializer successfully");

        StorageInitializer storageInitializer = new StorageInitializer(rootPath);
        storageInitializer.init();
        LOG.info("Storage initialized successfully");

        CacheInitializer cacheInitializer = new CacheInitializer(storageInitializer);
        cacheInitializer.init();
        LOG.info("Cache initialized successfully");

        CompilerInitialize compilerInitialize = new CompilerInitialize(rootPath);
        compilerInitialize.init();
        LOG.info("Compiler initialized successfully");

        RequestHandlerInitializer requestHandlerInitializer = new RequestHandlerInitializer();
        requestHandlerInitializer.init();
        LOG.info("RequestHandler initialized successfully");

        EntryInitializer entryInitializer = new EntryInitializer();
        entryInitializer.init();
        LOG.info("Entry initialized successfully");

        ConsoleInitialize consoleInitialize = new ConsoleInitialize();
        consoleInitialize.init();
        LOG.info("Console initialized successfully");
    }

}
