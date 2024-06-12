package com.easing.commons.android.greendao;

import org.greenrobot.greendao.converter.PropertyConverter;
import org.json.JSONArray;

import lombok.SneakyThrows;

public class JsonArrayConverter implements PropertyConverter<JSONArray, String> {

    @Override
    @SneakyThrows
    public JSONArray convertToEntityProperty(String dbValue) {
        return new JSONArray(dbValue);
    }

    @Override
    public String convertToDatabaseValue(JSONArray objValue) {
        return objValue.toString();
    }
}
