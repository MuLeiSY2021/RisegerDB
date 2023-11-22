package org.riseger.main.init;

import org.apache.log4j.Logger;
import org.riseger.main.system.WorkflowSystem;

public class RequestHandlerInitializer extends Initializer {
    private static final Logger LOG = Logger.getLogger(RequestHandlerInitializer.class);

    public RequestHandlerInitializer(String rootPath) {
        super(rootPath);
    }

    public boolean init() {
        try {
            WorkflowSystem.INSTANCE = new WorkflowSystem();
            return true;
        } catch (Exception e) {
            LOG.error("Failed to Initialize ApiHandler", e);
        }
        return false;
    }

    public void close() {
        WorkflowSystem.INSTANCE.close();
    }
}
