package com.android.ItemTouchHelper.util;

import android.view.MotionEvent;

public class MotionEvents {

    public static String name(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN)
            return "DOWN@" + event.getEventTime();
        if (action == MotionEvent.ACTION_UP)
            return "UP@" + event.getEventTime();
        if (action == MotionEvent.ACTION_MOVE)
            return "MOVE@" + event.getEventTime();
        if (action == MotionEvent.ACTION_CANCEL)
            return "CANCEL@" + event.getEventTime();
        return "OTHER@" + event.getEventTime();
    }
}

