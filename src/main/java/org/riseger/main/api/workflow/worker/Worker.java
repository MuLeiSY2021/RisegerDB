package org.riseger.main.api.workflow.worker;

import org.riseger.main.api.workflow.job.Job;

public interface Worker<J extends Job> {
    void work(J job);
}
