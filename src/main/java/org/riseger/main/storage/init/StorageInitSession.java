package org.riseger.main.storage.init;

import org.riseger.main.cache.entity.component.db.Database_c;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class StorageInitSession {
    public ReentrantLock lock = new ReentrantLock();

    public Condition cond = lock.newCondition();

    public Queue<Database_c> database = new ConcurrentLinkedDeque<>();

    public Database_c get() {
        lock.lock();
        try {
            while (database.size() == 0 || database.peek().isLoading()) {
                try {
                    cond.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return database.poll();
        } finally {
            lock.unlock();
        }
    }

    public void put(Database_c db) {
        lock.lock();
        try {
            db.active();
            database.add(db);
            cond.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        lock.unlock();
    }
}
