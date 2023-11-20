package org.riseger;

public class ConfigConstant {

    public static Integer PORT = 10086;

    public static Integer NETTY_BLOCKING_LOG = 125;

    public static int CORE_POOL_SIZE = 5; // 核心线程数

    public static int MAX_POOL_SIZE = 10; // 最大线程数

    public static long KEEP_ALIVE_TIME = 60; // 空闲线程存活时间（单位：秒）

    public static Integer DEFAULT_MEMORYSIZE = 1024;
}
