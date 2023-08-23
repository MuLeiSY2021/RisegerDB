package org.riseger.main.api.workflow.revoke;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public abstract class revocable<E> {
    ReentrantLock lock = new ReentrantLock();

    Condition cond = lock.newCondition();

    public abstract void setE(E database);

    public abstract E getE();

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
}
