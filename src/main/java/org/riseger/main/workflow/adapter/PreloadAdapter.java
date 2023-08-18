package org.riseger.main.workflow.adapter;

import org.riseger.main.workflow.job.PreloadJob;
import org.riseger.main.workflow.jobstack.JobStack;
import org.riseger.main.workflow.jobstack.PreloadJobStack;
import org.riseger.protoctl.request.PreloadRequest;

public class PreloadAdapter implements Adapter<PreloadJob, PreloadRequest>{
    private final JobStack<PreloadJob> stack = new PreloadJobStack();

    @Override
    public void adapt(PreloadRequest request) {
        stack.push(new PreloadJob().warp(request));
    }

    @Override
    public PreloadJob getJob() {
        return stack.pop();
    }
}
