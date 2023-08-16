package com.easyarch.communication.serverhandler;

import com.easyarch.entity.Session;
import com.easyarch.protocol.GetGroupMembersRequestPacket;
import com.easyarch.protocol.GetGroupMembersResponsePacket;
import com.easyarch.protocol.GetServerStatisticResponsePacket;
import com.easyarch.protocol.GetServerStatisticResquestPacket;
import com.easyarch.util.SessionUtil;
import com.easyarch.util.Statistic;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

import java.util.ArrayList;
import java.util.List;

public class GetServerStatisticRequestHandler extends SimpleChannelInboundHandler<GetServerStatisticResquestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, GetServerStatisticResquestPacket getServerStatisticResquestPacket) throws Exception {
        GetServerStatisticResponsePacket responsePacket = new GetServerStatisticResponsePacket();
        responsePacket.setInfoTransferSum(Statistic.getInfoTransferSum());
        responsePacket.setOnlineUsers(Statistic.getOnlineUsers());
        responsePacket.setProcessingBytes(Statistic.getProcessingBytes());

        channelHandlerContext.writeAndFlush(responsePacket);
    }
}
