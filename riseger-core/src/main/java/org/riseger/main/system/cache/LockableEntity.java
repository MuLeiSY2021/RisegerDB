package org.riseger.main.system.cache;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockableEntity {

    private transient ReadWriteLock lock;

    public LockableEntity() {
        this.lock = new ReentrantReadWriteLock();
    }

    public void read() {
        if (lock == null) {
            lock = new ReentrantReadWriteLock();
        }
        lock.readLock().lock();
    }

    public void unread() {
        lock.readLock().unlock();
    }

    public void write() {
        if (lock == null) {
            lock = new ReentrantReadWriteLock();
        }
        lock.writeLock().lock();
    }

    public void unwrite() {
        lock.writeLock().unlock();
    }
}
