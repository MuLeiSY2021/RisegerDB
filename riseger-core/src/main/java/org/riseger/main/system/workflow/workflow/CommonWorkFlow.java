package org.riseger.main.system.workflow.workflow;

import org.apache.log4j.Logger;
import org.riseger.main.system.workflow.adapter.CommonAdapter;
import org.riseger.main.system.workflow.jobstack.CommonJobStack;
import org.riseger.main.system.workflow.wokerpool.CommonWorkerPool;
import org.riseger.protocol.packet.request.BasicRequest;

public class CommonWorkFlow {
    private static final Logger LOG = Logger.getLogger(CommonWorkFlow.class);
    private final CommonAdapter adapter;
    private final CommonWorkerPool workerPool;

    public CommonWorkFlow() {
        CommonJobStack jobStack = new CommonJobStack();
        this.adapter = new CommonAdapter(jobStack);
        this.workerPool = new CommonWorkerPool(jobStack);
    }

    public void push(BasicRequest request) {
        LOG.debug("收到一条请求");
        adapter.adapt(request);
        workerPool.arrangeWork();
    }

    public void close() {
        workerPool.close();
    }
}
