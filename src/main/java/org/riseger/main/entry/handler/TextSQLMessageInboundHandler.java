package org.riseger.main.entry.handler;

import io.netty.channel.ChannelHandlerContext;
import org.riseger.main.system.WorkflowSystem;
import org.riseger.protoctl.packet.request.TextSQLRequest;

public class TextSQLMessageInboundHandler extends TransponderHandler<TextSQLRequest> {

    @Override
    public void handle(ChannelHandlerContext ctx, TextSQLRequest msg) throws Exception {
        msg.setIpAddress(ctx.channel().remoteAddress().toString());
        WorkflowSystem.INSTANCE.setRequest(msg, msg.getRequestType());
    }
}
