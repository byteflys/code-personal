package com.easing.commons.android.thread;

//控制线程自动结束的标志位
public class ThreadFlag {

    public boolean running = true;

    private ThreadFlag() {
    }

    public static ThreadFlag create() {
        return new ThreadFlag();
    }

    public boolean isRunning() {
        return running;
    }
}
