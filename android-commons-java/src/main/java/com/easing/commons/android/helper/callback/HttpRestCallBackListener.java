package com.easing.commons.android.helper.callback;


import androidx.annotation.Nullable;

/**
 * http请求结果回调调结果
 */
public interface HttpRestCallBackListener<T> {

    //成功
    void onSucceedCall(@Nullable T data);

    //失败
    void onFailCall(int code, String message);

    //完成
    void onFinishCall();
}
