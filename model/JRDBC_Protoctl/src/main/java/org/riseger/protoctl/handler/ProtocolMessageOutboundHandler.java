package org.riseger.protoctl.handler;

import io.netty.channel.*;
import org.apache.log4j.Logger;
import org.riseger.protoctl.message.*;

public class ProtocolMessageOutboundHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if(!(msg instanceof Message)) {
            return;
        }

        Message message = (Message) msg;
        ctx.writeAndFlush(new ProtocolMessage(message.getType(),JsonSerializer.serialize(message)));
    }


}
