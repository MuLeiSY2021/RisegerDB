package org.riseger.main.entry.handler;

import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Getter;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Getter
public abstract class TransponderHandler<I, E> extends SimpleChannelInboundHandler<I> {
    private final ReentrantLock lock = new ReentrantLock();

    private final Condition cond = lock.newCondition();

    private boolean failed = false;

    private Exception exception;

    public abstract E getE();

    public abstract void setE(E database);

    protected void sleep() throws InterruptedException {
        lock.lock();
        cond.await();
        lock.unlock();
    }

    protected void wake() {
        lock.lock();
        cond.signalAll();
        lock.unlock();
    }

    public void send(Exception e) {
        lock.lock();
        failed = true;
        this.exception = e;
        cond.signalAll();
        lock.unlock();
    }
}
