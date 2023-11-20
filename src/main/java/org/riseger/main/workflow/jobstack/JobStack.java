package org.riseger.main.workflow.jobstack;

import org.riseger.protoctl.job.Job;

public interface JobStack {
    void push(Job job);

    Job pop();
}
