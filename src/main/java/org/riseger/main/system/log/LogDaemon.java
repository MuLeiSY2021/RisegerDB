package org.riseger.main.system.log;

import org.riseger.main.constant.Config;
import org.riseger.main.system.LogSystem;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LogDaemon implements Runnable {
    private final ReadWriteLock READ_WRITE_LOCK = new ReentrantReadWriteLock();

    private CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(Config.LOG_COLLECT_ROUND);

    private static volatile Boolean STOP_FLAG = false;
    @Override
    public void run() {
        while (!STOP_FLAG) {
            try {
                COUNT_DOWN_LATCH.await();
                write();
                //TODO:遍历内存，获取所有被标记修改的内存,并获取所有标记部分的读锁
                //TODO:将所有标记部分按照文件系统结构落盘
                LogSystem.INSTANCE.deleteAllLog();
                COUNT_DOWN_LATCH = new CountDownLatch(Config.LOG_COLLECT_ROUND);
                unwrite();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void read() {
        READ_WRITE_LOCK.readLock().lock();
    }

    public void unread() {
        READ_WRITE_LOCK.readLock().unlock();
    }

    public void write() {
        READ_WRITE_LOCK.writeLock().lock();
    }

    public void unwrite() {
        READ_WRITE_LOCK.writeLock().lock();
    }

    public void countDown(int count) {
        for (int i = 0; i < count; i++) {
            COUNT_DOWN_LATCH.countDown();
        }
    }
}
