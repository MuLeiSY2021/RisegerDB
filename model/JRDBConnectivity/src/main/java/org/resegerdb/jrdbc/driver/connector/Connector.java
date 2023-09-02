package org.resegerdb.jrdbc.driver.connector;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import org.resegerdb.jrdbc.driver.handler.ClientHandlerManager;
import org.resegerdb.jrdbc.driver.result.Result;
import org.resegerdb.jrdbc.driver.session.PreloadSession;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Getter
public class Connector {

    private Channel channel;

    private final LinkedBlockingDeque<Result> resultQueue = new LinkedBlockingDeque<>();

    private ReentrantLock lock = new ReentrantLock();

    private Condition cond = lock.newCondition();

    public static Connector connect(String host, int port) throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Connector connector = new Connector();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ClientHandlerManager(connector))
                .channel(NioSocketChannel.class);
        connector.channel = bootstrap.connect(host, port).sync().channel();
        return connector;
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
        return new PreloadSession(this);
    }

    public Result getResult() throws InterruptedException {
        try {
            lock.lock();
            if (resultQueue.size() == 0) {
                cond.await();
            }
            return resultQueue.poll();
        } finally {
            lock.unlock();
        }
    }

    public void setResult(Result result) {
        try {
            lock.lock();
            this.resultQueue.push(result);
            cond.signal();
        } finally {
            lock.unlock();
        }
    }
}
