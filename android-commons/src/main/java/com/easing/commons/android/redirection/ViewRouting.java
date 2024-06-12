package com.easing.commons.android.redirection;

import com.easing.commons.android.annotation_configuration.Nulls;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ViewRouting {

    Class target() default Nulls.class;

    String targetName() default "";

    boolean destroyCurrent() default false;
}
