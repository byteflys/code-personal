package com.android.code.commons;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.WindowManager;

@SuppressWarnings("all")
public class Device {

    //获取屏幕大小
    public static Size getScreenSize(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        return new Size(dm.widthPixels, dm.heightPixels);
    }

    //获取屏幕宽度
    public static int screenWidth() {
        return Device.screenSize().getWidth();
    }

    //获取屏幕高度
    public static int screenHeight() {
        return Device.screenSize().getHeight();
    }

    //获取屏幕可用区域大小（不包括状态栏，导航栏）
    public static Size screenSize() {
        return Device.getScreenSize(Global.application);
    }
}
