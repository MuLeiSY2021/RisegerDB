package org.riseger.main.workflow.adapter;

import org.riseger.main.workflow.job.Job;
import org.riseger.protoctl.request.Request;

public interface Adapter<J extends Job<R>,R extends Request> {
    void adapt(R request);

    J getJob();
}
