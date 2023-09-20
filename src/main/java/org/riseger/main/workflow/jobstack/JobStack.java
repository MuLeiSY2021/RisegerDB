package org.riseger.main.workflow.jobstack;

import org.riseger.protoctl.job.Job;

public interface JobStack {
    int STACK_SIZE = 100;

    void push(Job job);

    Job pop();
}
