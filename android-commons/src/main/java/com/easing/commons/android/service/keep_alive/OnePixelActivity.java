package com.easing.commons.android.service.keep_alive;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.easing.commons.android.R;
import com.easing.commons.android.code.Console;
import com.easing.commons.android.event.EventBus;
import com.easing.commons.android.event.OnEvent;

@SuppressWarnings("all")
public class OnePixelActivity extends Activity {

    public static volatile boolean alive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //创建1像素窗口
        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 1;
        params.width = 1;
        window.setBackgroundDrawableResource(R.drawable.color_red);
        window.setAttributes(params);
        //窗口不接收触摸手势
        int flag = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        window.addFlags(flag);
        OnePixelActivity.alive = true;
        EventBus.core.subscribe(this);
        Console.info("OnePixelActivity", "onCreate");
    }

    @Override
    protected void onDestroy() {
        OnePixelActivity.alive = false;
        super.onDestroy();
        EventBus.core.unsubscribe(this);
        Console.info("OnePixelActivity", "onDestroy");
    }

    @OnEvent(type = "onScreenOn")
    public void onScreenOn(String type) {
        Console.info("OnePixelActivity", "onScreenOn");
        finish();
    }
}

