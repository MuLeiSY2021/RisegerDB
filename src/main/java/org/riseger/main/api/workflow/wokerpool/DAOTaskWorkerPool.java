package org.riseger.main.api.workflow.wokerpool;

import org.apache.log4j.Logger;
import org.riseger.main.api.workflow.jobstack.CommonJobStack;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DAOTaskWorkerPool implements WorkerPool{
    private final CommonJobStack jobStack;

    private static final Logger lo = Logger.getLogger(DAOTaskWorkerPool.class);

    private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE_TIME,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            new ThreadPoolExecutor.AbortPolicy()
    );

    public DAOTaskWorkerPool(CommonJobStack jobStack) {
        threadPool.prestartAllCoreThreads();
        this.jobStack = jobStack;
    }

    @Override
    public void arrangeWork() {
        try {
            threadPool.execute(jobStack.pop());
        } catch (RejectedExecutionException e) {
            lo.warn("Thread pool is full, reject to stack");
        }
    }
}
