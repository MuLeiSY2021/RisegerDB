package org.riseger.main.workflow.workflow;

import org.riseger.main.workflow.adapter.PreloadAdapter;
import org.riseger.main.workflow.job.PreloadJob;
import org.riseger.main.workflow.jobstack.PreloadJobStack;
import org.riseger.main.workflow.wokerpool.PreloadWorkerPool;
import org.riseger.protoctl.request.PreloadRequest;

public class PreloadWorkFlow {
    private final PreloadAdapter adapter;

    private final PreloadWorkerPool workerPool;

    public PreloadWorkFlow() {
        PreloadJobStack jobStack = new PreloadJobStack();
        this.adapter = new PreloadAdapter(jobStack);
        this.workerPool = new PreloadWorkerPool(jobStack);
    }

    public void push(PreloadRequest request) {
        adapter.adapt(request);
        workerPool.arrangeWork();
    }
}
