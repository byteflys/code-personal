package com.easing.commons.android.annotation_processor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.ui.dialog.TipBox;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import lombok.SneakyThrows;

//反射处理工具类
@SuppressWarnings("all")
public class Reflection {

    //根据名称寻找类
    public static Class findClass(String name) {
        if (name == null) return null;
        try {
            return Class.forName(name);
        } catch (Throwable e) {
            TipBox.tip("找不到类文件\n" + name);
            CommonApplication.ctx.handleGlobalException(e);
            return null;
        }
    }

    //获取指定对象的字段值
    public static Object findFiled(Object object, String field) {
        try {
            Class clazz = object instanceof Class ? (Class) object : object.getClass();
            Field declaredField = clazz.getDeclaredField(field);
            Object value = declaredField.get(object);
            return value;
        } catch (Throwable e) {
            TipBox.tip("找不到字段  " + object.getClass().getName() + "#" + field);
            CommonApplication.ctx.handleGlobalException(e);
            return null;
        }
    }

    //获取指定对象的字段值
    public static Object findFiledIgnoreException(Object object, String field) {
        try {
            Class clazz = object instanceof Class ? (Class) object : object.getClass();
            Field declaredField = clazz.getDeclaredField(field);
            declaredField.setAccessible(true);
            Object value = declaredField.get(object);
            return value;
        } catch (Throwable e) {
            CommonApplication.ctx.handleGlobalException(e);
            return null;
        }
    }

    //根据名称寻找类
    public static Class findClassIgnoreException(String name) {
        try {
            return Class.forName(name);
        } catch (Throwable e) {
            CommonApplication.ctx.handleGlobalException(e);
            return null;
        }
    }

    //获取构造方法
    public static Constructor getConstructor(Class clazz, Class[] args) {
        Constructor[] constructors = clazz.getConstructors();
        if (constructors.length == 0) return null;
        try {
            return clazz.getConstructor(args);
        } catch (Throwable e) {
            return null;
        }
    }

    //根据类型创建View
    @SneakyThrows
    public static <T extends View> T createViewInstance(Context context, Class<T> clazz) {
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length == 1 && parameterTypes[0] == Context.class)
                return (T) constructor.newInstance(context);
            if (parameterTypes.length != 2) continue;
            if (parameterTypes[0] == Context.class && parameterTypes[1] == AttributeSet.class)
                return (T) constructor.newInstance(context, null);
        }
        return null;
    }

    //根据类型创建View
    @SneakyThrows
    public static <T extends View> T createViewInstance(Context context, String className) {
        Class clazz = Reflection.findClass(className);
        return (T) createViewInstance(context, clazz);
    }

    //基本类型转包装类型
    public static Class wrap(Class clazz) {
        if (clazz == byte.class) return Byte.class;
        if (clazz == char.class) return Character.class;
        if (clazz == short.class) return Short.class;
        if (clazz == int.class) return Integer.class;
        if (clazz == long.class) return Long.class;
        if (clazz == float.class) return Float.class;
        if (clazz == double.class) return Double.class;
        if (clazz == boolean.class) return Boolean.class;
        if (clazz == byte[].class) return Byte[].class;
        if (clazz == char[].class) return Character[].class;
        if (clazz == short[].class) return Short[].class;
        if (clazz == int[].class) return Integer[].class;
        if (clazz == long[].class) return Long[].class;
        if (clazz == float[].class) return Float[].class;
        if (clazz == double[].class) return Double[].class;
        if (clazz == boolean[].class) return Boolean[].class;
        return clazz;
    }

    //创建实例
    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Throwable e) {
            CommonApplication.ctx.handleGlobalException(e);
            return null;
        }
    }

}
