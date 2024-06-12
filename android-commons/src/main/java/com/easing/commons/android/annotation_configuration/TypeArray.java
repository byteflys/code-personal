package com.easing.commons.android.annotation_configuration;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TypeArray {

    Class[] classes() default {};

    String[] classNames() default {};
}
