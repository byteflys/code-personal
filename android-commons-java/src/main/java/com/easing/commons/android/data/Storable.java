package com.easing.commons.android.data;

import java.util.LinkedHashMap;
import java.util.Map;

//此接口允许对象附带额外数据
public interface Storable {

    Map<String, Object> dataMap = new LinkedHashMap();

    default void data(String key, Object value) {
        dataMap.put(key, value);
    }

    default <T> T data(String key) {
        return (T) dataMap.get(key);
    }
}
