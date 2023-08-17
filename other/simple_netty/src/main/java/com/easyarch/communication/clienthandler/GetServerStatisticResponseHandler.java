package com.easyarch.communication.clienthandler;

import com.easyarch.protocol.GetServerStatisticResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class GetServerStatisticResponseHandler extends SimpleChannelInboundHandler<GetServerStatisticResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, GetServerStatisticResponsePacket getServerStatisticResponsePacket) throws Exception {
        System.out.println("当前服务器连接用户数:" + getServerStatisticResponsePacket.getOnlineUsers() +
                " 数据处理的字节数:" + getServerStatisticResponsePacket.getProcessingBytes() +
                " 消息转发数:" + getServerStatisticResponsePacket.getInfoTransferSum());
    }
}