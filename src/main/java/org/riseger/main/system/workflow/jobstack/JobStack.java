package org.riseger.main.system.workflow.jobstack;

import org.riseger.protoctl.job.Job;

public interface JobStack {
    void push(Job job);

    Job pop();
}
