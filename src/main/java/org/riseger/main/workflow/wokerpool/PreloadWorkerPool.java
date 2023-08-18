package org.riseger.main.workflow.wokerpool;

import org.riseger.main.workflow.job.Job;
import org.riseger.main.workflow.job.PreloadJob;
import sun.nio.ch.ThreadPool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PreloadWorkerPool implements WorkerPool<PreloadJob>{
    private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE_TIME,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            new ThreadPoolExecutor.DiscardPolicy()
    );

    @Override
    public void arrangeWork(PreloadJob job) {

    }
}
