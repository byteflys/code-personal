package com.easing.commons.android.thread;

import android.os.Handler;

import com.easing.commons.android.helper.callback.Action;

public class Handlers {

    public static void post(Handler handler, Action r) {
        handler.post(() -> {
            r.runAndPostException();
        });
    }

    public static void postLater(Handler handler, Action r, long ms) {
        handler.postDelayed(() -> {
            r.runAndPostException();
        }, ms);
    }
}
