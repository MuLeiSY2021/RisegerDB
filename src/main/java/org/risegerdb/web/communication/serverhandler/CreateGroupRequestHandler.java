package org.risegerdb.web.communication.serverhandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.DefaultChannelGroup;
import org.risegerdb.web.protocol.CreateGroupRequestPacket;
import org.risegerdb.web.protocol.CreateGroupResponsePacket;
import org.risegerdb.web.util.SessionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateGroupRequestHandler extends SimpleChannelInboundHandler<CreateGroupRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, CreateGroupRequestPacket createGroupRequestPacket) throws Exception {
        List<String> userIds = createGroupRequestPacket.getUserIds();

        DefaultChannelGroup channelGroup = new DefaultChannelGroup(channelHandlerContext.executor());

        List<String> userNames = new ArrayList<>();
        for (String userId : userIds) {
            Channel channel = SessionUtil.getChannel(userId);
            if (channel != null) {
                channelGroup.add(channel);
                userNames.add(SessionUtil.getSession(channel).getUserName());
            }
        }
        String groupId = UUID.randomUUID().toString();
        SessionUtil.createGroup(groupId, channelGroup);
        CreateGroupResponsePacket responsePacket = new CreateGroupResponsePacket();
        responsePacket.setSuccess(true);
        responsePacket.setGroupId(groupId);
        responsePacket.setUserNames(userNames);
        responsePacket.setCreator(SessionUtil.getSession(channelHandlerContext.channel()).getUserName());

        channelGroup.writeAndFlush(responsePacket);
    }
}
