package com.easyarch.communication.serverhandler;

import com.easyarch.protocol.JoinGroupRequestPacket;
import com.easyarch.protocol.JoinGroupResponsePacket;
import com.easyarch.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

public class JoinGroupRequestHandler extends SimpleChannelInboundHandler<JoinGroupRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, JoinGroupRequestPacket joinGroupRequestPacket) throws Exception {
        ChannelGroup channelGroup = SessionUtil.joinGroup(joinGroupRequestPacket.getGroupId(), channelHandlerContext.channel());

        JoinGroupResponsePacket joinGroupResponsePacket = new JoinGroupResponsePacket();

        joinGroupResponsePacket.setGroupId(joinGroupRequestPacket.getGroupId());
        joinGroupResponsePacket.setSuccess(true);
        joinGroupResponsePacket.setUserName(SessionUtil.getSession(channelHandlerContext.channel()).getUserName());

        channelGroup.writeAndFlush(joinGroupResponsePacket);
    }
}
