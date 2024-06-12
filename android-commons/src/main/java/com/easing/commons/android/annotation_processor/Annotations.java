package com.easing.commons.android.annotation_processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import lombok.SneakyThrows;

//注解处理工具类
@SuppressWarnings("all")
public class Annotations {

    //获取类中的方法
    public static Method getMethod(Class clazz, String methodName, Class... methodArgs) {
        try {
            return clazz.getDeclaredMethod(methodName, methodArgs);
        } catch (Throwable e) {
        }
        return null;
    }

    //获取字段上的注解值
    @SneakyThrows
    public static <T> T getFieldAnnotation(Class objectClass, String objectField, Class annotationClass, String annotationMethod) {
        //默认使用value方法
        if (annotationMethod == null) annotationMethod = "value";
        //获取对象中的字段
        Field field = objectClass.getDeclaredField(objectField);
        if (field == null) return null;
        //获取字段上的注解
        Annotation annotation = field.getDeclaredAnnotation(annotationClass);
        if (annotation == null) return null;
        //获取注解中的方法
        Method method = annotation.annotationType().getMethod(annotationMethod);
        if (method == null) return null;
        //执行注解中的方法
        Object value = method.invoke(annotation);
        return (T) value;
    }

    //获取方法上的注解值
    @SneakyThrows
    public static <T> T getMethodAnnotation(Class objectClass, String objectMethodName, Class annotationClass, String annotationMethodName, Class... objectMethodArgs) {
        //默认使用value方法
        if (annotationMethodName == null) annotationMethodName = "value";
        //获取对象中的方法
        Method method = Annotations.getMethod(objectClass, objectMethodName, objectMethodArgs);
        if (method == null) return null;
        //获取方法上的注解
        Annotation annotation = method.getDeclaredAnnotation(annotationClass);
        if (annotation == null) return null;
        //获取注解中的方法
        Method annotationMethod = annotation.annotationType().getMethod(annotationMethodName);
        if (annotationMethod == null) return null;
        //执行注解中的方法
        Object value = annotationMethod.invoke(annotation);
        return (T) value;
    }

}
