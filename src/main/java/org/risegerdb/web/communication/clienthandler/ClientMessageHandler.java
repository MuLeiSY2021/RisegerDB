package org.risegerdb.web.communication.clienthandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.risegerdb.web.protocol.MessageResponsePacket;

import java.util.Date;

public class ClientMessageHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {
//    @Override
//    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageResponsePacket messageResponsePacket) throws Exception {
//        System.out.println(new Date() + "receive server message:" + messageResponsePacket.getMessage());
//
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageResponsePacket messageResponsePacket) throws Exception {
        String fromUserId = messageResponsePacket.getFromUserId();
        String fromUserName = messageResponsePacket.getFromUserName();
        System.out.println(new Date() + "-->" + fromUserId + ":" + fromUserName +"---->"+ messageResponsePacket.getMessage());

    }
}
