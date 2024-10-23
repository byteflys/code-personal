package com.easing.commons.android.cache;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.easing.commons.android.greendao.cache.FileCacheGreenDaoSession;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

@SuppressWarnings("all")
public class FileCacheDao extends AbstractDao<FileCache, String> {

    public static final String TABLENAME = "FILE_CACHE";

    public static class Properties {
        public final static Property Url = new Property(0, String.class, "url", true, "URL");
        public final static Property Path = new Property(1, String.class, "path", false, "PATH");
    }

    public FileCacheDao(DaoConfig config) {
        super(config);
    }

    public FileCacheDao(DaoConfig config, FileCacheGreenDaoSession daoSession) {
        super(config, daoSession);
    }

    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "\"FILE_CACHE\" (" +
                "\"URL\" TEXT PRIMARY KEY NOT NULL UNIQUE ," +
                "\"PATH\" TEXT);");
    }

    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"FILE_CACHE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, FileCache entity) {
        stmt.clearBindings();

        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(1, url);
        }

        String path = entity.getPath();
        if (path != null) {
            stmt.bindString(2, path);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, FileCache entity) {
        stmt.clearBindings();

        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(1, url);
        }

        String path = entity.getPath();
        if (path != null) {
            stmt.bindString(2, path);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }

    @Override
    public FileCache readEntity(Cursor cursor, int offset) {
        FileCache entity = new FileCache(
                cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0),
                cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1)
        );
        return entity;
    }

    @Override
    public void readEntity(Cursor cursor, FileCache entity, int offset) {
        entity.setUrl(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setPath(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
    }

    @Override
    protected final String updateKeyAfterInsert(FileCache entity, long rowId) {
        return entity.getUrl();
    }

    @Override
    public String getKey(FileCache entity) {
        if (entity != null) {
            return entity.getUrl();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(FileCache entity) {
        return entity.getUrl() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
}

