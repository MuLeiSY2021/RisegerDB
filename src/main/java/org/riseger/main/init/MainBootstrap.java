package org.riseger.main.init;

import org.apache.log4j.Logger;

public class MainBootstrap {
    private static Logger LOG;

    public static void main(String[] args) throws Exception {
        String rootPath = args[0];

        SystemLogInitializer systemLogInitializer = new SystemLogInitializer(rootPath);
        systemLogInitializer.init();
        MainBootstrap.LOG = Logger.getLogger(MainBootstrap.class);
        if (systemLogInitializer.init()) {
            LOG.info("Log initializer successfully");
        }

        ConfigInitializer configInitializer = new ConfigInitializer(rootPath);
        if (configInitializer.init()) {
            LOG.info("Config initializer successfully");
        }

        StorageInitializer storageInitializer = new StorageInitializer(rootPath);
        if (storageInitializer.init()) {
            LOG.info("Storage initialized successfully");
        }

        CacheInitializer cacheInitializer = new CacheInitializer(rootPath, storageInitializer);
        if (cacheInitializer.init()) {
            LOG.info("Cache initialized successfully");
        }

        CompilerInitialize compilerInitialize = new CompilerInitialize(rootPath);
        if (compilerInitialize.init()) {
            LOG.info("Compiler initialized successfully");
        }

        RequestHandlerInitializer requestHandlerInitializer = new RequestHandlerInitializer(rootPath);
        if (requestHandlerInitializer.init()) {
            LOG.info("RequestHandler initialized successfully");
        }

        EntryInitializer entryInitializer = new EntryInitializer(rootPath);
        if (entryInitializer.init()) {
            LOG.info("Entry initialized successfully");
        }

        ConsoleInitialize consoleInitialize = new ConsoleInitialize(rootPath);
        if (consoleInitialize.init()) {
            LOG.info("Console initialized successfully");
        }
        consoleInitialize.join();

        //Ending
        entryInitializer.close();
        requestHandlerInitializer.close();
        LOG.info("Server closed");
    }

}
