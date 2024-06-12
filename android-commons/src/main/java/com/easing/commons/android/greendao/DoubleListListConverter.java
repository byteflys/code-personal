package com.easing.commons.android.greendao;

import com.easing.commons.android.data.JSON;
import com.easing.commons.android.struct.DoubleListList;

import org.greenrobot.greendao.converter.PropertyConverter;

public class DoubleListListConverter implements PropertyConverter<DoubleListList, String> {

    @Override
    public DoubleListList convertToEntityProperty(String dbValue) {
        if (dbValue != null)
            return JSON.parse(dbValue, DoubleListList.class);
        return null;
    }

    @Override
    public String convertToDatabaseValue(DoubleListList objValue) {
        if (objValue != null)
            return JSON.stringify(objValue);
        return null;
    }
}
