package org.riseger.main.system.log;

import lombok.Setter;
import org.apache.log4j.Logger;
import org.riseger.main.constant.Config;
import org.riseger.main.system.CacheSystem;
import org.riseger.main.system.LogSystem;
import org.riseger.main.system.StorageSystem;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LogDaemon implements Runnable {
    private static final Logger LOG = Logger.getLogger(LogDaemon.class);

    private final ReadWriteLock READ_WRITE_LOCK = new ReentrantReadWriteLock();

    private CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(Config.LOG_COLLECT_ROUND);

    @Setter
    private static volatile Boolean STOP_FLAG = false;

    @Override
    public void run() {
        while (!STOP_FLAG) {
            try {
                COUNT_DOWN_LATCH.await();
                write();
                StorageSystem.DEFAULT.organizeDatabases(CacheSystem.INSTANCE.getDatabases());
                LogSystem.INSTANCE.deleteAllLog();
                COUNT_DOWN_LATCH = new CountDownLatch(Config.LOG_COLLECT_ROUND);
            } catch (Throwable e) {
                LOG.error("LogDaemon: ", e);
            } finally {
                unwrite();
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
