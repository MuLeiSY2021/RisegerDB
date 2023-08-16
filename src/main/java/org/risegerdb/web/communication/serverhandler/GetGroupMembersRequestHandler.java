package org.risegerdb.web.communication.serverhandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import org.risegerdb.web.entity.Session;
import org.risegerdb.web.protocol.GetGroupMembersRequestPacket;
import org.risegerdb.web.protocol.GetGroupMembersResponsePacket;
import org.risegerdb.web.util.SessionUtil;

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
