package org.riseger.protoctl.job;

import org.riseger.main.compiler.CompilerMaster;
import org.riseger.main.compiler.compoent.SearchSession;
import org.riseger.main.entry.handler.TransponderHandler;
import org.riseger.protoctl.compiler.CommandTree;
import org.riseger.protoctl.packet.request.TranspondRequest;
import org.riseger.protoctl.packet.response.TextSQLResponse;

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
            SearchSession session = CompilerMaster.INSTANCE.adapt(commandTree, this.ipAddress);
            response.setResult(session.process());
            super.done();
        } catch (Exception e) {
            super.sendError(e);
        }
    }

}
