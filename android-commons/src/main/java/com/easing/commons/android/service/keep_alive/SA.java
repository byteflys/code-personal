package com.easing.commons.android.service.keep_alive;

import android.content.Intent;

import com.easing.commons.android.code.Console;

public class SA extends KeepAliveService {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Console.info("SA execute its work");
        return super.onStartCommand(intent, flags, startId);
    }
}
