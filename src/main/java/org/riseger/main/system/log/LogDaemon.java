package org.riseger.main.system.log;

import org.riseger.main.constant.Config;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LogDaemon implements Runnable {
    private final ReadWriteLock READ_WRITE_LOCK = new ReentrantReadWriteLock();

    private final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(Config.LOG_COLLECT_ROUND);

    @Override
    public void run() {

    }

    public void read() {
        READ_WRITE_LOCK.readLock().lock();
    }

    private void write() {
        READ_WRITE_LOCK.writeLock().lock();
    }

    public void countDown(int count) {
        for (int i = 0; i < count; i++) {
            COUNT_DOWN_LATCH.countDown();
        }
    }
}
