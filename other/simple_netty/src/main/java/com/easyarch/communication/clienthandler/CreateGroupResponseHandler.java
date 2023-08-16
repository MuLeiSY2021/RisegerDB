package com.easyarch.communication.clienthandler;

import com.easyarch.protocol.CreateGroupResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class CreateGroupResponseHandler extends SimpleChannelInboundHandler<CreateGroupResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, CreateGroupResponsePacket createGroupResponsePacket) throws Exception {
        System.out.println("群组创建成功！群成员有：" + createGroupResponsePacket.getUserNames() + "群ID为：" + createGroupResponsePacket.getGroupId());
    }
}
