package com.easing.commons.android.app;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Process;

import com.easing.commons.android.code.Console;
import com.easing.commons.android.io.Files;
import com.easing.commons.android.io.ProjectFile;
import com.easing.commons.android.manager.Uris;

import java.io.File;
import java.security.MessageDigest;
import java.util.List;
import java.util.Locale;

import lombok.SneakyThrows;

@SuppressWarnings("all")
public class Applications {

    @SneakyThrows
    public static String getPackageName(Context context) {
        PackageManager manager = context.getPackageManager();
        String packageName = context.getPackageName();
        return packageName;
    }

    //获取当前应用包名
    public static String packageName() {
        PackageManager manager = CommonApplication.ctx.getPackageManager();
        String packageName = CommonApplication.ctx.getPackageName();
        return packageName;
    }

    //获取当前进程名
    public static String getCurrentProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses())
            if (appProcess.pid == pid)
                return appProcess.processName;
        return null;
    }

    //获取当前进程名
    public static int currentProcessId() {
        return Process.myPid();
    }

    //获取当前进程名
    public static String currentProcessName() {
        return getCurrentProcessName(CommonApplication.ctx);
    }

    //获取当前应用的所有子线程
    public static List<ActivityManager.RunningAppProcessInfo> applicationProcesses() {
        ActivityManager manager = (ActivityManager) CommonApplication.ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : appProcesses)
            Console.info("子进程列表", info.processName, info.pid);
        return appProcesses;
    }

    //判断是否是主进程
    public static boolean isMainProcess(Context context) {
        String processName = Applications.getCurrentProcessName(context);
        String packageName = Applications.getPackageName(context);
        boolean main = processName.equalsIgnoreCase(packageName);
        return main;
    }

    /**
     * @see com.easing.commons.android.manager.Device#sdkVersionCode
     */
    @Deprecated
    public static int sdkVersionCode() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * @see com.easing.commons.android.manager.Device#sdkVersionName
     */
    @Deprecated
    public static String sdkVersionName() {
        return Build.VERSION.INCREMENTAL;
    }

    @SneakyThrows
    public static int getVersionCode() {
        PackageManager manager = CommonApplication.ctx.getPackageManager();
        String packageName = CommonApplication.ctx.getPackageName();
        PackageInfo info = manager.getPackageInfo(packageName, 0);
        return info.versionCode;
    }

    @SneakyThrows
    public static int getVersionCode(Context context) {
        PackageManager manager = context.getPackageManager();
        String packageName = context.getPackageName();
        PackageInfo info = manager.getPackageInfo(packageName, 0);
        return info.versionCode;
    }

    @SneakyThrows
    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        String packageName = context.getPackageName();
        PackageInfo info = manager.getPackageInfo(packageName, 0);
        return info.versionName;
    }

    @SneakyThrows
    public static String getMetaInfo(Context ctx, String key) {
        ApplicationInfo info = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
        if (info != null)
            if (info.metaData != null)
                return info.metaData.getString(key);
        return null;
    }

    public static void killProcess() {
        Process.killProcess(Process.myPid());
    }

    //安装更新包
    public static void installApk(String file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uris.fromFile(file);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        CommonApplication.ctx.startActivity(intent);
    }

    //从内部存储空间安装更新包
    @SneakyThrows
    public static void installApkFromInnerStorage(String file) {
        String destDirectory = ProjectFile.getProjectPackageDirectory();
        File destFile = Files.copyToFolder(file, destDirectory);
        installApk(destFile.getAbsolutePath());
    }

    //获取APK的SHA1码
    @SneakyThrows
    public static String SHA1(Context context) {
        PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
        byte[] cert = info.signatures[0].toByteArray();
        MessageDigest md = MessageDigest.getInstance("SHA1");
        byte[] publicKey = md.digest(cert);
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < publicKey.length; i++) {
            String appendString = Integer.toHexString(0xFF & publicKey[i]).toUpperCase(Locale.US);
            if (appendString.length() == 1)
                hexString.append("0");
            hexString.append(appendString);
            hexString.append(":");
        }
        String result = hexString.toString();
        return result.substring(0, result.length() - 1);
    }
}
