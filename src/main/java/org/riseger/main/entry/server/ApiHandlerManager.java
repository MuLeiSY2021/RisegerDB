package org.riseger.main.entry.server;

import org.riseger.main.workflow.workflow.CommonWorkFlow;
import org.riseger.protoctl.packet.request.BasicRequest;

public class ApiHandlerManager {
    public static ApiHandlerManager INSTANCE;

    private final CommonWorkFlow preloadWorkflow = new CommonWorkFlow();

    private final CommonWorkFlow maintainWorkflow = new CommonWorkFlow();

    private final CommonWorkFlow searchWorkflow = new CommonWorkFlow();

    private Compiler compiler;

    public void setPreloadRequest(BasicRequest request) {
        preloadWorkflow.push(request);
    }

    public void setSearchRequest(BasicRequest request) {
        searchWorkflow.push(request);
    }

    public void setMaintainRequest(BasicRequest request) {
        maintainWorkflow.push(request);
    }

}
