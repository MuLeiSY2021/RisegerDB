package org.riseger.main.entry.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.riseger.protoctl.codec.ProtocolCodec;

import java.util.ArrayList;
import java.util.List;

public class HandlerManager extends ChannelInitializer<SocketChannel> {
    private static final List<ChannelHandler> handlers = new ArrayList<>();

    static {
        handlers.add(new ProtocolCodec());
        handlers.add(new PreloadMessageInboundHandler());
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        for (ChannelHandler handler : handlers) {
            ch.pipeline().addLast(handler);
        }
    }
}
