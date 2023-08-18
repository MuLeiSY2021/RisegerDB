package org.riseger.main.workflow.adapter;

import org.riseger.main.workflow.job.PreloadJob;
import org.riseger.main.workflow.jobstack.JobStack;
import org.riseger.main.workflow.jobstack.PreloadJobStack;
import org.riseger.protoctl.request.PreloadRequest;

public class PreloadAdapter implements Adapter<PreloadRequest>{
    private final JobStack<PreloadJob> stack;

    public PreloadAdapter(JobStack<PreloadJob> stack) {
        this.stack = stack;
    }

    @Override
    public void adapt(PreloadRequest request) {
        stack.push(new PreloadJob().warp(request));
    }

}
