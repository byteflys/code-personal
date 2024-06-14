package com.easing.commons.android.greendao;

import com.easing.commons.android.data.JSON;
import com.easing.commons.android.struct.StringList;
import com.easing.commons.android.ui.control.attach_viewer.AttachFileBean;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

public class AttachFileListConverter implements PropertyConverter<List<AttachFileBean>, String> {

    @Override
    public List<AttachFileBean> convertToEntityProperty(String dbValue) {
        if (dbValue != null)
            return JSON.parse(dbValue, new TypeToken<List<AttachFileBean>>() {
            }.getType());
        return null;
    }

    @Override
    public String convertToDatabaseValue(List<AttachFileBean> objValue) {
        if (objValue != null)
            return JSON.stringify(objValue);
        return null;
    }
}
