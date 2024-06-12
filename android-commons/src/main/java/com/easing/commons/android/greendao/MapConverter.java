package com.easing.commons.android.greendao;

import com.easing.commons.android.data.JSON;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.Map;

import lombok.SneakyThrows;

public class MapConverter implements PropertyConverter<Map, String> {

    @Override
    @SneakyThrows
    public Map convertToEntityProperty(String dbValue) {
        return JSON.parse(dbValue, Map.class);
    }

    @Override
    public String convertToDatabaseValue(Map objValue) {
        return JSON.stringify(objValue);
    }
}
