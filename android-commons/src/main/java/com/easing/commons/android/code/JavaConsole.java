package com.easing.commons.android.code;

import com.easing.commons.android.format.Texts;
import com.easing.commons.android.time.Times;
import com.easing.commons.android.value.apk.PackageVersion;

import org.fusesource.jansi.Ansi;

//控制台打印模块
public class JavaConsole {

    public static final String TAG = "com.easing.commons";

    public static PackageVersion apkVersion = PackageVersion.DEBUG;

    public static void print(String tag, Object... objs) {
        String msg = Texts.arrayToString(objs);
        JavaConsole.print(tag, msg);
    }

    public static <T> void print(Class<T> clazz, Object... objs) {
        String msg = Texts.arrayToString(objs);
        JavaConsole.print(clazz, msg);
    }

    public static <T> void print(String tag, Object message) {
        Ansi ansi = Ansi.ansi()
                .bold().fg(Ansi.Color.GREEN).a(Times.now()).a("　　")
                .bold().fg(Ansi.Color.MAGENTA).a(tag).newline()
                .boldOff().fg(Ansi.Color.BLACK).a(message.toString())
                .reset();
        System.out.println(ansi);
    }

    public static <T> void print(Class<T> clazz, Object message) {
        Ansi ansi = Ansi.ansi()
                .bold().fg(Ansi.Color.GREEN).a(Times.now()).a("　　")
                .bold().fg(Ansi.Color.MAGENTA).a(clazz.getName()).newline()
                .boldOff().fg(Ansi.Color.BLACK).a(message.toString())
                .reset();
        System.out.println(ansi);
    }

    public static void print(Object obj) {
        Ansi ansi = Ansi.ansi().fg(Ansi.Color.BLACK).a(obj.toString()).reset();
        System.out.println(ansi);
    }

    public static void info(Object obj) {
        Console.infoWithTag(TAG, obj);
    }

    public static void info(Object... objs) {
        Console.infoWithTag(TAG, objs);
    }

    public static void infoWithTag(String tag, Object obj) {
        if (apkVersion == PackageVersion.RELEASE)
            return;
        if (obj != null)
            System.out.println(tag + " " + obj.toString());
        else
            System.out.println(tag + " " + "null");
    }

    public static void infoWithTag(String tag, Object... objs) {
        String msg = Texts.arrayToString(objs);
        Console.infoWithTag(tag, msg);
    }

    public static void error(Throwable e) {
        String info = Code.getExceptionDetail(e);
        Console.error(TAG, info);
    }

    public static void error(Throwable e, PackageVersion appVersion) {
        if (appVersion == PackageVersion.RELEASE)
            Console.error(e);
    }

    public static void error(Object obj) {
        Console.errorWithTag(TAG, obj);
    }

    public static void error(Object... objs) {
        Console.errorWithTag(TAG, objs);
    }

    public static void errorWithTag(String tag, Object obj) {
        if (JavaConsole.apkVersion == PackageVersion.RELEASE)
            return;
        if (obj != null)
            System.out.println(tag + " " + obj.toString());
        else
            System.out.println(tag + " " + "null");
    }

    public static void errorWithTag(String tag, Object... objs) {
        String msg = Texts.arrayToString(objs);
        Console.errorWithTag(tag, msg);
    }
}
