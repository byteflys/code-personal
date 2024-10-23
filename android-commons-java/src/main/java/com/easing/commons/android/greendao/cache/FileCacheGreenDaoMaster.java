package com.easing.commons.android.greendao.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.easing.commons.android.cache.FileCacheDao;
import com.easing.commons.android.code.Console;

import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.identityscope.IdentityScopeType;

public class FileCacheGreenDaoMaster extends AbstractDaoMaster {

    public static void createAllTables(Database db, boolean ifNotExists) {
        FileCacheDao.createTable(db, ifNotExists);
    }

    public static void dropAllTables(Database db, boolean ifExists) {
        FileCacheDao.dropTable(db, ifExists);
    }

    public static FileCacheGreenDaoSession newDevSession(Context context, String name) {
        Database db = new DevOpenHelper(context, name).getWritableDb();
        FileCacheGreenDaoMaster daoMaster = new FileCacheGreenDaoMaster(db);
        return daoMaster.newSession();
    }

    public FileCacheGreenDaoMaster(SQLiteDatabase db) {
        this(new StandardDatabase(db));
    }

    public FileCacheGreenDaoMaster(Database db) {
        super(db, FileCacheGreenDaoHandler.version);
        registerDaoClass(FileCacheDao.class);
    }

    public FileCacheGreenDaoSession newSession() {
        return new FileCacheGreenDaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }

    public FileCacheGreenDaoSession newSession(IdentityScopeType type) {
        return new FileCacheGreenDaoSession(db, type, daoConfigMap);
    }


    public static abstract class OpenHelper extends DatabaseOpenHelper {
        public OpenHelper(Context context, String name) {
            super(context, name, FileCacheGreenDaoHandler.version);
        }

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, FileCacheGreenDaoHandler.version);
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

