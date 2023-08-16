package org.resegerdb.jrdbc.driver.handler.clienthandler;

import com.easyarch.protocol.GroupMessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

public class GroupMessageResponseHandler extends SimpleChannelInboundHandler<GroupMessageResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, GroupMessageResponsePacket groupMessageResponsePacket) throws Exception {
        System.out.println(new Date()+"群消息，来自："+groupMessageResponsePacket.getFromGroupId()+"---->"+groupMessageResponsePacket.getFromUser()+":"+groupMessageResponsePacket.getMsg());
    }
}
