package com.easing.commons.android.service.keep_alive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.easing.commons.android.event.EventBus;

public class OnePixelReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        //屏幕打开时，关闭1px窗口
        if (action.equals(Intent.ACTION_SCREEN_ON)) {
            EventBus.core.emit("onScreenOn");
        }
        //屏幕关闭时，开启1px窗口
        if (action.equals(Intent.ACTION_SCREEN_OFF)) {
            Intent it = new Intent(context, OnePixelActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(it);
        }
    }
}

