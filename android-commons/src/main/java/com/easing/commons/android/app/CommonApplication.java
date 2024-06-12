package com.easing.commons.android.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Process;
import android.view.Display;
import android.view.WindowManager;

import androidx.multidex.MultiDex;

import com.easing.commons.android.data.Storable;
import com.easing.commons.android.event.EventBus;
import com.easing.commons.android.helper.callback.Action;
import com.easing.commons.android.helper.thread.ComponentState;
import com.easing.commons.android.io.Files;
import com.easing.commons.android.io.ProjectFile;
import com.easing.commons.android.thread.MainThread;
import com.easing.commons.android.manager.Uris;
import com.easing.commons.android.preference.Preference;
import com.easing.commons.android.thread.Threads;
import com.easing.commons.android.thread.WorkThread;
import com.easing.commons.android.time.LaunchTime;
import com.easing.commons.android.time.Times;
import com.easing.commons.android.ui.dialog.TipBox;
import com.easing.commons.android.util.CollectCrashUtils;
import com.easing.commons.android.value.apk.PackageVersion;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class CommonApplication extends Application implements Storable {

    //公司名称，英文名称，用于存储卡路径等地方
    public static String companyName = "app";

    //项目名称，英文名称，用于存储卡路径等地方
    public static String projectName = "demo";

    //项目别名，中文名称，用于在通知栏等地方显示
    public static String projectLabel = "安卓基础平台";

    //数据版本，用于判断是否需要清理旧版数据
    //当数据格式发生重大变化，无法兼容旧数据时，需要升级数据版本，并清理旧数据
    public static Integer dataVersion = 1;

    //内置数据库版本
    //用于Preference、Journal等通用功能
    public static Integer innerDBVersion = 1;
    //业务数据库版本
    public static Integer databaseVersion = 1;

    //数据库升级时，是否清除旧的数据库
    public static boolean clearDatabase = false;

    //全局Context
    public static CommonApplication ctx;

    //全局Handler
    public static Handler handler;

    //主线程tid
    public static int mainTid;

    //任务栈管理
    private static LinkedList<CommonActivity> activityStack = new LinkedList();
    public static CommonActivity currentActivity;

    //全局线程标志位，其它线程根据这个判断是否要继续执行
    //APP退出前将标志位置为false，相关联的子线程就会自动结束
    public static ComponentState componentState = ComponentState.create();

    //出现异常时是否退出
    public static boolean exitOnException = false;
    //退出前的等待时长
    public static int waitTimeOnException = 3000;

    //异常处理器
    public static CommonExceptionHandler exceptionHandler = new CommonExceptionHandler();

    //应用启动时间
    public static long applicationStartTime = 0;

    //发布包版本
    public static PackageVersion packageVersion = PackageVersion.DEBUG;

    @Override
    public void onCreate() {

        //启动调试线程
     /*   WorkThread.postByLoop("#APP调试线程", () -> {
            onDebugCode();
            Threads.sleep(5 * 1000L);
        });*/

        //记录应用启动时间
        applicationStartTime = Times.millisOfNow();

        super.onCreate();

        //初始化全局环境
        initContext();

        //初始化项目目录
        ProjectFile.initProjectDirectory();

        //为主线程设置异常处理器
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            CommonApplication.exceptionHandler.onException(t, e, false);
        });

        //订阅事件
        EventBus.core.subscribe(this);

        //统计应用运行时长
        LaunchTime.start();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    //初始化全局环境
    private void initContext() {
        CommonApplication.ctx = this;
        CommonApplication.handler = new Handler();
        CommonApplication.mainTid = Process.myTid();
        MainThread.init();
        TipBox.init(this);
        Uris.init(this);
        CollectCrashUtils.initColleteCrash(this);
    }

    public LinkedList<CommonActivity> getTaskStack() {
        return activityStack;
    }

    public void addToStack(CommonActivity activity) {
        activityStack.addLast(activity);
        CommonApplication.componentState.value = ComponentState.StateValue.ALIVE;
    }

    public void removeFromStack(Activity activity) {
        activityStack.remove(activity);
        MainThread.postLater(() -> {
            if (activityStack.isEmpty())
                finishAllProcess();
        }, 500);
    }

    //获取当前Activity
    public static CommonActivity currentActivity() {
        if (currentActivity != null) return currentActivity;
        if (!activityStack.isEmpty()) return activityStack.getLast();
        return null;
    }

    public void finishAll() {
        CommonApplication.componentState.value = ComponentState.StateValue.DISPOSED;
        for (Activity activity : activityStack)
            activity.finish();
    }

    //结束当前子进程
    public void finishCurrentProcess() {
        finishAll();
        Process.killProcess(Process.myPid());
    }

    //结束所有子进程
    public void finishAllProcess() {
        finishAll();
        WorkThread.postLater(() -> {
            ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = manager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcesses)
                if (processInfo.pid != Applications.currentProcessId())
                    Process.killProcess(processInfo.pid);
            Process.killProcess(Applications.currentProcessId());
        }, 10);
    }

    //延时结束进程
    public void finishProcessLater() {
        finishProcessLater(300);
    }

    //延时结束进程
    public void finishProcessLater(long ms) {
        finishAll();
        handler.postDelayed(this::finishAllProcess, ms);
    }

    //判断是不是横屏
    public static boolean isHorizontalScreen() {
        WindowManager windowManager = currentActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        return screenWidth > screenHeight;
    }

    //判断APP是否完全退出
    //如果Activity数量为0，则表示APP退出
    public static boolean isAppAlive() {
        return componentState.value == ComponentState.StateValue.DISPOSED;
    }

    //设置公司名称，供代码使用的英文名称
    public static void setCompanyName(String name) {
        CommonApplication.companyName = name;
    }

    //设置项目名称，供代码使用的英文名称
    public static void setProjectName(String name) {
        CommonApplication.projectName = name;
    }

    //设置项目名称，供用户阅读的中文名称
    public static void setProjectLabel(String label) {
        CommonApplication.projectLabel = label;
    }

    //获取本项目的外部存储目录下的文件路径
    public static String getFilePath(String relativePath) {
        String path = ProjectFile.getProjectFile(relativePath);
        Files.createFile(path);
        return path;
    }

    //获取本项目的外部存储目录下的文件夹路径
    public static String getFolderPath(String relativePath) {
        String path = ProjectFile.getProjectDirectory(relativePath);
        Files.createDirectory(path);
        return path;
    }

    //生成存储路径的人性化描述
    public static String getPathDescription(String relativePath) {
        String path = ProjectFile.getProjectPath(relativePath);
        String description = ProjectFile.getPathDescription(path);
        return description;
    }

    //设置数据版本
    //数据版本改变，意味着数据格式发生重大变化，无法兼容旧数据
    //调用checkLocalDataVersion方法检查版本是否变化，在callback中手动删除需要清理的数据
    public static void setDataVersion(Integer currentVersion) {
        CommonApplication.dataVersion = currentVersion;
    }

    //设置内置数据库版本
    public static void setInnerDBVersion(Integer version) {
        CommonApplication.innerDBVersion = version;
    }

    //设置业务数据库版本
    public static void setDatabaseVersion(Integer version) {
        CommonApplication.databaseVersion = version;
    }

    //设置在数据库升级时，是否清除旧的数据库
    public static void setClearDatabase(boolean b) {
        CommonApplication.clearDatabase = b;
    }

    //判断是否存在数据冲突，清理旧版数据
    //这个方法需要在开启数据库打开前执行，在数据库文件使用期间使用可能会引发未知问题
    public static void checkDataVersion(Action onMatch, Action onConflict) {
        Integer oldDataVersion = Preference.get("dataVersion", Integer.class);
        int currentDataVersion = CommonApplication.dataVersion;
        boolean conflict = oldDataVersion != null && oldDataVersion.intValue() != currentDataVersion;
        if (conflict)
            onConflict.runAndPostException();
        else {
            Preference.set("dataVersion", currentDataVersion);
            onMatch.runAndPostException();
        }
    }

    //启动窗口
    public static void startActivity(Class<? extends Activity> clazz) {
        Intent intent = new Intent(ctx, clazz);
        intent.putExtra("processId", Process.myPid());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    //启动窗口
    public static void startActivity2(Class<? extends Activity> clazz) {
        Intent intent = new Intent(ctx, clazz);
        intent.putExtra("processId", Process.myPid());
        CommonApplication.ctx.startActivity(intent);
    }

    //启动窗口
    public static void startActivity(Class<? extends Activity> clazz, Map<String, Serializable> params) {
        Intent intent = new Intent(ctx, clazz);
        intent.putExtra("processId", Process.myPid());
        if (params != null)
            for (String key : params.keySet())
                intent.putExtra(key, params.get(key));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    //启动服务
    public static void startService(Class<? extends Service> clazz) {
        Intent intent = new Intent(ctx, clazz);
        ctx.startService(intent);
    }

    //停止服务
    public static void stopService(Class<? extends Service> clazz) {
        Intent intent = new Intent(ctx, clazz);
        ctx.stopService(intent);
    }

    //获取服务器主机
    public String getServerHost() {
        return "www.baidu.com";
    }

    //获取服务器端口
    public int getServerPort() {
        return 80;
    }

    //处理全局异常
    public void handleGlobalException(Throwable e) {
        CommonApplication.exceptionHandler.onException(Thread.currentThread(), e, true);
    }

    //每隔5秒执行一次调试代码
    protected void onDebugCode() {

    }
}
