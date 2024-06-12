package com.easing.commons.android.preference;

import com.easing.commons.android.data.JSON;
import com.easing.commons.android.greendao.common.CommonGreenDaoHandler;

import org.greenrobot.greendao.annotation.Id;

import java.lang.reflect.Type;
import java.util.List;

import lombok.Data;

/*
 * 本地临时标签存储
 * */
@Data
public class Preference {

    @Id
    public String key;
    public String value;

    public Preference(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static <T> void set(String key, T value) {
        PreferenceDao dao = CommonGreenDaoHandler.session.getPreferenceDao();
        if (value == null) {
            List<Preference> records = dao.queryRaw("where key = ?", key);
            dao.deleteInTx(records);
            return;
        }
        Preference preference = new Preference(key, JSON.stringify(value));
        dao.insertOrReplace(preference);
    }

    public static <T> T get(String key, Class<T> clazz) {
        PreferenceDao dao = CommonGreenDaoHandler.session.getPreferenceDao();
        List<Preference> records = dao.queryRaw("where key = ?", key);
        return records.isEmpty() ? null : JSON.parse(records.get(0).value, clazz);
    }

    public static <T> T get(String key, Type type) {
        PreferenceDao dao = CommonGreenDaoHandler.session.getPreferenceDao();
        List<Preference> records = dao.queryRaw("where key = ?", key);
        return records.isEmpty() ? null : JSON.parse(records.get(0).value, type);
    }

    public static <T> T get(String key, Class<T> clazz, T defaultValue) {
        PreferenceDao dao = CommonGreenDaoHandler.session.getPreferenceDao();
        List<Preference> records = dao.queryRaw("where key = ?", key);
        if (records.isEmpty()) {
            Preference.set(key, defaultValue);
            return defaultValue;
        }
        return JSON.parse(records.get(0).value, clazz);
    }

    public static <T> T get(Class<T> clazz) {
        return Preference.get(clazz.getSimpleName(), clazz);
    }

    public static <T> T get(Class<T> clazz, T defaultValue) {
        return Preference.get(clazz.getSimpleName(), clazz, defaultValue);
    }

    public static String get(String key) {
        PreferenceDao dao = CommonGreenDaoHandler.session.getPreferenceDao();
        List<Preference> records = dao.queryRaw("where key = ?", key);
        return records.isEmpty() ? null : records.get(0).value;
    }

    public static <T> void set(T value) {
        Preference.set(value.getClass().getSimpleName(), value);
    }
}


