package com.easing.commons.android.thread;

import com.easing.commons.android.helper.callback.Action;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//可复用线程池，可用于线程复用，在线程频繁创建时提升性能
public class ReusableThreadPool {

    ExecutorService executor;

    private ReusableThreadPool() {}

    public static ReusableThreadPool newInstance() {
        ReusableThreadPool pool = new ReusableThreadPool();
        pool.executor = Executors.newCachedThreadPool();
        return pool;
    }

    public ReusableThreadPool submit(Action action) {
        executor.submit(action::runAndPostException);
        return this;
    }
}
