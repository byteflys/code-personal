package com.easing.commons.android.thread;

import com.blankj.utilcode.util.LogUtils;
import com.easing.commons.android.helper.callback.Action;
import com.easing.commons.android.data.Data;
import com.easing.commons.android.time.Times;

import java.util.Map;

public class Tasks {

    //判断时间间隔，再决定是否执行任务
    synchronized public static void executeWithIntervalLimit(Action r, Data<Long> timer, long duration, Action onFail) {
        long now = Times.millisOfNow();
        if (now - timer.data > duration) {
            r.runAndPostException();
            timer.data = now;
        } else if (onFail != null)
            onFail.runAndPostException();
    }

    public static void getAllThread() {
        Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
     //   LogUtils.d("线程总数 =  " + allStackTraces.size());
        for (Map.Entry<Thread, StackTraceElement[]> stackTrace : allStackTraces.entrySet()) {
            Thread thread = (Thread) stackTrace.getKey();
          //  LogUtils.d("线程Name =  " + thread.getName() + "   id = " + thread.getId() + "  State=" + thread.getState());
            StackTraceElement[] stack = (StackTraceElement[]) stackTrace.getValue();
            String strStackTrace = "堆栈 :";
            for (StackTraceElement stackTraceElement : stack) {
                strStackTrace += stackTraceElement.toString() + "\n";
            }
          //  LogUtils.d("线程堆栈 =  " + strStackTrace);
        }
    }
}
