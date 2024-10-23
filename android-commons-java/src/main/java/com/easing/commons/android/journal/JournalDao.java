package com.easing.commons.android.journal;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.easing.commons.android.greendao.journal.JournalGreenDaoSession;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;


@SuppressWarnings("all")
public class JournalDao extends AbstractDao<Journal, Long> {

    public static final String TABLENAME = "JOURNAL";

    public static class Properties {
        public final static Property RecordId = new Property(0, Long.class, "recordId", true, "_id");
        public final static Property Type = new Property(1, String.class, "type", false, "TYPE");
        public final static Property Time = new Property(2, String.class, "time", false, "TIME");
        public final static Property Data = new Property(3, String.class, "data", false, "DATA");
        public final static Property Type1 = new Property(4, String.class, "type1", false, "TYPE1");
        public final static Property Type2 = new Property(5, String.class, "type2", false, "TYPE2");
        public final static Property Type3 = new Property(6, String.class, "type3", false, "TYPE3");
        public final static Property Arg1 = new Property(7, String.class, "arg1", false, "ARG1");
        public final static Property Arg2 = new Property(8, String.class, "arg2", false, "ARG2");
        public final static Property Arg3 = new Property(9, String.class, "arg3", false, "ARG3");
        public final static Property Arg4 = new Property(10, String.class, "arg4", false, "ARG4");
        public final static Property Arg5 = new Property(11, String.class, "arg5", false, "ARG5");
        public final static Property Arg6 = new Property(12, String.class, "arg6", false, "ARG6");
        public final static Property Arg7 = new Property(13, String.class, "arg7", false, "ARG7");
        public final static Property Arg8 = new Property(14, String.class, "arg8", false, "ARG8");
        public final static Property Arg9 = new Property(15, String.class, "arg9", false, "ARG9");
        public final static Property Timemillis = new Property(16, Long.class, "timemillis", false, "TIMEMILLIS");
    }

    public JournalDao(DaoConfig config) {
        super(config);
    }

