package com.easing.commons.android.greendao.cache;

import android.database.sqlite.SQLiteDatabase;

import com.easing.commons.android.app.CommonApplication;

import org.greenrobot.greendao.identityscope.IdentityScopeType;

public class FileCacheGreenDaoHandler {

    public static final int version = CommonApplication.innerDBVersion;

    public static FileCacheGreenDaoSession session;

    static {
        initSession();
    }

    public static void initSession() {
        clearSession();
        FileCacheGreenDaoHelper helper = new FileCacheGreenDaoHelper(new FileCacheGreenDaoContext(), "FileCache", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        session = new FileCacheGreenDaoMaster(db).newSession(IdentityScopeType.None);
    }

    public static void clearSession() {
        if (session == null)
            return;
        session.clear();
        session = null;
    }
}

