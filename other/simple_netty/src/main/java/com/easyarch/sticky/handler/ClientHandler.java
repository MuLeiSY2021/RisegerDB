package com.easyarch.sticky.handler;

import com.easyarch.protocol.LoginRequestPacket;
import com.easyarch.protocol.PacketCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 粘包&拆包演示ClientHandler
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date() + "client write data");

        for (int i = 0; i < 1000; i++) {
            LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
            loginRequestPacket.setUserId(1);
            loginRequestPacket.setUserName("aaa");
            ByteBuf buf = PacketCodec.INSTANCE.encode(ctx.alloc().buffer(), loginRequestPacket);
//            ByteBuf byteBuf = getByteBuf(ctx);
            ctx.channel().writeAndFlush(buf);
        }
        System.out.println("client finish");
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        ByteBuf buffer = ctx.alloc().buffer();

        buffer.writeBytes(("this is a loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong message !=end=!").getBytes(StandardCharsets.UTF_8));

        return buffer;
    }
}
