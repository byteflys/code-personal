package com.easing.commons.android.redirection;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//通过此注解可以实现View和Activity的自动跳转
//在CommonActivity.onClick或CommonActivity.onDestroy执行后
//框架会自动查找注解，根据注解来自动跳转
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.TYPE,
        ElementType.FIELD
})
public @interface Redirection {

    String ruleName();
}
