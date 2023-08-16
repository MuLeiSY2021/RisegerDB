package org.risegerdb.web.sticky;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.risegerdb.web.communication.commonhandler.PacketEncoder;
import org.risegerdb.web.sticky.handler.ClientHandler;

/**
 * 粘包&拆包演示Client
 */
public class Client {

    private static final int MAX_RETRY = 3;

    public static void main(String[] args) {
        NioEventLoopGroup worker = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(worker)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new ClientHandler());
                        channel.pipeline().addLast(new PacketEncoder());
                    }
                });
        bootstrap.connect("localhost", 9999);
    }
}
