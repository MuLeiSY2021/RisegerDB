package org.riseger.main.workflow.jobstack;

import org.riseger.main.workflow.job.Job;

public interface JobStack<J extends Job<?>> {
    int STACK_SIZE = 100;

    void push(J job);

    J pop();
}
