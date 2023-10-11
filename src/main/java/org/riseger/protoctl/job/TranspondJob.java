package org.riseger.protoctl.job;

import org.apache.log4j.Logger;
import org.riseger.main.entry.handler.TransponderHandler;
import org.riseger.protoctl.packet.request.TranspondRequest;
import org.riseger.protoctl.packet.response.BasicResponse;


public abstract class TranspondJob implements Job {
    private static final Logger LOG = Logger.getLogger(TextSQLJob.class);

    private final TransponderHandler<? extends TranspondRequest> transponder;

    public TranspondJob(TransponderHandler<? extends TranspondRequest> transponder) {
        this.transponder = transponder;
    }

    public abstract void process() throws Exception;

    public void prepare(BasicResponse response) {
        transponder.setOut(response);
    }

    public void done() {
        transponder.wake();
    }

    @Override
    public void run() {
        try {
            process();
        } catch (Exception e) {
            transponder.send(e);
            LOG.error(e);
            e.printStackTrace();
        }
    }
}
