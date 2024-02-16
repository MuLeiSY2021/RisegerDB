package org.riseger.main.system.cache;

import lombok.Getter;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Getter
public class CacheEntity implements CacheEntity_i {
    private final transient ReadWriteLock lock;
    private boolean isChanged;

    public CacheEntity() {
        this.isChanged = false;
        this.lock = new ReentrantReadWriteLock();
    }

    public void changeEntity() {
        this.isChanged = true;
    }

    public void resetChanged() {
        this.isChanged = false;
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
