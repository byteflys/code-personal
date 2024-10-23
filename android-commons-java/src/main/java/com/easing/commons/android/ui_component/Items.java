package com.easing.commons.android.ui_component;

import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
public @interface Items {

    Class<? extends View>[] value() default {};
}

