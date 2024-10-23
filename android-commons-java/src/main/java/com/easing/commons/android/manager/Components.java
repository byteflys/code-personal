package com.easing.commons.android.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.easing.commons.android.app.CommonActivity;

@SuppressWarnings("all")
public class Components {

    //调用"文件管理"组件
    public static void callFileManageComponent(Context ctx) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        intent.setData(Uri.parse("package:" + ctx.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    //调用"应用信息"组件
    public static void callApplicationInfoComponent(Context ctx) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + ctx.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    //调用"定位服务"组件
    //此组件包含"位置信息"，"定位精度"两个子组件
    public static void callLocationServiceComponent(CommonActivity ctx) {
        ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.Settings$LocationSettingsActivity");
        Intent intent = new Intent();
        intent.setComponent(componentName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    //调用"电池优化"组件
    public static void callPowerOptimizeComponent(CommonActivity ctx) {
        ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.Settings$HighPowerApplicationsActivity");
        Intent intent = new Intent();
        intent.setComponent(componentName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    //调用华为"设置"组件
    public static void callHuaweiSettingComponent(CommonActivity ctx) {
        ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.HWSettings");
        Intent intent = new Intent();
        intent.setComponent(componentName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    //调用华为"应用启动管理"组件
    public static void callHuaweiApplicationLaunchComponent(CommonActivity ctx) {
        ComponentName componentName = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
        Intent intent = new Intent();
        intent.setComponent(componentName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    //调用小米"应用启动管理"组件
    public static void callXiaomiApplicationLaunchComponent(CommonActivity ctx) {
        ComponentName componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity");
        Intent intent = new Intent();
        intent.setComponent(componentName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }


}
