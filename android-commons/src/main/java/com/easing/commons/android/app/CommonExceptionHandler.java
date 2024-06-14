package com.easing.commons.android.app;

import android.os.Handler;
import android.os.Looper;

import com.easing.commons.android.R;
import com.easing.commons.android.code.Console;
import com.easing.commons.android.helper.exception.BizException;
import com.easing.commons.android.journal.Journal;
import com.easing.commons.android.thread.WorkThread;
import com.easing.commons.android.ui.dialog.TipBox;

import lombok.SneakyThrows;

@SuppressWarnings("all")
public class CommonExceptionHandler {

    //fromUser表示由开发者手动转交异常的，非未捕获异常
    @SneakyThrows
    public void onException(Thread thread, Throwable e, boolean fromUser) {

        //记录错误信息
        Console.error(e);
        Journal.save(e);

        //如果是BizException，不做处理
        if (BizException.isNormalException(e))
            return;

        //开启新线程，显示报错信息
        //因为当前线程可能已经因为异常而退出，没有消息队列可用于显示Toast
        WorkThread.post(() -> {
//            Looper.prepare();
//            TipBox.toast("程序发生未知异常", R.layout.layout_tip_box_default, null, new Handler());
//            Looper.loop();
        },false);

        //TODO => 修改结束进程规则
        //主线程如果触发了UncaughtExceptionHandler，则意味着主线程结束，应用接着会崩溃或重启
        //主线程休眠一会再结束进程，以便子线程显示提示异常信息
//        if (!fromUser && thread == Threads.mainThread()) {
//            Threads.sleep(CommonApplication.waitTimeOnException);
//            CommonApplication.ctx.finishAllProcess();
//        }

        //发生异常时退出应用，适用于安全性要求高的情景
        if (CommonApplication.exitOnException)
            CommonApplication.ctx.finishAllProcess();
    }
}


