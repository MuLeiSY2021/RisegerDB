package com.easyarch.communication.serverhandler;

import com.easyarch.protocol.BaseMsgPacket;
import com.easyarch.protocol.LoginRequestPacket;
import com.easyarch.protocol.LoginResponsePacket;
import com.easyarch.protocol.PacketCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.HashMap;
import java.util.Map;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static final Map<Long, String> userMap = new HashMap<>();

    static {
        userMap.put(1L, "123");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;

        BaseMsgPacket baseMsgPacket = PacketCodec.INSTANCE.decode(byteBuf);

        LoginResponsePacket responsePacket = new LoginResponsePacket();
        if (baseMsgPacket instanceof LoginRequestPacket) {
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) baseMsgPacket;
            responsePacket.setVersion(loginRequestPacket.getVersion());
            //做登录校验
            if (loginRequestPacket.getPassword().equals(userMap.get(loginRequestPacket.getUserId()))) {
                responsePacket.setUserName(loginRequestPacket.getUserName());
                responsePacket.setSuccess(true);
            } else {
                responsePacket.setSuccess(false);
                responsePacket.setInfo("密码错误");
            }
        }
        //编码
        ByteBuf buffer = PacketCodec.INSTANCE.encode(ctx.alloc().buffer(), responsePacket);
        //写数据
        ctx.channel().writeAndFlush(buffer);
//        System.out.println(new Date() + "server read data:" + byteBuf.toString(StandardCharsets.UTF_8));
//
//        System.out.println(new Date() + "server write data");
//
//
//        ByteBuf buffer = ctx.alloc().buffer();
//        buffer.writeBytes(("server get message:" + byteBuf.toString(StandardCharsets.UTF_8) + "at" + new Date()).getBytes(StandardCharsets.UTF_8));
//        ctx.channel().writeAndFlush(buffer);


    }
}
