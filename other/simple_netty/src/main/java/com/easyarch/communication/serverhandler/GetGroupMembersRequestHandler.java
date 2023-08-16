package com.easyarch.communication.serverhandler;

import com.easyarch.entity.Session;
import com.easyarch.protocol.GetGroupMembersRequestPacket;
import com.easyarch.protocol.GetGroupMembersResponsePacket;
import com.easyarch.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

import java.util.ArrayList;
import java.util.List;

public class GetGroupMembersRequestHandler extends SimpleChannelInboundHandler<GetGroupMembersRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, GetGroupMembersRequestPacket getGroupMembersRequestPacket) throws Exception {
        ChannelGroup channelGroup = SessionUtil.getGroup(getGroupMembersRequestPacket.getGroupId());

        List<String> usersName = new ArrayList<>();
        List<String> usersId = new ArrayList<>();

        for (Channel userChannel : channelGroup) {
            Session userSession = SessionUtil.getSession(userChannel);
            if (userSession != null) {
                usersName.add(userSession.getUserName());
                usersId.add(userSession.getUserId());
            }
        }
        GetGroupMembersResponsePacket responsePacket = new GetGroupMembersResponsePacket();

        responsePacket.setUsersId(usersId);
        responsePacket.setUsersName(usersName);

        channelHandlerContext.writeAndFlush(responsePacket);
    }
}
