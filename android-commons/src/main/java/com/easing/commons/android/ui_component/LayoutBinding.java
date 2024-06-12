package com.easing.commons.android.ui_component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LayoutBinding {

    Layout layout() default Layout.Grid;

    int column() default 3;

    int width() default 0;

    int height() default 0;
}

