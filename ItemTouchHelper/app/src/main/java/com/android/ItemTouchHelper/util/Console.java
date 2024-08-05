package com.android.ItemTouchHelper.util;

import android.util.Log;

public class Console {

    public static final String TAG = "ItemTouchHelperDebug";

    public static void debug(Object... objects) {
        String message = "";
        for (Object object : objects)
            message = message + " " + String.valueOf(object);
        Log.d(TAG, message);
    }
}
