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
import org.reseger.jrdbc.driver.session.TextSQLMessageSession;
import org.riseger.protoctl.otherProtocol.ProgressBar;
import org.riseger.protoctl.packet.response.BasicResponse;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Getter
public class Connection {

    private final LinkedBlockingDeque<BasicResponse> resultQueue = new LinkedBlockingDeque<>();
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

    public static Connection connect(String host, int port, ProgressBar progressBar) throws InterruptedException {
        Connection connection = new Connection(new NioEventLoopGroup());
        progressBar.loading(10);
        Bootstrap bootstrap = new Bootstrap();
        progressBar.loading(10);
        EventLoopGroup workerGroup = connection.getEventLoopGroup();
        progressBar.loading(10);
        bootstrap.group(workerGroup)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(65535))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ClientHandlerManager(connection))
                .channel(NioSocketChannel.class);
        progressBar.loading(10);
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

    public BasicResponse getResult() throws InterruptedException {
        try {
            lock.lock();
            if (resultQueue.isEmpty()) {
                cond.await();
            }
            return resultQueue.poll();
        } finally {
            lock.unlock();
        }
    }

    public void setResult(BasicResponse result) {
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

    public TextSQLMessageSession sqlText() {
        return new TextSQLMessageSession(this);
    }
}
