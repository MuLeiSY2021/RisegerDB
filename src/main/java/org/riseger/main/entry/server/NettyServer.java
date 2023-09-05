package org.riseger.main.entry.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;
import org.riseger.main.entry.handler.HandlerManager;

public class NettyServer implements Server, Runnable {
    private static final Logger LOG = Logger.getLogger(NettyServer.class);

    @Override
    public void run() {
        bootstrap();
    }

    public void bootstrap() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HandlerManager())
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = bootstrap.bind(10086).sync();
            LOG.info("Netty server connected ,waiting for port 10086 requests");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOG.error(e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
