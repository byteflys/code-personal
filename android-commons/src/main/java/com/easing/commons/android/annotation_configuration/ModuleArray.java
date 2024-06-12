package com.easing.commons.android.annotation_configuration;

import com.easing.commons.android.ui_component.module_button.ModuleInfo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleArray {

    Class<? extends ModuleInfo>[] value();
}

