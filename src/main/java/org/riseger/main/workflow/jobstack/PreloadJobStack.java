package org.riseger.main.workflow.jobstack;

import org.riseger.main.workflow.job.PreloadJob;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PreloadJobStack implements JobStack<PreloadJob> {
    private final ConcurrentLinkedQueue<PreloadJob> jobs = new ConcurrentLinkedQueue<>();

    @Override
    public void push(PreloadJob job) {
        jobs.add(job);
    }

    @Override
    public PreloadJob pop() {
        return jobs.poll();
    }
}
