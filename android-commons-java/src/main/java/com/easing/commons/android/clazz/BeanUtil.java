package com.easing.commons.android.clazz;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BeanUtil {

    //将对象的所有字段值拷贝到Map中
    @SneakyThrows
    public static Map<String, Object> attributeMap(Object source) {
        Map<String, Object> attributeMap = new HashMap();
        Field[] fields = source.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.get(source) != null)
                attributeMap.put(field.getName(), field.get(source));
        }
        return attributeMap;
    }

    //将对象的所有字段值拷贝到Map中
    @SneakyThrows
    public static void copyAttribute(Object source, Map<String, Object> attributeMap) {
        Field[] fields = source.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.get(source) != null)
                attributeMap.put(field.getName(), field.get(source));
        }
    }

    //将Map的所有属性值拷贝对象中
    @SneakyThrows
    public static void copyAttribute(Map<String, Object> attributeMap, Object target) {
        Field[] fields = target.getClass().getDeclaredFields();
        Map<String, Field> fieldMap = new HashMap();
        for (Field field : fields) {
            field.setAccessible(true);
            fieldMap.put(field.getName(), field);
        }
        for (String attribute : attributeMap.keySet())
            if (fieldMap.containsKey(attribute))
                if (attributeMap.get(attribute) != null)
                    fieldMap.get(attribute).set(target, attributeMap.get(attribute));
    }

    //将一个对象的所有字段值拷贝到另一个对象中
    @SneakyThrows
    public static void copyAttribute(Object source, Object target) {
        Field[] fields = target.getClass().getDeclaredFields();
        Map<String, Field> targetFieldMap = new HashMap();
        for (Field targetField : fields) {
            targetField.setAccessible(true);
            targetFieldMap.put(targetField.getName(), targetField);
        }
        for (Field sourceField : source.getClass().getDeclaredFields()) {
            sourceField.setAccessible(true);
            if (targetFieldMap.containsKey(sourceField.getName()))
                if (sourceField.get(source) != null)
                    copyField(source, target, sourceField, targetFieldMap.get(sourceField.getName()));
        }
    }

    //将指定的属性值设置到对象中
    @SneakyThrows
    public static void setAttribute(String key, Object value, Object target) {
        Field[] fields = target.getClass().getDeclaredFields();
        Map<String, Field> fieldMap = new HashMap();
        for (Field field : fields) {
            field.setAccessible(true);
            fieldMap.put(field.getName(), field);
        }
        if (fieldMap.containsKey(key))
            if (fieldMap.get(key).getType().getName().equals(value.getClass().getName()))
                fieldMap.get(key).set(target, value);
    }

    //将一个对象中的指定字段值，拷贝到另一个对象中
    @SneakyThrows
    public static void copyAttribute(Object source, Object target, String fieldName) {
        for (Field sourceField : source.getClass().getDeclaredFields())
            if (sourceField.getName().equals(fieldName)) {
                sourceField.setAccessible(true);
                Object fieldValue = sourceField.get(fieldName);
                for (Field targetField : target.getClass().getDeclaredFields())
                    if (targetField.getName().equals(fieldName)) {
                        targetField.setAccessible(true);
                        targetField.set(target, fieldValue);
                        return;
                    }
            }
    }

    //将一个对象的字段值，拷贝到另一个对象的任意字段
    @SneakyThrows
    private static void copyField(Object source, Object target, Field sourceField, Field targetField) {
        if (targetField.getType().getName().equals(sourceField.getType().getName()))
            targetField.set(target, sourceField.get(source));
    }
}
