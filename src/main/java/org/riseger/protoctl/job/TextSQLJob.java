package org.riseger.protoctl.job;

import org.riseger.main.compiler.CompilerMaster;
import org.riseger.main.compiler.session.Session;
import org.riseger.main.entry.handler.TransponderHandler;
import org.riseger.protoctl.packet.request.TranspondRequest;
import org.riseger.protoctl.packet.response.TextSQLResponse;


public class TextSQLJob extends TranspondJob {

    private final String text;

    public TextSQLJob(TransponderHandler<? extends TranspondRequest> transponder, String text) {
        super(transponder);
        this.text = text;
    }

    @Override
    public void process() throws Exception {
        TextSQLResponse response = new TextSQLResponse();
        super.prepare(response);
        Session session = CompilerMaster.INSTANCE.adapt(text);
        response.setShellOutcome(session.run());

        super.done();
    }

}
