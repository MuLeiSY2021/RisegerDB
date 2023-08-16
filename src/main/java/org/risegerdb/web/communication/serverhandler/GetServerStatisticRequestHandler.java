package org.risegerdb.web.communication.serverhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.risegerdb.web.protocol.GetServerStatisticResponsePacket;
import org.risegerdb.web.protocol.GetServerStatisticResquestPacket;
import org.risegerdb.web.util.Statistic;

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
