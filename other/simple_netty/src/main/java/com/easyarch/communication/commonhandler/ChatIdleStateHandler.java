package com.easyarch.communication.commonhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class ChatIdleStateHandler extends IdleStateHandler {

    private static final int READ_IDLE_TIME = 9000000;

    public ChatIdleStateHandler() {
        super(READ_IDLE_TIME, 0, 0, TimeUnit.SECONDS);
    }


    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        System.out.println(READ_IDLE_TIME + "秒未读到连接数据，关闭连接");
        ctx.channel().close();
    }
}
