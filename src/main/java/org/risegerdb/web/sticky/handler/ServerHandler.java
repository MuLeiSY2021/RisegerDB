package org.risegerdb.web.sticky.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.risegerdb.web.protocol.LoginRequestPacket;

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
