package org.riseger.protoctl.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.riseger.protoctl.message.JsonSerializer;
import org.riseger.protoctl.message.Message;
import org.riseger.protoctl.message.ProtocolMessage;

public class ProtocolMessageOutboundHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if(!(msg instanceof Message)) {
            return;
        }

        Message message = (Message) msg;
        ctx.writeAndFlush(new ProtocolMessage(message.getType(), JsonSerializer.serialize(message)));
    }


}
