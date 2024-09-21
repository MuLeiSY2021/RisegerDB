package org.riseger.protocol.job;

import org.apache.log4j.Logger;
import org.riseger.main.entry.handler.TransponderHandler;
import org.riseger.protocol.packet.request.TranspondRequest;
import org.riseger.protocol.packet.response.BasicResponse;


public abstract class TranspondJob implements Job {
    private static final Logger LOG = Logger.getLogger(TextSQLJob.class);

    private final TransponderHandler<? extends TranspondRequest> transponder;

    public TranspondJob(TransponderHandler<? extends TranspondRequest> transponder) {
        this.transponder = transponder;
    }

    public abstract void process();

    public void prepare(BasicResponse response) {
        transponder.setOut(response);
    }

    public void done() {
        transponder.wake();
    }

    @Override
    public void run() {
        process();
    }

    public void sendError(Exception e) {
        transponder.send(e);
        LOG.error("Error ", e);
    }
}
