package com.easing.commons.android.manager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import androidx.core.content.FileProvider;

import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.code.Console;
import com.easing.commons.android.io.AndroidFile;
import com.easing.commons.android.io.Files;
import com.easing.commons.android.io.ProjectFile;
import com.easing.commons.android.io.Streams;

import lombok.SneakyThrows;

@SuppressWarnings("all")
public class Uris {

    //默认使用CommonApplication的包名作为FileProvider的authority
    //Manifest中FileProvider的authority必须和这个值一致
    public static String AUTHORITY_FILE_PROVIDER = CommonApplication.ctx.getPackageName();

    //绑定自己的Context，而不是CommonApplication
    public static void init(Context ctx) {
        AUTHORITY_FILE_PROVIDER = ctx.getPackageName();
    }

    //获取res.raw下的资源URI
    public static Uri fromResource(String resource) {
        return Uri.parse("android.resource://" + CommonApplication.ctx.getPackageName() + "/raw/" + resource);
    }

    //获取文件的资源URI
    public static Uri fromFile(Context context, String path, String authority) {
        return FileProvider.getUriForFile(context, authority, new File(path));
    }

    //获取文件的资源URI
    public static Uri fromFile(String path) {
        return FileProvider.getUriForFile(CommonApplication.ctx, Uris.AUTHORITY_FILE_PROVIDER, new File(path));
    }

