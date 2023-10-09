package org.riseger.protoctl.job;

import org.riseger.main.compiler.CompilerMaster;
import org.riseger.main.compiler.session.Session;
import org.riseger.main.entry.handler.TransponderHandler;
import org.riseger.protoctl.packet.request.SearchRequest;

public class TextSQLJob implements Job {

    private final String text;

    private final TransponderHandler<SearchRequest, String> transponder;

    public TextSQLJob(String text, TransponderHandler<SearchRequest, String> transponder) {
        this.text = text;
        this.transponder = transponder;
    }

    @Override
    public void run() {
        Session session = CompilerMaster.INSTANCE.adapt(text);
        transponder.setOut(session.run());
    }
}
