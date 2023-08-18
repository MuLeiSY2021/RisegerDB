package org.riseger.main.workflow.worker;

import org.riseger.main.workflow.job.Job;

public interface Worker<J extends Job> {
    void work(J job);
}
