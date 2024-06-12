package com.easing.commons.android.manager;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.value.apk.SdkVersion;
import com.easing.commons.android.value.measure.Size;

import lombok.SneakyThrows;

@SuppressWarnings("all")
public class Device {

    static Size cachedScreenSize;

    //获取屏幕大小
    public static Size getScreenSize(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        return new Size(dm.widthPixels, dm.heightPixels);
    }

    //获取屏幕可用区域大小（不包括状态栏，导航栏）
    public static Size screenSize() {
        return Device.getScreenSize(CommonApplication.ctx);
    }

    //获取手机屏幕完整大小
    public static Size phoneSize() {
        WindowManager windowManager = CommonApplication.ctx.getSystemService(WindowManager.class);
        Display display = windowManager.getDefaultDisplay();
        Point outPoint = new Point();
        display.getRealSize(outPoint);
        int w = outPoint.x;
        int h = outPoint.y;
        return new Size(w, h);
    }

    //获取屏幕大小，有缓存则使用缓存
    public static Size getCachedScreenSize() {
        if (cachedScreenSize == null)
            cachedScreenSize = Device.getScreenSize(CommonApplication.ctx);
        return cachedScreenSize;
    }

    //获取屏幕宽度
    public static int screenWidth() {
        return Device.screenSize().w;
    }

    //获取屏幕高度
    public static int screenHeight() {
        return Device.screenSize().h;
    }

    //获取下方导航栏高度
    public static int navigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    //获取上方状态栏高度
    public static int statuBarHeight() {
        return statuBarHeight(CommonApplication.ctx);
    }

    //获取上方状态栏高度
    @SneakyThrows
    public static int statuBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int dimen = context.getResources().getDimensionPixelSize(resourceId);
        return dimen;
    }

    //判断屏幕是否打开，锁屏也被视为息屏
    public static boolean isScreenOn() {
        PowerManager powerManager = (PowerManager) CommonApplication.ctx.getSystemService(Context.POWER_SERVICE);
        return powerManager.isInteractive();
    }

    //判断屏幕是否打开，锁屏也被视为息屏
    public static String screenState() {
        if (isScreenOn()) return "SCREEN_ON";
        return "SCREEN_OFF";
    }

    public static String phoneManufacture() {
        return Build.MANUFACTURER;
    }

    public static String phoneBrand() {
        return Build.BRAND;
    }

    public static String phoneModel() {
        return Build.MODEL;
    }

    public static String[] phoneAbis() {
        return Build.SUPPORTED_ABIS;
    }

    public static String modelInfo() {
        return Build.BRAND + "  " + Build.MODEL;
    }

    public static int sdkVersionCode() {
        return Build.VERSION.SDK_INT;
    }

    public static String sdkVersionName() {
        return Build.VERSION.INCREMENTAL;
    }

    //判断是否是华为手机
    public static boolean isHuaweiPhone() {
        String brand = Build.BRAND.toUpperCase();
        if (brand.equals("HUAWEI") || brand.equals("HONOR"))
            return true;
        return false;
    }

    //判断是否是小米手机
    public static boolean isXiaomiPhone() {
        String brand = Build.BRAND.toUpperCase();
        if (brand.equals("XIAOMI"))
            return true;
        return false;
    }

    //判断是否是三星手机
    public static boolean isSamsungPhone() {
        String brand = Build.BRAND.toUpperCase();
        if (brand.equals("SAMSUNG"))
            return true;
        return false;
    }

    //判断是否拥有完全文件访问权限
    public static boolean isExternalStorageManager() {
        if (sdkVersionCode() < SdkVersion.ANDROID_11)
            return true;
        return Environment.isExternalStorageManager();
    }
}
