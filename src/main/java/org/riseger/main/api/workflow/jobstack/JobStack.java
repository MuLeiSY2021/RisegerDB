package org.riseger.main.api.workflow.jobstack;

import org.riseger.main.api.workflow.job.Job;

public interface JobStack {
    int STACK_SIZE = 100;

    void push(Job job);

    Job pop();
}
