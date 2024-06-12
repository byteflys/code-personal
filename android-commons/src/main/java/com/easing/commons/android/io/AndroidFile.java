package com.easing.commons.android.io;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;

@SuppressWarnings("all")
public class AndroidFile {

    public static String getAndroidExternalFile(String path) {
        return getAndroidExternalPath(path, false);
    }

    public static String getAndroidExternalFolder(String path) {
        return getAndroidExternalPath(path, true);
    }

    public static String getAndroidExternalPath(String path, boolean isFolder) {
        if (path == null || path.equals(""))
            return Environment.getExternalStorageDirectory().getPath().toLowerCase();
        String fullPath = Environment.getExternalStorageDirectory().getPath().toLowerCase() + "/" + path;
        if (isFolder)
            Files.createDirectory(fullPath);
        else
            Files.createFile(fullPath);
        return fullPath.toLowerCase();
    }

    public static String getAndroidPrivatePath(Context context, String path) {
        return context.getExternalFilesDir(path).getPath().toLowerCase();
    }

    public static String getAndroidPrivateCachePath(Context context, String path) {
        if (path == null)
            return context.getExternalCacheDir().getPath().toLowerCase();
        String fullPath = context.getExternalCacheDir().getPath().toLowerCase() + "/" + path;
        Files.createFile(fullPath);
        return fullPath;
    }

    public static String getAndroidInternalPath(Context context, String path) {
        if (path == null)
            return context.getFilesDir().getPath().toLowerCase();
        String file = context.getFilesDir().getPath().toLowerCase() + "/" + path;
        Files.createFile(file);
        return file;
    }

    public static String getAndroidInternalCachePath(Context context, String path) {
        if (path == null)
            return context.getCacheDir().getPath().toLowerCase();
        return context.getCacheDir().getPath().toLowerCase() + "/" + path;
    }

    public static File getAndroidInternalDatabase(Context context, String name) {
        return context.getDatabasePath(name);
    }

    public static SharedPreferences getAndroidSharedPreferences(Context context, String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static String getAndroidPublicPictureDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath().toLowerCase();
    }

    public static String getAndroidPublicMusicDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getPath().toLowerCase();
    }

    public static String getAndroidPublicMovieDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getPath().toLowerCase();
    }

    public static String getAndroidPublicDocumentDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath().toLowerCase();
    }

    public static String getAndroidPublicAlbumDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath().toLowerCase();
    }

    public static String getAndroidPublicDownloadDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath().toLowerCase();
    }
}
