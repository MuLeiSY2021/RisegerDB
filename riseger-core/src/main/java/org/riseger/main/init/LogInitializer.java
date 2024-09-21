package org.riseger.main.init;

import org.apache.log4j.Logger;
import org.riseger.main.system.LogSystem;

public class LogInitializer extends Initializer {
    private static final Logger LOG = Logger.getLogger(LogInitializer.class);


    public LogInitializer(String rootPath) {
        super(rootPath);
    }

    public boolean init() {
        try {
            LogSystem.setINSTANCE(new LogSystem(rootPath));
            LogSystem.INSTANCE.init();
            return true;
        } catch (Exception e) {
            LOG.error("Failed to initialize log", e);
            return false;
        }

    }

}
