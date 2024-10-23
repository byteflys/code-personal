package com.easing.commons.android.greendao.cache;

import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.io.ProjectFile;

import java.io.File;

public class FileCacheGreenDaoContext extends ContextWrapper {

    public FileCacheGreenDaoContext() {
        super(CommonApplication.ctx);
    }

    @Override
    public File getDatabasePath(String name) {
        String path = ProjectFile.getProjectFile("common/" + name + ".db");
        return new File(path);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler handler) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
    }
}

