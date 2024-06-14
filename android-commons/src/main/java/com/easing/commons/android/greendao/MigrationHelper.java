package com.easing.commons.android.greendao;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.internal.DaoConfig;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 数据库升级辅助类
 * 支持数据库添加，
 *
 */
public class MigrationHelper {

    public static boolean DEBUG = true;
    private static String TAG = "MigrationHelper";
    private static final String SQLITE_MASTER = "sqlite_master";
    private static final String SQLITE_TEMP_MASTER = "sqlite_temp_master";
    private static WeakReference<MigrationHelper.ReCreateAllTableListener> weakListener;

    public MigrationHelper() {
    }

    public static void migrate(SQLiteDatabase db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        printLog("【The Old Database Version】" + db.getVersion());
        Database database = new StandardDatabase(db);
        migrate((Database) database, daoClasses);
    }

    public static void migrate(SQLiteDatabase db, MigrationHelper.ReCreateAllTableListener listener, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        weakListener = new WeakReference(listener);
        migrate(db, daoClasses);
    }

    public static void migrate(Database database, MigrationHelper.ReCreateAllTableListener listener, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        weakListener = new WeakReference(listener);
        migrate(database, daoClasses);
    }

    public static void migrate(Database database, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        printLog("【Generate temp table】start");
        generateTempTables(database, daoClasses);
        printLog("【Generate temp table】complete");
        MigrationHelper.ReCreateAllTableListener listener = null;
        if (weakListener != null) {
            listener = (MigrationHelper.ReCreateAllTableListener) weakListener.get();
        }

        if (listener != null) {
            listener.onDropAllTables(database, true);
            printLog("【Drop all table by listener】");
            listener.onCreateAllTables(database, false);
            printLog("【Create all table by listener】");
        } else {
            dropAllTables(database, true, daoClasses);
            createAllTables(database, false, daoClasses);
        }

        printLog("【Restore data】start");
        restoreData(database, daoClasses);
        printLog("【Restore data】complete");
    }

