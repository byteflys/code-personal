package com.easing.commons.android.greendao.journal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.easing.commons.android.code.Console;
import com.easing.commons.android.journal.JournalDao;

import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.identityscope.IdentityScopeType;

@SuppressWarnings("all")
public class JournalGreenDaoMaster extends AbstractDaoMaster {

    public static void createAllTables(Database db, boolean ifNotExists) {
        JournalDao.createTable(db, ifNotExists);
    }


    public static void dropAllTables(Database db, boolean ifExists) {
        JournalDao.dropTable(db, ifExists);
    }

    public JournalGreenDaoMaster(SQLiteDatabase db) {
        this(new StandardDatabase(db));
    }

    public JournalGreenDaoMaster(Database db) {
        super(db, JournalGreenDaoHandler.version);
        registerDaoClass(JournalDao.class);
    }

    public JournalGreenDaoSession newSession() {
        return new JournalGreenDaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }

    public JournalGreenDaoSession newSession(IdentityScopeType type) {
        return new JournalGreenDaoSession(db, type, daoConfigMap);
    }


    public static abstract class OpenHelper extends DatabaseOpenHelper {
        public OpenHelper(Context context, String name) {
            super(context, name, JournalGreenDaoHandler.version);
        }

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, JournalGreenDaoHandler.version);
        }

        @Override
        public void onCreate(Database db) {
            Console.info("GreenDAO Create Tables");
            createAllTables(db, false);
        }
    }


    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name) {
            super(context, name);
        }

        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            Console.info("GreenDAO Upgrade");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

}
