package org.risegerdb.web.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class WebSocketServer {
    public static void main(String[] args) {

        NioEventLoopGroup boss = new NioEventLoopGroup(); //用它来处理连接事件
        NioEventLoopGroup worker = new NioEventLoopGroup(); //用它来处理读写事件
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                //用来明确谁处理连接事件、谁处理读写事件
                .group(boss, worker)
                //当前程序使用什么样的Channel来做数据传输
                .channel(NioServerSocketChannel.class)
                //给serverSocketChannel添加一些参数，比如下面的：so_backlog
                //用来指定临时存放已完成三次握手的连接的队列的最大长度
                .option(ChannelOption.SO_BACKLOG, 1024)
                //给socketchannel添加一些参数，比如下面的：so_keepalive
                // 用来指定是否开启TCP心跳机制
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                //当连接进来之后，需要如何处理数据，由下面的handler来决定
                //注意这里的编排顺序与各handler的数据传播息息相关，详见:ServerMessageHandler
                .childHandler(new HandlerManager());
        //针对启动器，绑定相应的端口
        bind(serverBootstrap, 8000);
    }


    private static void bind(final ServerBootstrap bootstrap, final int port) {
        bootstrap.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("端口" + port + "绑定成功");
                } else {
                    System.err.println("端口" + port + "绑定失败");
                    bind(bootstrap, port + 1);
                }
            }
        });
    }
}
