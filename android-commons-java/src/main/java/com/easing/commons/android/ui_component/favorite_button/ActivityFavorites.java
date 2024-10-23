package com.easing.commons.android.ui_component.favorite_button;

import androidx.annotation.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ActivityFavorites {

    @NonNull String group();

    @NonNull String title();

    @NonNull String icon();
}
