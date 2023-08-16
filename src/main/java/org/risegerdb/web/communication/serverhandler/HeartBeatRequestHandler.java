package org.risegerdb.web.communication.serverhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.risegerdb.web.protocol.HeartBeatRequestPacket;
import org.risegerdb.web.protocol.HeartBeatResponsePacket;

public class HeartBeatRequestHandler extends SimpleChannelInboundHandler<HeartBeatRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HeartBeatRequestPacket heartBeatRequestPacket) throws Exception {
        channelHandlerContext.writeAndFlush(new HeartBeatResponsePacket());
    }
}
