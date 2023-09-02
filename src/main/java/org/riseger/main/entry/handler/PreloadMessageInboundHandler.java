package org.riseger.main.entry.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.riseger.main.api.ApiHandlerManager;
import org.riseger.protoctl.message.PreloadMessage;
import org.riseger.protoctl.response.PreloadResponse;

public class PreloadMessageInboundHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg1) throws Exception {
        if (!(msg1 instanceof PreloadMessage)) {
            return;
        }
        PreloadMessage msg = (PreloadMessage) msg1;
        PreloadResponse response = new PreloadResponse();
        try {
            ApiHandlerManager.INSTANCE.setPreloadRequest(msg);
        } catch (Exception e) {
            response.setSuccess(false);
        }
        response.setSuccess(true);
        ctx.channel().writeAndFlush(response);
    }
}