    public JournalDao(DaoConfig config, JournalGreenDaoSession daoSession) {
        super(config, daoSession);
    }

    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "\"JOURNAL\" (" +
                "\"_id\" INTEGER PRIMARY KEY ," +
                "\"TYPE\" TEXT," +
                "\"TIME\" TEXT," +
                "\"DATA\" TEXT," +
                "\"TYPE1\" TEXT," +
                "\"TYPE2\" TEXT," +
                "\"TYPE3\" TEXT," +
                "\"ARG1\" TEXT," +
                "\"ARG2\" TEXT," +
                "\"ARG3\" TEXT," +
                "\"ARG4\" TEXT," +
                "\"ARG5\" TEXT," +
                "\"ARG6\" TEXT," +
                "\"ARG7\" TEXT," +
                "\"ARG8\" TEXT," +
                "\"ARG9\" TEXT," +
                "\"TIMEMILLIS\" INTEGER);");
    }

    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"JOURNAL\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Journal entity) {
        stmt.clearBindings();

        Long recordId = entity.getRecordId();
        if (recordId != null) {
            stmt.bindLong(1, recordId);
        }

        String type = entity.getType();
        if (type != null) {
            stmt.bindString(2, type);
        }

        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(3, time);
        }

        String data = entity.getData();
        if (data != null) {
            stmt.bindString(4, data);
        }

        String type1 = entity.getType1();
        if (type1 != null) {
            stmt.bindString(5, type1);
        }

        String type2 = entity.getType2();
        if (type2 != null) {
            stmt.bindString(6, type2);
        }

        String type3 = entity.getType3();
        if (type3 != null) {
            stmt.bindString(7, type3);
        }

        String arg1 = entity.getArg1();
        if (arg1 != null) {
            stmt.bindString(8, arg1);
        }

        String arg2 = entity.getArg2();
        if (arg2 != null) {
            stmt.bindString(9, arg2);
        }

        String arg3 = entity.getArg3();
        if (arg3 != null) {
            stmt.bindString(10, arg3);
        }

        String arg4 = entity.getArg4();
        if (arg4 != null) {
            stmt.bindString(11, arg4);
        }

        String arg5 = entity.getArg5();
        if (arg5 != null) {
            stmt.bindString(12, arg5);
        }

        String arg6 = entity.getArg6();
        if (arg6 != null) {
            stmt.bindString(13, arg6);
        }

        String arg7 = entity.getArg7();
        if (arg7 != null) {
            stmt.bindString(14, arg7);
        }

        String arg8 = entity.getArg8();
        if (arg8 != null) {
            stmt.bindString(15, arg8);
        }

        String arg9 = entity.getArg9();
        if (arg9 != null) {
            stmt.bindString(16, arg9);
        }

        Long timemillis = entity.getTimemillis();
        if (timemillis != null) {
            stmt.bindLong(17, timemillis);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Journal entity) {
        stmt.clearBindings();

        Long recordId = entity.getRecordId();
        if (recordId != null) {
            stmt.bindLong(1, recordId);
        }

        String type = entity.getType();
        if (type != null) {
            stmt.bindString(2, type);
        }

        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(3, time);
        }

        String data = entity.getData();
        if (data != null) {
            stmt.bindString(4, data);
        }

        String type1 = entity.getType1();
        if (type1 != null) {
            stmt.bindString(5, type1);
        }

        String type2 = entity.getType2();
        if (type2 != null) {
            stmt.bindString(6, type2);
        }

        String type3 = entity.getType3();
        if (type3 != null) {
            stmt.bindString(7, type3);
        }

        String arg1 = entity.getArg1();
        if (arg1 != null) {
            stmt.bindString(8, arg1);
        }

        String arg2 = entity.getArg2();
        if (arg2 != null) {
            stmt.bindString(9, arg2);
        }

        String arg3 = entity.getArg3();
        if (arg3 != null) {
            stmt.bindString(10, arg3);
        }

        String arg4 = entity.getArg4();
        if (arg4 != null) {
            stmt.bindString(11, arg4);
        }

        String arg5 = entity.getArg5();
        if (arg5 != null) {
            stmt.bindString(12, arg5);
        }

        String arg6 = entity.getArg6();
        if (arg6 != null) {
            stmt.bindString(13, arg6);
        }

        String arg7 = entity.getArg7();
        if (arg7 != null) {
            stmt.bindString(14, arg7);
        }

        String arg8 = entity.getArg8();
        if (arg8 != null) {
            stmt.bindString(15, arg8);
        }

        String arg9 = entity.getArg9();
        if (arg9 != null) {
            stmt.bindString(16, arg9);
        }

        Long timemillis = entity.getTimemillis();
        if (timemillis != null) {
            stmt.bindLong(17, timemillis);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    @Override
    public Journal readEntity(Cursor cursor, int offset) {
        Journal entity = new Journal(
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0),
                cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1),
                cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2),
                cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3),
                cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4),
                cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5),
                cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6),
                cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7),
                cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8),
                cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9),
                cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10),
                cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11),
                cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12),
                cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13),
                cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14),
                cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15),
                cursor.isNull(offset + 16) ? null : cursor.getLong(offset + 16)
        );
        return entity;
    }

    @Override
    public void readEntity(Cursor cursor, Journal entity, int offset) {
        entity.setRecordId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setType(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setTime(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setData(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setType1(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setType2(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setType3(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setArg1(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setArg2(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setArg3(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setArg4(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setArg5(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setArg6(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setArg7(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setArg8(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setArg9(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setTimemillis(cursor.isNull(offset + 16) ? null : cursor.getLong(offset + 16));
    }

    @Override
    protected final Long updateKeyAfterInsert(Journal entity, long rowId) {
        entity.setRecordId(rowId);
        return rowId;
    }

    @Override
    public Long getKey(Journal entity) {
        if (entity != null) {
            return entity.getRecordId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Journal entity) {
        return entity.getRecordId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }

}
