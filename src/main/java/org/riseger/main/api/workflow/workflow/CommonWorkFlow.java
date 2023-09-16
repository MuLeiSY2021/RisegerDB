package org.riseger.main.api.workflow.workflow;

import org.apache.log4j.Logger;
import org.riseger.main.api.workflow.adapter.CommonAdapter;
import org.riseger.main.api.workflow.jobstack.CommonJobStack;
import org.riseger.main.api.workflow.wokerpool.CommonWorkerPool;
import org.riseger.protoctl.packet.request.ResponseRequest;

public class CommonWorkFlow {
    private static final Logger LOG = Logger.getLogger(CommonWorkFlow.class);
    private final CommonAdapter adapter;
    private final CommonWorkerPool workerPool;

    public CommonWorkFlow() {
        CommonJobStack jobStack = new CommonJobStack();
        this.adapter = new CommonAdapter(jobStack);
        this.workerPool = new CommonWorkerPool(jobStack);
    }

    public void push(ResponseRequest request) {
        LOG.debug("收到一条请求");
        adapter.adapt(request);
        workerPool.arrangeWork();
    }
}
