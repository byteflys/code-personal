package com.easing.commons.android.app;

import com.easing.commons.android.annotation_processor.Reflection;

public class BuildConfigs {

    public static final Class buildConfigClass;

    static {
        buildConfigClass = Reflection.findClass(CommonApplication.ctx.getPackageName() + ".BuildConfig");
    }

    public static String findString(String filed) {
        return (String) Reflection.findFiledIgnoreException(buildConfigClass, filed);
    }

    public static Integer findInteger(String filed) {
        return (Integer) Reflection.findFiledIgnoreException(buildConfigClass, filed);
    }

    public static Long findLong(String filed) {
        return (Long) Reflection.findFiledIgnoreException(buildConfigClass, filed);
    }

    public static Double findDouble(String filed) {
        return (Double) Reflection.findFiledIgnoreException(buildConfigClass, filed);
    }

    public static Boolean findBoolean(String filed) {
        return (Boolean) Reflection.findFiledIgnoreException(buildConfigClass, filed);
    }
}
