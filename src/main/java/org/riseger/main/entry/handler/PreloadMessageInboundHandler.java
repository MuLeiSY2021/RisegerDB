package org.riseger.main.entry.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.riseger.protoctl.message.PreloadMessage;

public class PreloadMessageInboundHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg1) throws Exception {
        if (!(msg1 instanceof PreloadMessage)) {
            return;
        }
        PreloadMessage msg = (PreloadMessage) msg1;


    }
}
