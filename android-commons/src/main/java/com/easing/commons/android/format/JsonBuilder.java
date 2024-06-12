package com.easing.commons.android.format;

import com.easing.commons.android.struct.MultiKeyMap;

//构建一个JSON字符串
@SuppressWarnings("all")
public class JsonBuilder {

    MultiKeyMap map = new MultiKeyMap();

    public static JsonBuilder create() {
        return new JsonBuilder();
    }

    public JsonBuilder put(Object value, Object... keys) {
        map.put(value, keys);
        return this;
    }

    public String build() {
        String json = map.toString();
        map = null;
        return json;
    }

}
