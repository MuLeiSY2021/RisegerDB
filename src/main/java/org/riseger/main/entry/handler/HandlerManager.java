package org.riseger.main.entry.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.riseger.protoctl.codec.ProtoctlCodec;
import org.riseger.protoctl.handler.ProtocolMessageInboundHandler;
import org.riseger.protoctl.handler.ProtocolMessageOutboundHandler;

import java.util.ArrayList;
import java.util.List;

public class HandlerManager extends ChannelInitializer<SocketChannel> {
    private static final List<ChannelHandler> handlers = new ArrayList<>();
    static {
        handlers.add(new ProtoctlCodec());
        handlers.add(new ProtocolMessageInboundHandler());
        handlers.add(new ProtocolMessageOutboundHandler());
        handlers.add(new PreloadMessageInboundHandler());
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        for (ChannelHandler handler:handlers) {
            ch.pipeline().addLast(handler);
        }
    }
}
