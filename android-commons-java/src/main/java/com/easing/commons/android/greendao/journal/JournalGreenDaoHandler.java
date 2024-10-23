package com.easing.commons.android.greendao.journal;

import android.database.sqlite.SQLiteDatabase;

import com.easing.commons.android.app.CommonApplication;

public class JournalGreenDaoHandler {

    public static final int version = CommonApplication.innerDBVersion;

    public static JournalGreenDaoSession session;

    static {
        initSession();
    }

    public static void initSession() {
        clearSession();
        JournalGreenDaoHelper helper = new JournalGreenDaoHelper(new JournalGreenDaoContext(), "journal", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        session = new JournalGreenDaoMaster(db).newSession();
    }

    public static void clearSession() {
        if (session == null)
            return;
        session.clear();
        session = null;
    }


}

