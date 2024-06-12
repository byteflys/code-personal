package com.easing.commons.android.time;

import com.easing.commons.android.preference.Preference;

import java.util.Timer;
import java.util.TimerTask;

//一个用于计算机器总运行时间的工具类
//有些机器没有电池，停止供电后，时间就会恢复到出厂设置
//这时的系统时间就是错误的，需要以硬件总共运行的时间作为时间戳
public class LaunchTime {

    public static long time = 0L;

    //开始计时
    public static void start() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                time = time + 500L;
                Preference.set("LaunchTime", 0L);
            }
        }, 0, 500L);
    }

    //读取上次关机时的时间
    public static void restore() {
        time = Preference.get("LaunchTime", Long.class, 0L);
    }
}

