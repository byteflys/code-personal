package com.easing.commons.android.helper.exception;

import com.easing.commons.android.code.Console;

public class BizException extends RuntimeException {

    public static final BizException SERVER_ERROR = ServerError.of("Server Error");
    public static final BizException BAD_NETWORK = BizException.of("Bad Network");
    public static final BizException THREAD_NORMAL_EXIT = BizException.of("Thread Normal Exit");

    public static BizException of(Throwable e) {
        return new BizException(e);
    }

    public static BizException of(String message) {
        return new BizException(message);
    }

    //判断是不是普通异常
    //普通异常是指开发中必现的常见异常，不用处理
    public static boolean isNormalException(Throwable e) {
        if (e == SERVER_ERROR) return true;
        if (e == BAD_NETWORK) return true;
        if (e == THREAD_NORMAL_EXIT) return true;
        return false;
    }

    protected BizException(Throwable e) {
        super(e);
    }

    protected BizException(String message) {
        super(message);
    }

    public void print() {
        Console.error(this);
    }

    public void post() {
        throw this;
    }

}
