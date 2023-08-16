package com.easyarch.websocket;

import com.easyarch.entity.Session;
import com.easyarch.util.LoginUtil;
import com.easyarch.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WebSocketAuthHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Map<String, String> userMap = new HashMap<>();

    static {
        userMap.put("abc", "123");
        userMap.put("qwe", "456");
        userMap.put("zzz", "123");
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        if (!LoginUtil.hasLogin(ctx.channel())) {
            String uri = req.uri();
            System.out.println(uri);
            String[] split = uri.split("/");
            String userName = split[2];
            String password = split[3];
            if (userMap.get(userName).equals(password)) {
                String userId = UUID.randomUUID().toString();
                SessionUtil.bindSession(new Session(userId, userName), ctx.channel());
                req.setUri("/"+split[1]);
            } else {
                ctx.channel().writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                        HttpResponseStatus.UNAUTHORIZED));
                ctx.channel().close();
            }

        } else {
            ctx.pipeline().remove(this);
        }
        ctx.fireChannelRead(req.retain());
    }


    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        if (LoginUtil.hasLogin(ctx.channel())) {
            System.out.println("当前用户已登录，无需再次校验");
        } else {
            System.out.println("未登录终止连接");
        }
    }
}
