package com.easing.commons.android.functional;

//将任意类型数据转为String
public interface StringConvertor<T> {

    String covert(T data);
}
