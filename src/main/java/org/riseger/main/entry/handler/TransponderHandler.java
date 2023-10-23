package org.riseger.main.entry.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Getter;
import org.apache.log4j.Logger;
import org.riseger.protoctl.packet.request.TranspondRequest;
import org.riseger.protoctl.packet.response.BasicResponse;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Getter
public abstract class TransponderHandler<IN extends TranspondRequest> extends SimpleChannelInboundHandler<IN> {
    private final Logger LOG = Logger.getLogger(TransponderHandler.class);

    private final ReentrantLock lock = new ReentrantLock();

    private final Condition cond = lock.newCondition();

    private boolean failed = false;

    private Exception exception;

    private BasicResponse response;

    public BasicResponse getOut() throws InterruptedException {
        this.sleep();
        return response;
    }

    public void setOut(BasicResponse response) {
        this.response = response;
    }

    public void preHandle(ChannelHandlerContext ctx, IN msg) {

    }

    public abstract void handle(ChannelHandlerContext ctx, IN msg) throws Exception;

    public void postHandle(ChannelHandlerContext ctx, IN msg) {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IN msg) {
        LOG.info("Received Request: " + msg);

        msg.setTransponder(this);

        preHandle(ctx, msg);

        //Sleep
        try {
            LOG.info("Processing Request...");

            handle(ctx, msg);

            this.response = getOut();

            if (this.isFailed()) {
                response.failed(getException());
                LOG.info("Request processed failed.");

            } else {
                response.success();
                LOG.info("Request processed successfully.");
            }
        } catch (Exception e) {
            LOG.error("E", e);
            response.failed(e);
        }
        postHandle(ctx, msg);
        ctx.channel().writeAndFlush(response);

        LOG.info("Sending Request: " + response);

    }

    protected void sleep() throws InterruptedException {
        lock.lock();
        cond.await();
        lock.unlock();
    }

    public void wake() {
        lock.lock();
        cond.signalAll();
        lock.unlock();
    }

    public void send(Exception e) {
        lock.lock();
        this.failed = true;
        this.exception = e;
        cond.signalAll();
        lock.unlock();
    }
}
