package org.riseger.main.api.workflow.workflow;

import org.riseger.main.api.workflow.adapter.CommonAdapter;
import org.riseger.main.api.workflow.jobstack.CommonJobStack;
import org.riseger.main.api.workflow.wokerpool.CommonWorkerPool;
import org.riseger.protoctl.request.Request;

public class CommonWorkFlow {
    private final CommonAdapter adapter;

    private final CommonWorkerPool workerPool;

    public CommonWorkFlow() {
        CommonJobStack jobStack = new CommonJobStack();
        this.adapter = new CommonAdapter(jobStack);
        this.workerPool = new CommonWorkerPool(jobStack);
    }

    public void push(Request request) {
        adapter.adapt(request);
        workerPool.arrangeWork();
    }
}