    private static void generateTempTables(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        for (int i = 0; i < daoClasses.length; ++i) {
            String tempTableName = null;
            DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);
            String tableName = daoConfig.tablename;
            if (!isTableExists(db, false, tableName)) {
                printLog("【New Table】" + tableName);
            } else {
                try {
                    tempTableName = daoConfig.tablename.concat("_TEMP");
                    StringBuilder dropTableStringBuilder = new StringBuilder();
                    dropTableStringBuilder.append("DROP TABLE IF EXISTS ").append(tempTableName).append(";");
                    db.execSQL(dropTableStringBuilder.toString());
                    StringBuilder insertTableStringBuilder = new StringBuilder();
                    insertTableStringBuilder.append("CREATE TEMPORARY TABLE ").append(tempTableName);
                    insertTableStringBuilder.append(" AS SELECT * FROM `").append(tableName).append("`;");
                    db.execSQL(insertTableStringBuilder.toString());
                    printLog("【Table】" + tableName + "\n ---Columns-->" + getColumnsStr(daoConfig));
                    printLog("【Generate temp table】" + tempTableName);
                } catch (SQLException var8) {
                    Log.e(TAG, "【Failed to generate temp table】" + tempTableName, var8);
                }
            }
        }

    }

    private static boolean isTableExists(Database db, boolean isTemp, String tableName) {
        if (db != null && !TextUtils.isEmpty(tableName)) {
            String dbName = isTemp ? "sqlite_temp_master" : "sqlite_master";
            String sql = "SELECT COUNT(*) FROM `" + dbName + "` WHERE type = ? AND name = ?";
            Cursor cursor = null;
            int count = 0;

            try {
                cursor = db.rawQuery(sql, new String[]{"table", tableName});
                if (cursor == null || !cursor.moveToFirst()) {
                    boolean var7 = false;
                    return var7;
                }

                count = cursor.getInt(0);
            } catch (Exception var11) {
                var11.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }

            }

            return count > 0;
        } else {
            return false;
        }
    }

    private static String getColumnsStr(DaoConfig daoConfig) {
        if (daoConfig == null) {
            return "no columns";
        } else {
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < daoConfig.allColumns.length; ++i) {
                builder.append(daoConfig.allColumns[i]);
                builder.append(",");
            }

            if (builder.length() > 0) {
                builder.deleteCharAt(builder.length() - 1);
            }

            return builder.toString();
        }
    }

    private static void dropAllTables(Database db, boolean ifExists, @NonNull Class<? extends AbstractDao<?, ?>>... daoClasses) {
        reflectMethod(db, "dropTable", ifExists, daoClasses);
        printLog("【Drop all table by reflect】");
    }

    private static void createAllTables(Database db, boolean ifNotExists, @NonNull Class<? extends AbstractDao<?, ?>>... daoClasses) {
        reflectMethod(db, "createTable", ifNotExists, daoClasses);
        printLog("【Create all table by reflect】");
    }

    private static void reflectMethod(Database db, String methodName, boolean isExists, @NonNull Class<? extends AbstractDao<?, ?>>... daoClasses) {
        if (daoClasses.length >= 1) {
            try {
                Class[] var4 = daoClasses;
                int var5 = daoClasses.length;

                for (int var6 = 0; var6 < var5; ++var6) {
                    Class cls = var4[var6];
                    Method method = cls.getDeclaredMethod(methodName, Database.class, Boolean.TYPE);
                    method.invoke((Object) null, db, isExists);
                }
            } catch (NoSuchMethodException var9) {
                var9.printStackTrace();
            } catch (InvocationTargetException var10) {
                var10.printStackTrace();
            } catch (IllegalAccessException var11) {
                var11.printStackTrace();
            }

        }
    }

    private static void restoreData(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        for (int i = 0; i < daoClasses.length; ++i) {
            DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);
            String tableName = daoConfig.tablename;
            String tempTableName = daoConfig.tablename.concat("_TEMP");
            if (isTableExists(db, true, tempTableName)) {
                try {
                    List<MigrationHelper.TableInfo> newTableInfos = MigrationHelper.TableInfo.getTableInfo(db, tableName);
                    List<MigrationHelper.TableInfo> tempTableInfos = MigrationHelper.TableInfo.getTableInfo(db, tempTableName);
                    ArrayList<String> selectColumns = new ArrayList(newTableInfos.size());
                    ArrayList<String> intoColumns = new ArrayList(newTableInfos.size());
                    Iterator var10 = tempTableInfos.iterator();

                    MigrationHelper.TableInfo tableInfo;
                    String column;
                    while (var10.hasNext()) {
                        tableInfo = (MigrationHelper.TableInfo) var10.next();
                        if (newTableInfos.contains(tableInfo)) {
                            column = '`' + tableInfo.name + '`';
                            intoColumns.add(column);
                            selectColumns.add(column);
                        }
                    }

                    var10 = newTableInfos.iterator();

                    while (var10.hasNext()) {
                        tableInfo = (MigrationHelper.TableInfo) var10.next();
                        if (tableInfo.notnull && !tempTableInfos.contains(tableInfo)) {
                            column = '`' + tableInfo.name + '`';
                            intoColumns.add(column);
                            String value;
                            if (tableInfo.dfltValue != null) {
                                value = "'" + tableInfo.dfltValue + "' AS ";
                            } else {
                                value = "'' AS ";
                            }

                            selectColumns.add(value + column);
                        }
                    }

                    StringBuilder dropTableStringBuilder;
                    if (intoColumns.size() != 0) {
                        dropTableStringBuilder = new StringBuilder();
                        dropTableStringBuilder.append("REPLACE INTO `").append(tableName).append("` (");
                        dropTableStringBuilder.append(TextUtils.join(",", intoColumns));
                        dropTableStringBuilder.append(") SELECT ");
                        dropTableStringBuilder.append(TextUtils.join(",", selectColumns));
                        dropTableStringBuilder.append(" FROM ").append(tempTableName).append(";");
                        db.execSQL(dropTableStringBuilder.toString());
                        printLog("【Restore data】 to " + tableName);
                    }

                    dropTableStringBuilder = new StringBuilder();
                    dropTableStringBuilder.append("DROP TABLE ").append(tempTableName);
                    db.execSQL(dropTableStringBuilder.toString());
                    printLog("【Drop temp table】" + tempTableName);
                } catch (SQLException var14) {
                    Log.e(TAG, "【Failed to restore data from temp table 】" + tempTableName, var14);
                }
            }
        }

    }

    private static List<String> getColumns(Database db, String tableName) {
        List<String> columns = null;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + tableName + " limit 0", (String[]) null);
            if (null != cursor && cursor.getColumnCount() > 0) {
                columns = Arrays.asList(cursor.getColumnNames());
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }

            if (null == columns) {
                columns = new ArrayList();
            }

        }

        return (List) columns;
    }

    private static void printLog(String info) {
        if (DEBUG) {
            Log.d(TAG, info);
        }

    }

    private static class TableInfo {
        int cid;
        String name;
        String type;
        boolean notnull;
        String dfltValue;
        boolean pk;

        private TableInfo() {
        }

        public boolean equals(Object o) {
            return this == o || o != null && this.getClass() == o.getClass() && this.name.equals(((MigrationHelper.TableInfo) o).name);
        }

        public String toString() {
            return "TableInfo{cid=" + this.cid + ", name='" + this.name + '\'' + ", type='" + this.type + '\'' + ", notnull=" + this.notnull + ", dfltValue='" + this.dfltValue + '\'' + ", pk=" + this.pk + '}';
        }

        private static List<MigrationHelper.TableInfo> getTableInfo(Database db, String tableName) {
            String sql = "PRAGMA table_info(`" + tableName + "`)";
            MigrationHelper.printLog(sql);
            Cursor cursor = db.rawQuery(sql, (String[]) null);
            if (cursor == null) {
                return new ArrayList();
            } else {
                ArrayList tableInfos = new ArrayList();

                while (cursor.moveToNext()) {
                    MigrationHelper.TableInfo tableInfo = new MigrationHelper.TableInfo();
                    tableInfo.cid = cursor.getInt(0);
                    tableInfo.name = cursor.getString(1);
                    tableInfo.type = cursor.getString(2);
                    tableInfo.notnull = cursor.getInt(3) == 1;
                    tableInfo.dfltValue = cursor.getString(4);
                    tableInfo.pk = cursor.getInt(5) == 1;
                    tableInfos.add(tableInfo);
                }

                cursor.close();
                return tableInfos;
            }
        }
    }

    public interface ReCreateAllTableListener {
        void onCreateAllTables(Database var1, boolean var2);

        void onDropAllTables(Database var1, boolean var2);
    }
}
