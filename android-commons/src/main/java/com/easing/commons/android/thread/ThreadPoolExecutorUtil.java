package com.easing.commons.android.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 */
public class ThreadPoolExecutorUtil /*implements ThreadFactory */ {


    private static ThreadPoolExecutor threadPoolExecutor;

    /**
     * 初始化线程池
     * <p>
     * corePoolSize 最小
     * maximumPoolSize 最大
     * <p>
     * keepAliveTime 现场缓存 时长
     *
     * @return
     */
    private static final ThreadPoolExecutor initPool() {
        if (threadPoolExecutor == null)
            threadPoolExecutor = new ThreadPoolExecutor(64,
                    512, 20, TimeUnit.SECONDS, new ArrayBlockingQueue<>(128)/*, new ThreadPoolExecutor()*/);
        return threadPoolExecutor;
    }

    /**
     * 获取线程池
     *
     * @return
     */
    public static final Executor getInstance() {
        return initPool();
    }

    public static void clear() {
        if (threadPoolExecutor != null && threadPoolExecutor.getQueue() != null)
            for (Runnable runnable : threadPoolExecutor.getQueue()) {
                threadPoolExecutor.remove(runnable);
            }
    }

  /*  @Override
    public Thread newThread(Runnable r) {

        Thread thread = new Thread(r);
        thread.setName("name");
        thread.setDaemon(true);
        return thread;
    }*/
}
