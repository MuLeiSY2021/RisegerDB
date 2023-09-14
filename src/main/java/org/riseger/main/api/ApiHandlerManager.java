package org.riseger.main.api;

import org.riseger.main.api.compile.Compiler;
import org.riseger.main.api.workflow.workflow.CommonWorkFlow;
import org.riseger.protoctl.message.Message;

import java.io.IOException;

public class ApiHandlerManager {
    public static ApiHandlerManager INSTANCE;

    private CommonWorkFlow preloadWorkflow = new CommonWorkFlow();

    private CommonWorkFlow maintainWorkflow = new CommonWorkFlow();

    private CommonWorkFlow searchWorkflow = new CommonWorkFlow();

    private Compiler compiler;

    public void setPreloadRequest(Message message) throws IOException {
        preloadWorkflow.push(message.warp());
    }

    public void setSearchRequest(Message message) throws IOException {
        searchWorkflow.push(message.warp());
    }

}
