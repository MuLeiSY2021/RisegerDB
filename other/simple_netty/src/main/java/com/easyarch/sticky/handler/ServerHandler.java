package com.easyarch.sticky.handler;

import com.easyarch.protocol.LoginRequestPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * 粘包&拆包演示ServerHandler
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LoginRequestPacket byteBuf = (LoginRequestPacket) msg;
//        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(new Date() + "server read data------->" + byteBuf.getUserId() + byteBuf.getUserName());

    }
}
