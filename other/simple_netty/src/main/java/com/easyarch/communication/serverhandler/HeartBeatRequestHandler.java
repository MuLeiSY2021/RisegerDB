package com.easyarch.communication.serverhandler;

import com.easyarch.protocol.HeartBeatRequestPacket;
import com.easyarch.protocol.HeartBeatResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class HeartBeatRequestHandler extends SimpleChannelInboundHandler<HeartBeatRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HeartBeatRequestPacket heartBeatRequestPacket) throws Exception {
        channelHandlerContext.writeAndFlush(new HeartBeatResponsePacket());
    }
}
