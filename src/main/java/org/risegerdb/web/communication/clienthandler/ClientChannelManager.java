package org.risegerdb.web.communication.clienthandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.risegerdb.web.communication.commonhandler.PacketDecoder;
import org.risegerdb.web.communication.commonhandler.PacketEncoder;
import org.risegerdb.web.communication.commonhandler.SplitterHandler;

public class ClientChannelManager extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new HeartBeatTimerHandler());
        socketChannel.pipeline().addLast(new SplitterHandler());
        socketChannel.pipeline().addLast(new PacketDecoder());
        socketChannel.pipeline().addLast(new SimpClientHandler());
        socketChannel.pipeline().addLast(new ClientMessageHandler());
        socketChannel.pipeline().addLast(new PacketEncoder());
        socketChannel.pipeline().addLast(new CreateGroupResponseHandler());
        socketChannel.pipeline().addLast(new JoinGroupResponseHandler());
        socketChannel.pipeline().addLast(new GroupMessageResponseHandler());
        socketChannel.pipeline().addLast(new QuitGroupResponseHandler());
        socketChannel.pipeline().addLast(new GetGroupMembersResponseHandler());
        socketChannel.pipeline().addLast(new GetServerStatisticResponseHandler());
    }
}
