package com.easing.commons.android.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 临时数据存储
 */
@SuppressWarnings("all")
public class DataCenter {

    private static final Map<String, Object> map = new ConcurrentHashMap();

    //存储数据
    public static void set(String name, Object object) {
        if (object == null)
            map.remove(name);
        else
            map.put(name, object);
    }

    //获取数据
    public static <T> T get(String name) {
        return (T) map.get(name);
    }

    //删除数据
    public static void remove(String name) {
        if (map.containsKey(name)) {
            map.remove(name);
        }
    }

    //获取数据
    public static <T> T get(String name, T defaultValue) {
        T value = (T) map.get(name);
        if (value == null)
            return defaultValue;
        return value;
    }
}