    //通过文件URI解析文件地址
    @SneakyThrows
    public static String uriToPath(Uri uri) {
        if (uri == null) return null;

        //获得ContentResolver，用于访问其它应用数据
        ContentResolver resolver = CommonApplication.ctx.getContentResolver();

        //获得URI路径
        String pathUri = uri.getPath().toLowerCase();
        pathUri = pathUri.replaceAll("%3a", ":");
        pathUri = pathUri.replaceAll("%2f", "/");

        //返回的是微信分享路径
        if (uri.toString().toLowerCase().startsWith("content://com.tencent.mm.external.fileprovider/external/")) {
            String file = ProjectFile.getProjectFile("cache/" + Files.getFileName(pathUri));
            InputStream is = resolver.openInputStream(uri);
            OutputStream os = new FileOutputStream(file);
            Streams.write(is, os);
            return file;
        }

        //返回的是微信分享路径
        if (uri.toString().toLowerCase().startsWith("content://com.tencent.mm.external.fileprovider/attachment/")) {
            String file = ProjectFile.getProjectFile("cache/" + Files.getFileName(pathUri));
            InputStream is = resolver.openInputStream(uri);
            OutputStream os = new FileOutputStream(file);
            Streams.write(is, os);
            return file;
        }

        //返回的是相对文件路径
        if (pathUri.startsWith("/external-path/")) {
            String relativePath = pathUri.replaceAll("/external-path/", "");
            String file = AndroidFile.getAndroidExternalFile(relativePath);
            return file;
        }

        //返回的是绝对文件路径
        if (pathUri.startsWith(ProjectFile.EXTERNAL_STORAGE))
            return pathUri;

        //返回的是绝对文件路径
        if (pathUri.startsWith("/document/raw:")) {
            pathUri = pathUri.replaceAll("/document/raw:", "");
            return pathUri;
        }

        //返回的是数据库资源id
        String id = null;
        if (pathUri.startsWith("/document/document:"))
            id = pathUri.replaceAll("/document/document:", "");
        if (pathUri.startsWith("/document/image:"))
            id = pathUri.replaceAll("/document/image:", "");
        if (pathUri.startsWith("/document/video:"))
            id = pathUri.replaceAll("/document/video:", "");
        if (pathUri.startsWith("/document/msf:"))
            id = pathUri.replaceAll("/document/msf:", "");
        if (pathUri.startsWith("/external/file/"))
            id = pathUri.replaceAll("/external/file/", "");
        if (pathUri.startsWith("/external_primary/images/media/"))
            id = pathUri.replaceAll("/external_primary/images/media/", "");

        //通过文件id查询路径
        if (id != null) {
            Cursor cursor = resolver.query(
                    MediaStore.Files.getContentUri("external", Long.parseLong(id)),
                    new String[]{MediaStore.Files.FileColumns.DATA},
                    MediaStore.Files.FileColumns._ID + " = ?",
                    new String[]{id},
                    null
            );
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
            String data = cursor.getString(index);
            cursor.close();
            return data;
        }

        try {
            String scheme = uri.getScheme();
            String data = null;
            if (scheme == null)
                data = uri.getPath();
            else if (ContentResolver.SCHEME_FILE.equals(scheme))
                data = uri.getPath();
            else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
                Cursor cursor = resolver.query(
                        uri,
                        new String[]{MediaStore.Images.ImageColumns.DATA},
                        null,
                        null,
                        null
                );
                if (null != cursor) {
                    if (cursor.moveToFirst()) {
                        int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                        if (index > -1)
                            data = cursor.getString(index);
                    }
                    cursor.close();
                }
                if (data == null)
                    data = externalUriToPath(uri);
            }

            return data;
        } catch (Throwable e) {
            Console.error(e);
            return null;
        }
    }

    //通过图片URI解析图片地址
    public static String externalUriToPath(Uri uri) {

        if (uri == null) return null;

        Context context = CommonApplication.ctx;
        // Document
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            if (isStorageDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type))
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadDocument(uri)) {
                String id = DocumentsContract.getDocumentId(uri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return queryFilePathFromMediaDb(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return queryFilePathFromMediaDb(context, contentUri, selection, selectionArgs);
            }
        }

        // MediaStore
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhoto(uri))
                return uri.getLastPathSegment();
            return queryFilePathFromMediaDb(context, uri, null, null);
        }

        // File
        else if ("file".equalsIgnoreCase(uri.getScheme()))
            return uri.getPath();

        return null;
    }

    //通过文件URI查询文件路径
    @SneakyThrows
    private static String queryFilePathFromMediaDb(Context context, Uri uri, String selection, String[] args) {
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        Cursor cursor = context.getContentResolver().query(uri, projection, selection, args, null);
        if (cursor != null)
            if (cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                String path = cursor.getString(index);
                cursor.close();
                return path;
            }
        return null;
    }

    private static boolean isStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhoto(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getFPUriToPath(Context context, Uri uri) {
        try {
            List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);

            if (packs != null) {
                String fileProviderClassName = "android.support.v4.content.FileProvider";

                for (PackageInfo pack : packs) {
                    ProviderInfo[] providers = pack.providers;

                    if (providers != null) {
                        for (ProviderInfo provider : providers) {
                            if (uri.getAuthority().equals(provider.authority)) {
                                if (provider.name.equalsIgnoreCase(fileProviderClassName)) {
                                    Class fileProviderClass = FileProvider.class;

                                    try {
                                        Method getPathStrategy = fileProviderClass.getDeclaredMethod("getPathStrategy", Context.class, String.class);

                                        getPathStrategy.setAccessible(true);

                                        Object invoke = getPathStrategy.invoke(null, context, uri.getAuthority());

                                        if (invoke != null) {
                                            String PathStrategyStringClass = FileProvider.class.getName() + "$PathStrategy";

                                            Class PathStrategy = Class.forName(PathStrategyStringClass);

                                            Method getFileForUri = PathStrategy.getDeclaredMethod("getFileForUri", Uri.class);

                                            getFileForUri.setAccessible(true);

                                            Object invoke1 = getFileForUri.invoke(invoke, uri);

                                            if (invoke1 instanceof File) {
                                                String filePath = ((File) invoke1).getAbsolutePath();

                                                return filePath;

                                            }

                                        }

                                    } catch (NoSuchMethodException e) {
                                        e.printStackTrace();

                                    } catch (InvocationTargetException e) {
                                        e.printStackTrace();

                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();

                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();

                                    }

                                    break;

                                }

                                break;

                            }

                        }

                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return null;

    }


}
