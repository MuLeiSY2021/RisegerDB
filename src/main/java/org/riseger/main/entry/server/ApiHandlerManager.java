package org.riseger.main.entry.server;

import org.riseger.main.compiler.Compiler;
import org.riseger.main.workflow.workflow.CommonWorkFlow;
import org.riseger.protoctl.packet.request.ResponseRequest;

public class ApiHandlerManager {
    public static ApiHandlerManager INSTANCE;

    private final CommonWorkFlow preloadWorkflow = new CommonWorkFlow();

    private final CommonWorkFlow maintainWorkflow = new CommonWorkFlow();

    private final CommonWorkFlow searchWorkflow = new CommonWorkFlow();

    private Compiler compiler;

    public void setPreloadRequest(ResponseRequest request) throws Exception {
        preloadWorkflow.push(request);
    }

    public void setSearchRequest(ResponseRequest request) throws Exception {
        searchWorkflow.push(request);
    }

}