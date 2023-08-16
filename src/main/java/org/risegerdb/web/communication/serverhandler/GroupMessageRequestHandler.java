package org.risegerdb.web.communication.serverhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import org.risegerdb.web.protocol.GroupMessageRequestPacket;
import org.risegerdb.web.protocol.GroupMessageResponsePacket;
import org.risegerdb.web.util.SessionUtil;
import org.risegerdb.web.util.Statistic;

public class GroupMessageRequestHandler extends SimpleChannelInboundHandler<GroupMessageRequestPacket> implements SengMessage {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, GroupMessageRequestPacket groupMessageRequestPacket) throws Exception {
        String groupId = groupMessageRequestPacket.getGroupId();
        GroupMessageResponsePacket responsePacket = new GroupMessageResponsePacket();
        responsePacket.setFromGroupId(groupId);
        responsePacket.setFromUser(SessionUtil.getSession(channelHandlerContext.channel()).getUserName());
        responsePacket.setMsg(groupMessageRequestPacket.getMsg());
        ChannelGroup group = SessionUtil.getGroup(groupId);
        fixStatistic(responsePacket.getMsg().getBytes().length,group.size());
        group.writeAndFlush(responsePacket);
    }


    @Override
    public void fixStatistic(int byteNum, int membersNum) {
        Statistic.addProcessingBytes(byteNum);
        Statistic.addInfoTransfer(membersNum);
    }
}
