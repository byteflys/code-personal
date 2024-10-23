package com.easing.commons.android.thread;

import android.os.Looper;
import android.os.Process;

import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.event.ThreadMode;
import com.easing.commons.android.helper.callback.Action;

import lombok.SneakyThrows;

@SuppressWarnings("all")
public class Threads {

    public static int mainTid() {
        return CommonApplication.mainTid;
    }

    public static boolean isMainTid() {
        return Process.myPid() == CommonApplication.mainTid;
    }

    public static long currentThreadId() {
        return Thread.currentThread().getId();
    }

    public static long mainThreadId() {
        return Looper.getMainLooper().getThread().getId();
    }

    public static Thread mainThread() {
        return Looper.getMainLooper().getThread();
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    @SneakyThrows
    public static void sleep(long ms) {
        Thread.currentThread().sleep(ms);
    }

    @SneakyThrows
    public static void wait(Object lock) {
        lock.wait();
    }

    @SneakyThrows
    public static void wait(Object lock, long timeout) {
        lock.wait(timeout);
    }

    @SneakyThrows
    public static void notify(Object lock) {
        lock.notify();
    }

    @SneakyThrows
    public static void notifyAll(Object lock) {
        lock.notifyAll();
    }

    @SneakyThrows
    public static void join(Thread thread) {
        thread.join();
    }

    @SneakyThrows
    public static void interrupt(Thread thread) {
        thread.interrupt();
    }

    @SneakyThrows
    public static void runOn(ThreadMode mode, Action action) {
        if (mode == ThreadMode.CURRENT_THREAD)
            action.runAndPostException();
        if (mode == ThreadMode.MAIN_THREAD)
            MainThread.post(action::runAndPostException);
        if (mode == ThreadMode.WORK_THREAD)
            WorkThread.post(action::runAndPostException);
    }
}
