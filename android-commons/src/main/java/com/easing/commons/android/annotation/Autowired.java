package com.easing.commons.android.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

//自动装载对象实例
//由于注解不支持传递对象，因此只能调用无参数或单参数的构造方法
//多参数的构造方法，除了第一个参数，后面的参数都为null
@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
public @interface Autowired {

    Class clazz();

    Class[] args() default {};
}
