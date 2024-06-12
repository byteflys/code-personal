package com.easing.commons.android.greendao.common;

import android.database.sqlite.SQLiteDatabase;

import com.easing.commons.android.app.CommonApplication;

public class CommonGreenDaoHandler {

    public static final int version = CommonApplication.innerDBVersion;

    public static CommonGreenDaoSession session;

    static {
        initSession();
    }

    public static void initSession() {
        clearSession();
        CommonGreenDaoHelper helper = new CommonGreenDaoHelper(new CommonGreenDaoContext(), "common", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        session = new CommonGreenDaoMaster(db).newSession();
    }

    public static void clearSession() {
        if (session == null)
            return;
        session.clear();
        session = null;
    }


}

