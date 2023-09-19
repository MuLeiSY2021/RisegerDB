package org.reseger.jrdbc.driver.connector;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import org.reseger.jrdbc.driver.handler.ClientHandlerManager;
import org.reseger.jrdbc.driver.session.PreloadSession;
import org.reseger.jrdbc.driver.session.SearchSession;

import javax.xml.ws.Response;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Getter
public class Connection {

    private final LinkedBlockingDeque<Response<?>> resultQueue = new LinkedBlockingDeque<>();
    private final EventLoopGroup eventLoopGroup;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition cond = lock.newCondition();
    private Channel channel;

    public Connection(EventLoopGroup eventLoopGroup) {
        this.eventLoopGroup = eventLoopGroup;
    }

    public static Connection connect(String host, int port) throws InterruptedException {
        Connection connection = new Connection(new NioEventLoopGroup());
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup workerGroup = connection.getEventLoopGroup();
        bootstrap.group(workerGroup)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(65535))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ClientHandlerManager(connection))
                .channel(NioSocketChannel.class);
        connection.channel = bootstrap.connect(host, port).sync().channel();
        return connection;
    }

    public void close() {
        if (eventLoopGroup != null) {
            eventLoopGroup.shutdownGracefully();
        }
    }

    public PreloadSession preload() {
        return new PreloadSession(this);
    }

    public Response<?> getResult() throws InterruptedException {
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

    public void setResult(Response<?> result) {
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