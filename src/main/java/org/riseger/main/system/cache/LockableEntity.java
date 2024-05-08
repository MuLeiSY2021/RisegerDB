package org.riseger.main.system.cache;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockableEntity {

    private final transient ReadWriteLock lock;

    public LockableEntity() {
        this.lock = new ReentrantReadWriteLock();
    }

    public void read() {
        lock.readLock().lock();
    }

    public void unread() {
        lock.readLock().unlock();
    }

    public void write() {
        lock.writeLock().lock();
    }

    public void unwrite() {
        lock.writeLock().unlock();
    }
}
