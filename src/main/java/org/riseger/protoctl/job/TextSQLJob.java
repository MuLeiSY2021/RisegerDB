package org.riseger.protoctl.job;

import org.riseger.main.compiler.CompilerMaster;
import org.riseger.main.compiler.compoent.SearchSession;
import org.riseger.main.entry.handler.TransponderHandler;
import org.riseger.protoctl.compiler.result.ResultSet;
import org.riseger.protoctl.packet.request.TranspondRequest;
import org.riseger.protoctl.packet.response.TextSQLResponse;


public class TextSQLJob extends TranspondJob {

    private final String text;

    private final String ipAddress;

    public TextSQLJob(TransponderHandler<? extends TranspondRequest> transponder, String text, String ipAddress) {
        super(transponder);
        this.text = text;
        this.ipAddress = ipAddress;
    }

    @Override
    public void process() {
        try {
            TextSQLResponse response = new TextSQLResponse();
            super.prepare(response);
            SearchSession session = CompilerMaster.INSTANCE.adapt(text, ipAddress);
            ResultSet resultSet = session.process();
            response.setResult(resultSet);
            super.done();
        } catch (Exception e) {
            super.sendError(e);
        }
    }

}
