package org.riseger.main.workflow.wokerpool;

import org.apache.log4j.Logger;
import org.riseger.main.workflow.job.PreloadJob;
import org.riseger.main.workflow.jobstack.PreloadJobStack;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class PreloadWorkerPool implements WorkerPool{
    private final PreloadJobStack jobStack;

    private static final Logger lo = Logger.getLogger(PreloadWorkerPool.class);

    private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE_TIME,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            new ThreadPoolExecutor.AbortPolicy()
    );

    public PreloadWorkerPool(PreloadJobStack jobStack) {
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
