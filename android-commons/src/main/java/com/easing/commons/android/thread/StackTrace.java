package com.easing.commons.android.thread;

import com.easing.commons.android.time.Times;

public class StackTrace {

    public static String callerInfo() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        StackTraceElement element = elements[4];
        String info = element.getFileName() + ":" + element.getLineNumber() + " " + Times.now(Times.FORMAT_04);
        return info;
    }
}
