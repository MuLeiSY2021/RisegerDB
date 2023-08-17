package org.risegerdb.web.communication.serverhandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.risegerdb.web.entity.Session;
import org.risegerdb.web.protocol.MessageRequestPacket;
import org.risegerdb.web.protocol.MessageResponsePacket;
import org.risegerdb.web.util.SessionUtil;
import org.risegerdb.web.util.Statistic;

@ChannelHandler.Sharable
public class ServerMessageHandler extends SimpleChannelInboundHandler<MessageRequestPacket> implements SengMessage {
    //    @Override
//    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageRequestPacket messageRequestPacket) throws Exception {
//        System.out.println(new Date() + "receive client message:" + messageRequestPacket.getMessage());
//
//        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
//        messageResponsePacket.setMessage("[sever response]: " + messageRequestPacket.getMessage());
//        //从pipeline的最后一个outboundhandler开始执行
////        channelHandlerContext.channel().writeAndFlush(messageResponsePacket);
//        //从当前handler开始，找到当前handler的前一个outboundhandler开始执行
//        channelHandlerContext.writeAndFlush(messageResponsePacket);
//
//    }
    public static final ServerMessageHandler INSTANCE = new ServerMessageHandler();

    private ServerMessageHandler() {

    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageRequestPacket messageRequestPacket) throws Exception {
//        System.out.println(new Date() + "receive client message:" + messageRequestPacket.getMessage());
        Session session = SessionUtil.getSession(channelHandlerContext.channel());
        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setFromUserId(session.getUserId());
        messageResponsePacket.setFromUserName(session.getUserName());
        messageResponsePacket.setMessage(messageRequestPacket.getMessage());

        Channel channel = SessionUtil.getChannel(messageRequestPacket.getToUserId());
        if (channel != null && LoginUtil.hasLogin(channel)) {
            fixStatistic(messageResponsePacket.getMessage().getBytes().length,0);
            channel.writeAndFlush(messageResponsePacket);
        } else {
            MessageResponsePacket notLoginMessage = new MessageResponsePacket();
            notLoginMessage.setMessage(messageRequestPacket.getToUserId() + "不在线");
            channelHandlerContext.channel().writeAndFlush(notLoginMessage);
        }

    }

    @Override
    public void fixStatistic(int byteNum, int membersNum) {
        Statistic.addProcessingBytes(byteNum);
        Statistic.addInfoTransfer();
    }
}
