package org.resegerdb.jrdbc.driver;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;



public class Connection {
    private static Logger

    public static Connection connet(String host,int port) throws InterruptedException {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new SimpleClientHandler());
                        }
                    });

            Channel channel = bootstrap.connect(host, port).sync().channel();
            LOG.info("Connect");
            System.out.println("Client connected to " + host + ":" + port);

            // 在这里发送消息
            String messageToSend = "Hello, server!";
            ByteBuf byteBuf = channel.alloc().buffer();
            byteBuf.writeBytes(messageToSend.getBytes());
            channel.writeAndFlush(byteBuf);

            // 关闭客户端
            channel.closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    private static class SimpleClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
            // 接收到服务器返回的消息
            String receivedMessage = msg.toString(io.netty.util.CharsetUtil.UTF_8);
            System.out.println("Received message from server: " + receivedMessage);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            // 处理异常情况
            cause.printStackTrace();
            ctx.close();
        }
    }

    public void close() {

    }

    void flush(Session session) {

    }

    public void createSession() {
    }

    public PreloadSession preloadSession() {
        return new PreloadSession(this);
    }

    public void send(byte[] bytes) {

    }

    public Result getResponse() {
        return null;
    }
}
