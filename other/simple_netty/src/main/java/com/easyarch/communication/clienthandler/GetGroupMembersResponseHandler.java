package com.easyarch.communication.clienthandler;

import com.easyarch.protocol.GetGroupMembersResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class GetGroupMembersResponseHandler extends SimpleChannelInboundHandler<GetGroupMembersResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, GetGroupMembersResponsePacket getGroupMembersResponsePacket) throws Exception {
        int len = getGroupMembersResponsePacket.getUsersId().size();
        System.out.println("用户列表：");
        for (int i = 0; i < len; i++) {
            System.out.println(
                    "用户姓名：" + getGroupMembersResponsePacket.getUsersName().get(i) +
                    " 用户ID：" + getGroupMembersResponsePacket.getUsersId().get(i));
        }
    }
}
