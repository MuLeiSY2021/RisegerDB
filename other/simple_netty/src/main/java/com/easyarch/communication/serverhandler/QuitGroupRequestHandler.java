package com.easyarch.communication.serverhandler;

import com.easyarch.protocol.QuitGroupRequestPacket;
import com.easyarch.protocol.QuitGroupResponsePacket;
import com.easyarch.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

public class QuitGroupRequestHandler extends SimpleChannelInboundHandler<QuitGroupRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, QuitGroupRequestPacket quitGroupRequestPacket) throws Exception {
        ChannelGroup channelGroup = SessionUtil.quitGroup(quitGroupRequestPacket.getGroupId(), channelHandlerContext.channel());
        QuitGroupResponsePacket quitGroupResponsePacket = new QuitGroupResponsePacket();

        quitGroupResponsePacket.setGroupId(quitGroupRequestPacket.getGroupId());
        quitGroupResponsePacket.setSuccess(true);
        quitGroupResponsePacket.setUserName(SessionUtil.getSession(channelHandlerContext.channel()).getUserName());

        channelGroup.writeAndFlush(quitGroupResponsePacket);
        channelHandlerContext.writeAndFlush(quitGroupResponsePacket);
    }
}
