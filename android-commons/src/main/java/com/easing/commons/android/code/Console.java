package com.easing.commons.android.code;

import android.util.Log;

import com.easing.commons.android.format.Texts;
import com.easing.commons.android.helper.exception.BizException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

//控制台打印模块
@SuppressWarnings("all")
public class Console {

    public static final int LOG_LEVEL_NONE = 0;
    public static final int LOG_LEVEL_INFO = 0x01 << 0;
    public static final int LOG_LEVEL_DEBUG = 0x01 << 1;
    public static final int LOG_LEVEL_WARN = 0x01 << 2;
    public static final int LOG_LEVEL_ERROR = 0x01 << 3;
    public static final int LOG_LEVEL_DETAIL = 0x01 << 4;
    public static final int LOG_LEVEL_ALL = LOG_LEVEL_INFO | LOG_LEVEL_DEBUG | LOG_LEVEL_WARN | LOG_LEVEL_ERROR | LOG_LEVEL_DETAIL;

    public static int LEVEL = LOG_LEVEL_ALL;

    public static final String TAG = "com.easing.commons";

    public static void INFO(Object obj) {
        if ((LEVEL & LOG_LEVEL_INFO) == 0)
            return;
        info(obj);
    }

    public static void DEBUG(Object obj) {
        if ((LEVEL & LOG_LEVEL_DEBUG) == 0)
            return;
        info(obj);
    }

    public static void WARN(Object obj) {
        ERROR(obj);
    }

    public static void ERROR(Object obj) {
        if ((LEVEL & LOG_LEVEL_ERROR) == 0)
            return;
        if (obj instanceof Throwable)
            error((Throwable) obj);
        else
            error(obj);
    }

    public static void DETAIL(Object obj) {
        if ((LEVEL & LOG_LEVEL_DETAIL) == 0)
            return;
        info(obj);
    }

    public static void INFO(Object... obj) {
        if ((LEVEL & LOG_LEVEL_INFO) == 0)
            return;
        info(obj);
    }

    public static void DEBUG(Object... obj) {
        if ((LEVEL & LOG_LEVEL_DEBUG) == 0)
            return;
        info(obj);
    }

    public static void WARN(Object... obj) {
        ERROR(obj);
    }

    public static void ERROR(Object... obj) {
        if ((LEVEL & LOG_LEVEL_ERROR) == 0)
            return;
        error(obj);
    }

    public static void DETAIL(Object... obj) {
        if ((LEVEL & LOG_LEVEL_DETAIL) == 0)
            return;
        info(obj);
    }

    public static void info(Object obj) {
        Console.infoWithTag(TAG, obj);
    }

    public static void info(Class clazz, Object... objs) {
        Object[] array = new Object[objs.length + 1];
        array[0] = clazz.getSimpleName();
        int index = 1;
        for (Object obj : objs)
            array[index++] = obj;
        Console.infoWithTag(TAG, array);
    }

    public static void info(Object... objs) {
        Console.infoWithTag(TAG, objs);
    }

    public static void infoWithTag(String tag, Object obj) {
        if (obj != null)
            Log.w(tag, obj.toString());
        else
            Log.w(tag, "null");
    }

    public static void infoWithTag(String tag, Object... objs) {
        String msg = Texts.arrayToString(objs);
        Console.infoWithTag(tag, msg);
    }

    public static void error(Throwable e) {
        if (e instanceof SocketTimeoutException)
            Console.info("Socket Timeout Exception");
        else if (e instanceof ConnectException)
            Console.info("Connect Exception");
        else if (e instanceof ConnectException && e.getMessage().contains("Bad Network"))
            Console.info("Bad Network Exception");
        else if (e == BizException.THREAD_NORMAL_EXIT)
            Console.info("Thread Normal Exit");
        else {
            String info = Code.getExceptionDetail(e);
            Console.error(TAG, info);
        }
    }

    public static void error(Object obj) {
        Console.errorWithTag(TAG, obj);
    }

    public static void error(Object... objs) {
        Console.errorWithTag(TAG, objs);
    }

    public static void errorWithTag(String tag, Object obj) {
        if (obj != null)
            Log.e(tag, obj.toString());
        else
            Log.e(tag, "null");
    }

    public static void errorWithTag(String tag, Object... objs) {
        String msg = Texts.arrayToString(objs);
        Console.errorWithTag(tag, msg);
    }
}

