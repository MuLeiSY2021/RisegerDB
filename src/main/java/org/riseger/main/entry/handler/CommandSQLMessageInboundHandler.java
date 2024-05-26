package org.riseger.main.entry.handler;

import io.netty.channel.ChannelHandlerContext;
import org.riseger.main.system.WorkflowSystem;
import org.riseger.protocol.packet.request.CommandSQLRequest;

public class CommandSQLMessageInboundHandler extends TransponderHandler<CommandSQLRequest> {

    @Override
    public void handle(ChannelHandlerContext ctx, CommandSQLRequest msg) throws Exception {
        msg.setIpAddress(ctx.channel().remoteAddress().toString());
        WorkflowSystem.INSTANCE.setRequest(msg, msg.getRequestType());
    }
}
