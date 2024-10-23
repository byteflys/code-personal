package com.easing.commons.android.struct;

import java.util.LinkedHashMap;
import java.util.Map;

public class StringMap extends LinkedHashMap<String, String> {

    public static StringMap getDefault() {
        return new StringMap();
    }

    public static StringMap from(Map<String, String> map) {
        StringMap stringMap = StringMap.getDefault();
        stringMap.putAll(map);
        return stringMap;
    }

}


