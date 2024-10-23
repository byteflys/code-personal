package com.easing.commons.android.thread;

import android.os.Handler;
import android.view.View;

import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.helper.callback.Action;
import com.easing.commons.android.view.Views;

//通过全局Handler提交主线程任务
@SuppressWarnings("all")
public class MainThread {

    private static Handler handler;

    //初始化，必须在主线程中调用
    public static void init() {
        handler = CommonApplication.handler;
    }

    //提交任务
    public static void post(Action r) {
        Handlers.post(handler, r);
    }

    //延时提交任务
    public static void postLater(Action r, long ms) {
        Handlers.postLater(handler, r, ms);
    }

    //等View绑定窗口后再提交任务，绑定窗口前控件的宽高都为0，无法正确处理某些界面计算任务
    public static void postAfterViewAttach(View view, Action r) {
        Views.postAfterViewAttach(view, r);
    }
}


