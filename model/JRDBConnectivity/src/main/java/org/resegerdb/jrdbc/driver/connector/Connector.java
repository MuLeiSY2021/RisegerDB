package org.resegerdb.jrdbc.driver.connector;

import io.netty.channel.Channel;
import org.resegerdb.jrdbc.driver.session.PreloadSession;

public class    Connector {

    private final Channel channel;

    private Connector(Channel channel) {
        this.channel = channel;
    }

    public static Connector connet(String host, int port) throws InterruptedException {
        return new Connector(null);
//        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
//
//        try {
//            Bootstrap bootstrap = new Bootstrap();
//            bootstrap.group(workerGroup)
//                    .option(ChannelOption.SO_KEEPALIVE, true)
//                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
//                    .option(ChannelOption.TCP_NODELAY, true)
//                    .channel(NioSocketChannel.class);
//
//            Channel channel = bootstrap.connect(host, port).sync().channel();
//            return new Connector(channel);
//
//        } finally {
//            workerGroup.shutdownGracefully();
//        }
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
