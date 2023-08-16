package org.risegerdb.web.communication.serverhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import org.risegerdb.web.protocol.QuitGroupRequestPacket;
import org.risegerdb.web.protocol.QuitGroupResponsePacket;
import org.risegerdb.web.util.SessionUtil;

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
