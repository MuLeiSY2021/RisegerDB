package org.riseger.main.workflow.wokerpool;

import org.riseger.main.workflow.job.Job;

public interface WorkerPool<J extends Job<?>> {
    int CORE_POOL_SIZE = 5; // 核心线程数

    int MAX_POOL_SIZE = 10; // 最大线程数

    long KEEP_ALIVE_TIME = 60; // 空闲线程存活时间（单位：秒）

    void arrangeWork(J job);
}
