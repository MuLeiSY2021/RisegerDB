package org.reseger.jrdbc.driver.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.reseger.jrdbc.driver.connector.Connector;
import org.riseger.protoctl.codec.ProtocolCodec;

import java.util.ArrayList;
import java.util.List;

public class ClientHandlerManager extends ChannelInitializer<SocketChannel> {
    private final Connector connector;

    private final List<ChannelHandler> handlers;

    public ClientHandlerManager(Connector connector) {
        this.connector = connector;
        this.handlers = new ArrayList<>();
        handlers.add(new ProtocolCodec());
        handlers.add(new ResponseInBoundHandler(connector));
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        for (ChannelHandler handler : handlers) {
            ch.pipeline().addLast(handler);
        }
    }
}
