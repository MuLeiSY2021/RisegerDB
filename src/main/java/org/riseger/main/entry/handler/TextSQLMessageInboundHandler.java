package org.riseger.main.entry.handler;

import io.netty.channel.ChannelHandlerContext;
import org.riseger.main.entry.server.ApiHandlerManager;
import org.riseger.protoctl.packet.request.TextSQLRequest;

public class TextSQLMessageInboundHandler extends TransponderHandler<TextSQLRequest> {

    @Override
    public void handle(ChannelHandlerContext ctx, TextSQLRequest msg) throws Exception {
        msg.setIpAddress(ctx.channel().remoteAddress().toString());
        ApiHandlerManager.INSTANCE.setMaintainRequest(msg);
    }
}
