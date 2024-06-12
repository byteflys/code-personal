package com.easing.commons.android.struct;

import com.easing.commons.android.data.JSON;

import java.util.LinkedHashMap;

import lombok.Getter;

//通过多个索引来存储数据的MAP
@SuppressWarnings("all")
public class MultiKeyMap {

    LinkedHashMap coreMap = new LinkedHashMap();

    public void put(Object value, Object... keys) {
        LinkedHashMap map = coreMap;
        for (int i = 0; i < keys.length - 1; i++) {
            if (map.get(keys[i]) == null)
                map.put(keys[i], new LinkedHashMap());
            map = (LinkedHashMap) map.get(keys[i]);
        }
        map.put(keys[keys.length - 1], value);
    }

    public <T> T get(Object... keys) {
        LinkedHashMap map = coreMap;
        for (int i = 0; i < keys.length - 1; i++) {
            if (map.get(keys[i]) == null)
                return null;
            map = (LinkedHashMap) map.get(keys[i]);
        }
        return (T) map.get(keys[keys.length - 1]);
    }

    public LinkedHashMap getMap(Object... keys) {
        LinkedHashMap map = coreMap;
        for (int i = 0; i < keys.length; i++) {
            if (map.get(keys[i]) == null)
                return null;
            map = (LinkedHashMap) map.get(keys[i]);
        }
        return map;
    }

    @Override
    public String toString() {
        return JSON.stringifyBeautifully(coreMap);
    }
}


