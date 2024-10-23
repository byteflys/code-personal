package com.easing.commons.android.helper.exception;

public class ExceptionWrapper extends BizException {

    //是否打印异常到控制台
    public boolean print = false;
    //是否通过弹窗提示异常
    public boolean tip = false;
    //是否保存异常到日志
    public boolean save = false;
    //是否在异常时结束进程
    public boolean finish = false;

    protected ExceptionWrapper(Exception e) {
        super(e);
    }
}

