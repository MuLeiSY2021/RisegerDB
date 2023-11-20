package org.riseger.main.entry.server;

import org.riseger.main.workflow.workflow.CommonWorkFlow;
import org.riseger.protoctl.packet.RequestType;
import org.riseger.protoctl.packet.request.BasicRequest;

import java.util.HashMap;
import java.util.Map;

public class ApiHandlerManager {
    public static ApiHandlerManager INSTANCE;

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

}
