package com.easing.commons.android.lbs;

import com.easing.commons.android.data.JSON;

import org.greenrobot.greendao.converter.PropertyConverter;

@SuppressWarnings("all")
public class SimpleLBSLocationListConvertor implements PropertyConverter<SimpleLBSLocationList, String> {

    @Override
    public SimpleLBSLocationList convertToEntityProperty(String dbValue) {
        if (dbValue != null)
            return JSON.parse(dbValue, SimpleLBSLocationList.class);
        return null;
    }

    @Override
    public String convertToDatabaseValue(SimpleLBSLocationList objValue) {
        if (objValue != null)
            return JSON.stringify(objValue);
        return null;
    }
}

