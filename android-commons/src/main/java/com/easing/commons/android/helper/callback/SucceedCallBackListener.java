package com.easing.commons.android.helper.callback;


import androidx.annotation.Nullable;

/**
 * 通用简单回调结果
 */
public interface SucceedCallBackListener<T> {


    void succeedCallBack(@Nullable T o);
}
