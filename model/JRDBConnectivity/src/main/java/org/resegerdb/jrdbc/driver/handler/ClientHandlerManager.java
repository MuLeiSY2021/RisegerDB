package org.resegerdb.jrdbc.driver.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.riseger.main.entry.handler.PreloadMessageInboundHandler;

import java.util.ArrayList;
import java.util.List;

public class ClientHandlerManager extends ChannelInitializer<SocketChannel> {
    private static final List<ChannelHandler> handlers = new ArrayList<>();
    static {
        handlers.add(new PreloadMessageInboundHandler());
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        for (ChannelHandler handler:handlers) {
            ch.pipeline().addLast(handler);
        }
    }
}
