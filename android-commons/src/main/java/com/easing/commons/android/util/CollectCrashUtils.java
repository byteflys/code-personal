package com.easing.commons.android.util;


import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Bundle;

import com.mauiie.aech.AECHConfiguration;
import com.mauiie.aech.AECrashHelper;
import com.uc.crashsdk.export.CrashApi;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.umcrash.UMCrash;

public class CollectCrashUtils {

    public static void initColleteCrash(Application app) {
        //初始化Handler,收集java层崩溃
      /*  AECrashHelper.initCrashHandler(app,
                new AECHConfiguration.Builder()
                        .setLocalFolderPath("/sdcard/AoDunCrashLog") //配置日志信息存储的路径
                        .setReportToServer(true)                                              //开启报告服务器功能
                        .setSaveToLocal(true)                                                 //开启存储在本地功能
                        .setReporter(new AECHConfiguration.IAECHReporter() {                  //向服务器发送报告由你自己根据项目的环境定制实现
                            @Override
                            public void report(Throwable ex) {
                                //  com.mauiie.aech.utils.LogUtil.d("向服务器发送报告由你自己根据项目的环境定制实现");
                            }
                        }).build());*/

        //收集native层崩溃
      /*  File file = new File("sdcard/Crashlog");
        if (!file.exists()) {
            file.mkdirs();
        }*/
        //NativeBreakpad.init(file.getAbsolutePath());

        /**
         *设置组件化的Log开关
         *参数: boolean 默认为false，如需查看LOG设置为true
         */
        // UMConfigure.setLogEnabled(true);
        UMConfigure.preInit(app, "62fb7d4388ccdf4b7e05bab0", "aodun_2012");

        UMConfigure.init(app, "62fb7d4388ccdf4b7e05bab0", "aodun_2012", UMConfigure.DEVICE_TYPE_PHONE, null);
        UMCrash.init(app, "62fb7d4388ccdf4b7e05bab0", "aodun_2012");
        // 选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);

        Bundle bundle = new Bundle();
        bundle.putLong(UMCrash.KEY_PA_TIMEOUT_TIME, 2000L);//设置卡顿阀值为2000L
        UMCrash.initConfig(bundle);
        UMCrash.setAppVersion("1.0.0", "巡护测试", "巡护测试--1");
        final Bundle customInfo = new Bundle();
        customInfo.putBoolean("mCallNativeDefaultHandler", true);
        CrashApi.getInstance().updateCustomInfo(customInfo);

    }
}
