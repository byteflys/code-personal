package com.easing.commons.android.annotation_configuration;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TypeValue {

    Class clazz() default Nulls.class;

    String className() default "";
}
