package org.risegerdb.web.communication.serverhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.risegerdb.web.communication.commonhandler.AuthHandler;
import org.risegerdb.web.communication.commonhandler.ChatIdleStateHandler;
import org.risegerdb.web.communication.commonhandler.PacketCodecHandler;
import org.risegerdb.web.communication.commonhandler.SplitterHandler;

public class ServerChannelManager extends ChannelInitializer<NioSocketChannel> {
    @Override
    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
        nioSocketChannel.pipeline().addLast(new ChatIdleStateHandler());
        nioSocketChannel.pipeline().addLast(new SplitterHandler());
        nioSocketChannel.pipeline().addLast(PacketCodecHandler.INSTANCE);
//        nioSocketChannel.pipeline().addLast(new PacketDecoder());
        nioSocketChannel.pipeline().addLast(new SimpleServerHandler());
        nioSocketChannel.pipeline().addLast(new HeartBeatRequestHandler());
        nioSocketChannel.pipeline().addLast(new AuthHandler());
        nioSocketChannel.pipeline().addLast(ServerMessageHandler.INSTANCE);
//        nioSocketChannel.pipeline().addLast(new PacketEncoder());
        nioSocketChannel.pipeline().addLast(new CreateGroupRequestHandler());
        nioSocketChannel.pipeline().addLast(new JoinGroupRequestHandler());
        nioSocketChannel.pipeline().addLast(new GroupMessageRequestHandler());
        nioSocketChannel.pipeline().addLast(new QuitGroupRequestHandler());
        nioSocketChannel.pipeline().addLast(new GetGroupMembersRequestHandler());
        nioSocketChannel.pipeline().addLast(new GetServerStatisticRequestHandler());
    }
}
