package com.easing.commons.android.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnEvent {


    String[] type() default {};

    ThreadMode thread() default ThreadMode.CURRENT_THREAD;

    //事件处理顺序，暂未实现此功能
    int order() default 0;
}

