package org.riseger.main.api;

import org.riseger.main.api.compile.Compiler;
import org.riseger.main.api.workflow.workflow.CommonWorkFlow;
import org.riseger.protoctl.message.Message;

public class ApiHandlerManager {
    public static ApiHandlerManager INSTANCE;

    private CommonWorkFlow preloadWorkflow = new CommonWorkFlow();

    private CommonWorkFlow maintainWorkflow = new CommonWorkFlow();

    private CommonWorkFlow selectWorkflow = new CommonWorkFlow();

    private Compiler compiler;

    public void setPreloadRequest(Message preloadRequest) {
        preloadWorkflow.push(preloadRequest.warp());
    }
}