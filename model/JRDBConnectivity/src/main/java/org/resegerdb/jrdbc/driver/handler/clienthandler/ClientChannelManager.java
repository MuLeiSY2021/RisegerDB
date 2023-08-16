package org.resegerdb.jrdbc.driver.handler.clienthandler;

import org.resegerdb.jrdbc.driver.handler.commonhandler.PacketDecoder;
import org.resegerdb.jrdbc.driver.handler.commonhandler.PacketEncoder;
import org.resegerdb.jrdbc.driver.handler.commonhandler.SplitterHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ClientChannelManager extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new HeartBeatTimerHandler());
        socketChannel.pipeline().addLast(new SplitterHandler());
        socketChannel.pipeline().addLast(new PacketDecoder());
        socketChannel.pipeline().addLast(new ClientMessageHandler());
        socketChannel.pipeline().addLast(new PacketEncoder());
        socketChannel.pipeline().addLast(new GroupMessageResponseHandler());
    }
}
