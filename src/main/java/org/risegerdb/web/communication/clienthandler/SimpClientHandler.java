package org.risegerdb.web.communication.clienthandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.risegerdb.web.protocol.LoginResponsePacket;
import org.risegerdb.web.util.LoginUtil;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class SimpClientHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println(new Date() + "client write data");
//
//            ByteBuf byteBuf = getByteBuf(ctx);
//            ctx.channel().writeAndFlush(byteBuf);
        System.out.println("客户端登录开始...." + new Date());

//        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
//        loginRequestPacket.setUserId(1);
//        loginRequestPacket.setUserName("name");
//        loginRequestPacket.setPassword("123");
//
////        给登录请求编码
////        ByteBuf byteBuf = PacketCodec.INSTANCE.encode(ctx.alloc().buffer(), loginRequestPacket);
//
//        //写数据
//        ctx.channel().writeAndFlush(loginRequestPacket);

    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket packet) throws Exception {
//        ByteBuf byteBuf = (ByteBuf) msg;
//        BaseMsgPacket baseMsgPacket = PacketCodec.INSTANCE.decode(byteBuf);
//        if (baseMsgPacket instanceof LoginResponsePacket) {
//            LoginResponsePacket responsePacket = (LoginResponsePacket) baseMsgPacket;
            //做登录校验
            if (packet.isSuccess()) {
                LoginUtil.markLogin(ctx.channel());
                System.out.println("登录成功！id:"+packet.getUserId());
            } else {
                System.out.println("登录失败，原因：" + packet.getInfo());
            }
//        }
//        System.out.println(byteBuf.toString(StandardCharsets.UTF_8));

    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        ByteBuf buffer = ctx.alloc().buffer();

        buffer.writeBytes(("this is a message").getBytes(StandardCharsets.UTF_8));

        return buffer;
    }
}
