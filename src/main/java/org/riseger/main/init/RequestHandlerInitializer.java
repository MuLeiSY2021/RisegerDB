package org.riseger.main.init;

import org.riseger.main.entry.server.ApiHandlerManager;

public class RequestHandlerInitializer implements Initializer {
    public void init() {
        ApiHandlerManager.INSTANCE = new ApiHandlerManager();
    }
}
