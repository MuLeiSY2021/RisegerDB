package org.riseger.main.workflow.job;

import org.riseger.protoctl.request.Request;

public interface Job<R extends Request> extends Runnable {
    PreloadJob warp(R request);
}
