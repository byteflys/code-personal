package com.easing.commons.android.helper.callback;

import com.easing.commons.android.app.CommonApplication;

//封装一组行为，和Runnable功能是一样的，但是回避了Java的强制处理机制
//由于run方法增加了throws Exception选型，将异常抛给了调用者处理，开发者在实现run方法时就不用处理强制异常了
//虽然run方法不用处理异常，但是runIgnoreException调用了run方法，等于接收了run方法的强制异常，需要对它们进行处理
//我们进一步在runIgnoreException中将强制型异常转化为运行时异常抛出，运行时异常是不需要强制处理的
//当我们使用Action对象时，调用runIgnoreException方法来替代run方法，这样就可以回避Java的强制处理机制了

//上面的做法只是直接跳过了Java的强制异常处理机制，但并不是什么时候我们都可以这样做
//毕竟我们的程序不是完美的，有些异常可能是我们没有考虑到的，一个完善的系统是不能仅仅忽略异常的
//runAndPostException提供了一个额外的功能，它对任意异常进行捕捉，然后统一转交给Application处理

@SuppressWarnings("all")
public interface Action {

    public static final Action NULL = () -> {};

    //封装一组行为
    void run() throws Exception;

    //忽略异常
    default void runIgnoreException() {
        try {
            run();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    //将异常转交给Application统一处理
    default void runAndPostException() {
        try {
            run();
        } catch (Throwable e) {
            CommonApplication.ctx.handleGlobalException(e);
        }
    }
}


