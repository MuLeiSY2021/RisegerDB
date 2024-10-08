package org.riseger.main.entry.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.riseger.protocol.codec.ProtocolCodec;

public class HandlerManager extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline()
                .addLast(new ProtocolCodec())
                .addLast(new TextSQLMessageInboundHandler())
                .addLast(new CommandSQLMessageInboundHandler());
    }
}
