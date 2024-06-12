package com.easing.commons.android.thread;

import com.easing.commons.android.helper.callback.Action;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//单例线程池
public class SingleThreadPool {

    ExecutorService executor;

    private SingleThreadPool() {
    }

    public static SingleThreadPool newInstance() {
        return newInstance(1);
    }

    public static SingleThreadPool newInstance(Integer integer) {
        SingleThreadPool pool = new SingleThreadPool();
        pool.executor = Executors.newSingleThreadExecutor();
        return pool;
    }

    public SingleThreadPool submit(Action action) {
        executor.submit(action::runAndPostException);
        return this;
    }
}

