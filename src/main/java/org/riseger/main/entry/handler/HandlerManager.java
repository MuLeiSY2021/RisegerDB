package org.riseger.main.entry.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.riseger.protoctl.codec.ProtocolCodec;

public class HandlerManager extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline()
                .addLast(new ProtocolCodec())
                .addLast(new PreloadMessageInboundHandler())
                .addLast(new SearchMessageInboundHandler());
    }
}
