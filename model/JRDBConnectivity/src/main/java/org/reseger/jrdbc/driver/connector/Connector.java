package org.reseger.jrdbc.driver.connector;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import org.reseger.jrdbc.driver.handler.ClientHandlerManager;
import org.reseger.jrdbc.driver.result.Result;
import org.reseger.jrdbc.driver.session.PreloadSession;
import org.reseger.jrdbc.driver.session.SearchSession;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Getter
public class Connector {

    private final LinkedBlockingDeque<Result> resultQueue = new LinkedBlockingDeque<>();
    private final EventLoopGroup eventLoopGroup;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition cond = lock.newCondition();
    private Channel channel;

    public Connector(EventLoopGroup eventLoopGroup) {
        this.eventLoopGroup = eventLoopGroup;
    }

    public static Connector connect(String host, int port) throws InterruptedException {
        Connector connector = new Connector(new NioEventLoopGroup());
        try {
            Bootstrap bootstrap = new Bootstrap();
            EventLoopGroup workerGroup = connector.getEventLoopGroup();
            bootstrap.group(workerGroup)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ClientHandlerManager(connector))
                    .channel(NioSocketChannel.class);
            connector.channel = bootstrap.connect(host, port).sync().channel();
        } catch (Exception e) {
            connector.close();
            throw e;
        }
        return connector;
    }

    public void close() {
        if (eventLoopGroup != null) {
            eventLoopGroup.shutdownGracefully();
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

    public SearchSession search() {
        return new SearchSession(this);
    }
}
