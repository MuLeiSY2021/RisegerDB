package org.riseger.main.api.workflow.jobstack;

import org.riseger.main.api.workflow.job.Job;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CommonJobStack implements JobStack {
    private final ConcurrentLinkedQueue<Job> jobs = new ConcurrentLinkedQueue<>();

    @Override
    public void push(Job job) {
        jobs.add(job);
    }

    @Override
    public Job pop() {
        return jobs.poll();
    }
}