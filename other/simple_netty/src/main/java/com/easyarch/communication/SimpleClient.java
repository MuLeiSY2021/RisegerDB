package com.easyarch.communication;

import com.easyarch.communication.clienthandler.ClientChannelManager;
import com.easyarch.communication.consolemanager.ConsoleCommandManager;
import com.easyarch.communication.consolemanager.LoginCommand;
import com.easyarch.util.LoginUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class SimpleClient {

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
                .handler(new ClientChannelManager());
        connect(bootstrap, "localhost", 8000, MAX_RETRY);
    }

    private static void connect(Bootstrap bootstrap, String host, int port, int retryCount) {
        bootstrap.connect(host, port).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("connect success");
                    //启动额外线程用于控制台输入消息
                    startMessageConsole(future.channel());
                } else if (retryCount == 0) {
                    System.err.println("retry fail give up connect");
                    bootstrap.config().group().shutdownGracefully();
                } else {
                    int order = (MAX_RETRY - retryCount) + 1;
                    int delay = 1 << order;
                    System.err.println("connect fail retry to connect.... times:" + order);
                    bootstrap.config().group().schedule(new Runnable() {
                        int retry = retryCount;

                        @Override
                        public void run() {
                            connect(bootstrap, host, port, --retry);
                        }
                    }, delay, TimeUnit.SECONDS);

                }
            }
        });
    }

//    private static void startMessageConsole(Channel channel) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (!Thread.interrupted()) {
//                    if (LoginUtil.hasLogin(channel)) {
//                        System.out.println("请输入消息：");
//                        Scanner scanner = new Scanner(System.in);
//                        String nextLine = scanner.nextLine();
//                        MessageRequestPacket requestPacket = new MessageRequestPacket();
//                        requestPacket.setMessage(nextLine);
//                        channel.writeAndFlush(requestPacket);
//                    }
//                }
//            }
//        }).start();
//    }


    private static void startMessageConsole(Channel channel) {
        Scanner scanner = new Scanner(System.in);
        ConsoleCommandManager consoleCommandManager = new ConsoleCommandManager();
        LoginCommand loginCommand = new LoginCommand();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()) {
                    if (!LoginUtil.hasLogin(channel)) {
                        loginCommand.exec(scanner, channel);
                    } else {
                        consoleCommandManager.exec(scanner, channel);

                    }
                }
            }
        }).start();
    }
}
