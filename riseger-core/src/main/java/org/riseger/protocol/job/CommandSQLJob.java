package org.riseger.protocol.job;

import org.riseger.main.entry.handler.TransponderHandler;
import org.riseger.main.system.CompileSystem;
import org.riseger.main.system.compile.compoent.SearchSession;
import org.riseger.protocol.compiler.CommandTree;
import org.riseger.protocol.packet.request.TranspondRequest;
import org.riseger.protocol.packet.response.TextSQLResponse;

public class CommandSQLJob extends TranspondJob {

    private final CommandTree commandTree;

    private final String ipAddress;


    public CommandSQLJob(TransponderHandler<? extends TranspondRequest> transponder, CommandTree commandTree, String ipAddress) {
        super(transponder);
        this.commandTree = commandTree;
        this.ipAddress = ipAddress;
    }

    @Override
    public void process() {
        try {
            TextSQLResponse response = new TextSQLResponse();
            super.prepare(response);
            SearchSession session = CompileSystem.INSTANCE.adapt(commandTree, this.ipAddress);
            response.setResult(session.process());
            super.done();
        } catch (Exception e) {
            super.sendError(e);
        }
    }

}
