package com.easing.commons.android.data;

import com.easing.commons.android.format.Maths;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//以Map的形式包装多个数据
@SuppressWarnings("all")
public class KeyArguments {

    final Map map = new ConcurrentHashMap();

    public static KeyArguments create() {
        return new KeyArguments();
    }

    //复制Map数据，创建一个KeyArguments
    public static KeyArguments fromMap(Map map) {
        KeyArguments arguments = new KeyArguments();
        arguments.map.putAll(map);
        return arguments;
    }

    //复制Json数据，创建一个KeyArguments
    public static KeyArguments fromJson(String json) {
        Map<String, Object> map = JSON.parse(json, Map.class);
        KeyArguments arguments = new KeyArguments();
        arguments.map.putAll(map);
        return arguments;
    }

    //存储数据
    public KeyArguments arg(Object key, Object value) {
        map.put(key, value);
        return this;
    }

    //获取数据
    public <T> T arg(Object key) {
        return (T) map.get(key);
    }

    //获取整数，如果是小数，则转为整数
    public Integer intValue(Object key) {
        Object value = map.get(key);
        return Maths.intValue((Number) value);
    }

    //从Map中拷贝一组数据
    public KeyArguments putAll(Map map) {
        map.putAll(map);
        return this;
    }
}
