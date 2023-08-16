package org.risegerdb.web.communication.serverhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.risegerdb.web.entity.Session;
import org.risegerdb.web.protocol.LoginRequestPacket;
import org.risegerdb.web.protocol.LoginResponsePacket;
import org.risegerdb.web.util.LoginUtil;
import org.risegerdb.web.util.SessionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SimpleServerHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {


    private static final Map<String, String> userMap = new HashMap<>();

    static {
        userMap.put("abc", "123");
        userMap.put("qwe", "456");
        userMap.put("zzz", "123");
        userMap.put("bcd", "567");
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket packet) throws Exception {
//        ByteBuf byteBuf = (ByteBuf) msg;

//        BaseMsgPacket baseMsgPacket = PacketCodec.INSTANCE.decode(byteBuf);

        LoginResponsePacket responsePacket = new LoginResponsePacket();
//        if (baseMsgPacket instanceof LoginRequestPacket) {
//            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) baseMsgPacket;
        responsePacket.setVersion(packet.getVersion());
        //做登录校验
        if (packet.getPassword().equals(userMap.get(packet.getUserName()))) {
            responsePacket.setUserName(packet.getUserName());
            responsePacket.setSuccess(true);
            LoginUtil.markLogin(ctx.channel());
            String userId = UUID.randomUUID().toString();
            SessionUtil.bindSession(new Session(userId,packet.getUserName()), ctx.channel());
            responsePacket.setUserId(userId);
        } else {
            responsePacket.setSuccess(false);
            responsePacket.setInfo("密码错误");
        }
//        }
        //编码
//        ByteBuf buffer = PacketCodec.INSTANCE.encode(ctx.alloc().buffer(), responsePacket);
        //写数据
        ctx.channel().writeAndFlush(responsePacket);
//        System.out.println(new Date() + "server read data:" + byteBuf.toString(StandardCharsets.UTF_8));
//
//        System.out.println(new Date() + "server write data");
//
//
//        ByteBuf buffer = ctx.alloc().buffer();
//        buffer.writeBytes(("server get message:" + byteBuf.toString(StandardCharsets.UTF_8) + "at" + new Date()).getBytes(StandardCharsets.UTF_8));
//        ctx.channel().writeAndFlush(buffer);


    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SessionUtil.unBindSession(ctx.channel());
    }
}
