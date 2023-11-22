package org.riseger.main.system;

import org.riseger.main.system.workflow.workflow.CommonWorkFlow;
import org.riseger.protoctl.packet.RequestType;
import org.riseger.protoctl.packet.request.BasicRequest;

import java.util.HashMap;
import java.util.Map;

public class WorkflowSystem {
    public static WorkflowSystem INSTANCE;

    private final Map<RequestType, CommonWorkFlow> workFlowMap = new HashMap<>();

    {
        workFlowMap.put(RequestType.SEARCH, new CommonWorkFlow());
        workFlowMap.put(RequestType.PRELOAD, new CommonWorkFlow());
        workFlowMap.put(RequestType.UPDATE, new CommonWorkFlow());
        workFlowMap.put(RequestType.SHELL, new CommonWorkFlow());
    }

    public void setRequest(BasicRequest request, RequestType type) {
        workFlowMap.get(type).push(request);
    }

    public void close() {
        for (CommonWorkFlow c : workFlowMap.values()) {
            c.close();
        }
    }
}
