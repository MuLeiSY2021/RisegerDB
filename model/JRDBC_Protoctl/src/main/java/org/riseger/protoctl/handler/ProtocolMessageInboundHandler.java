package org.riseger.protoctl.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;
import org.riseger.protoctl.message.*;

public class ProtocolMessageInboundHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(!(msg instanceof ProtocolMessage)) {
            return;
        }
        ProtocolMessage protocolMessage = (ProtocolMessage) msg;
        switch(protocolMessage.getMessageType()) {
            case TYPE_0_PRELOAD:
                ctx.writeAndFlush(JsonSerializer.deserialize(protocolMessage.getData(), PreloadMessage.class));
                break;
            case TYPE_1_MAINTAIN:
                ctx.writeAndFlush(JsonSerializer.deserialize(protocolMessage.getData(), MaintainMessage.class));
                break;
            case TYPE_2_LOOKUP:
                ctx.writeAndFlush(JsonSerializer.deserialize(protocolMessage.getData(), LookupMessage.class));
                break;
            case TYPE_3_RESPONSE:
                ctx.writeAndFlush(JsonSerializer.deserialize(protocolMessage.getData(), ResponseMessage.class));
                break;
            default:
                Logger.getLogger(ProtocolMessageInboundHandler.class).warn("No suitable message type");
        }
    }


}
