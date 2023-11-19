package org.riseger.main.entry.handler;

import io.netty.channel.ChannelHandlerContext;
import org.riseger.main.entry.server.ApiHandlerManager;
import org.riseger.protoctl.packet.request.CommandSQLRequest;

public class CommandSQLMessageInboundHandler extends TransponderHandler<CommandSQLRequest> {

    @Override
    public void handle(ChannelHandlerContext ctx, CommandSQLRequest msg) throws Exception {
        msg.setIpAddress(ctx.channel().remoteAddress().toString());
        ApiHandlerManager.INSTANCE.setRequest(msg, msg.getRequestType());
    }
}
