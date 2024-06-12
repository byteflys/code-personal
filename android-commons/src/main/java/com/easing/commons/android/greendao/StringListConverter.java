package com.easing.commons.android.greendao;

import com.easing.commons.android.data.JSON;
import com.easing.commons.android.struct.StringList;

import org.greenrobot.greendao.converter.PropertyConverter;

public class StringListConverter implements PropertyConverter<StringList, String> {

    @Override
    public StringList convertToEntityProperty(String dbValue) {
        if (dbValue != null)
            return JSON.parse(dbValue, StringList.class);
        return null;
    }

    @Override
    public String convertToDatabaseValue(StringList objValue) {
        if (objValue != null)
            return JSON.stringify(objValue);
        return null;
    }
}
