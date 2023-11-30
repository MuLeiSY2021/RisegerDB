package org.riseger.main.system.workflow.revoke;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Revocable<E> {
    final ReentrantLock lock = new ReentrantLock();

    final Condition cond = lock.newCondition();

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
}
