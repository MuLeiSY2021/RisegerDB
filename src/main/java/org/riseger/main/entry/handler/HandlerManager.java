package org.riseger.main.entry.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.util.ArrayList;
import java.util.List;

public class HandlerManager extends ChannelInitializer<SocketChannel> {
    private static final List<? extends ChannelHandler> handlers = new ArrayList<>();
    static {

    }


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        for (ChannelHandler handler:handlers) {
            ch.pipeline().addLast(handler);
        }
    }
}
