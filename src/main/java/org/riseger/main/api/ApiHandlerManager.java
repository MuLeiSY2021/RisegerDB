package org.riseger.main.api;

import org.riseger.main.api.compile.Compiler;
import org.riseger.main.api.workflow.workflow.CommonWorkFlow;
import org.riseger.protoctl.message.Message;

public class ApiHandlerManager {
    public static ApiHandlerManager INSTANCE;

    private final CommonWorkFlow preloadWorkflow = new CommonWorkFlow();

    private final CommonWorkFlow maintainWorkflow = new CommonWorkFlow();

    private final CommonWorkFlow searchWorkflow = new CommonWorkFlow();

    private Compiler compiler;

    public void setPreloadRequest(Message message) throws Exception {
        preloadWorkflow.push(message.warp());
    }

    public void setSearchRequest(Message message) throws Exception {
        searchWorkflow.push(message.warp());
    }

}
