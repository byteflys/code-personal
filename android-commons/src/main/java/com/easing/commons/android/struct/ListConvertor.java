package com.easing.commons.android.struct;

//将一种List元素转换为另一种类型的List元素
public interface ListConvertor<T, S> {

    S convert(T a);
}
