package org.resegerdb.jrdbc.driver.connector;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.resegerdb.jrdbc.driver.session.PreloadSession;

public class    Connector {

    private final Channel channel;

    private Connector(Channel channel) {
        this.channel = channel;
    }

    public static Connector connect(String host, int port) throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .channel(NioSocketChannel.class);

            Channel channel = bootstrap.connect(host, port).sync().channel();
            return new Connector(channel);

        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public void close() {
        if (channel != null) {
            try {
                channel.closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public PreloadSession preload() {
        return new PreloadSession(this.channel);
    }
}
