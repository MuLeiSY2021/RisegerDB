package org.riseger.main.system.cache;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockableEntity implements LockableEntity_i {

    private final transient ReadWriteLock lock;

    public LockableEntity() {
        this.lock = new ReentrantReadWriteLock();
    }

    @Override
    public void read() {
        lock.readLock().lock();
    }

    @Override
    public void unread() {
        lock.readLock().unlock();
    }

    @Override
    public void write() {
        lock.writeLock().lock();
    }

    @Override
    public void unwrite() {
        lock.writeLock().unlock();
    }
}
