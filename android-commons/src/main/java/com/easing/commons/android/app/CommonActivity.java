package com.easing.commons.android.app;

import static com.easing.commons.android.value.identity.Codes.CODE_AI_IMAGE_COPE;
import static com.easing.commons.android.value.identity.Codes.CODE_AI_IMAGE_SELECT;
import static com.easing.commons.android.value.identity.Codes.CODE_QR_IMAGE_COPE;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Process;
import android.provider.MediaStore;
import android.transition.Slide;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.easing.commons.android.R;
import com.easing.commons.android.annotation_processor.AnnotationHandler;
import com.easing.commons.android.data.Storable;
import com.easing.commons.android.event.EventBus;
import com.easing.commons.android.event.OnEvent;
import com.easing.commons.android.format.Maths;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.helper.thread.ComponentState;
import com.easing.commons.android.io.AndroidFile;
import com.easing.commons.android.io.Files;
import com.easing.commons.android.io.ProjectFile;
import com.easing.commons.android.manager.Device;
import com.easing.commons.android.media.MediaType;
import com.easing.commons.android.redirection.ViewRouter;
import com.easing.commons.android.struct.Collections;
import com.easing.commons.android.thread.Handlers;
import com.easing.commons.android.thread.MainThread;
import com.easing.commons.android.thread.WorkThread;
import com.easing.commons.android.time.Times;
import com.easing.commons.android.helper.callback.Action;
import com.easing.commons.android.manager.Uris;
import com.easing.commons.android.ui.control.record.AudioRecorder;
import com.easing.commons.android.ui.control.view.NavigationBarPlaceholder;
import com.easing.commons.android.ui.dialog.TipBox;
import com.easing.commons.android.value.apk.SdkVersion;
import com.easing.commons.android.value.identity.Actions;
import com.easing.commons.android.value.identity.Codes;
import com.easing.commons.android.value.identity.Keys;
import com.easing.commons.android.value.identity.RequestCodeUtils;
import com.easing.commons.android.view.Views;
import com.easing.commons.android.value.identity.Values;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lombok.Getter;

//通过泛型实现ctx的自动转型
@SuppressWarnings("all")
public class CommonActivity<T extends CommonActivity> extends AppCompatActivity implements Storable {

    public T ctx;
    public Handler handler;

    protected long createTime;
    protected int resumeTimes;

    protected Values.ORIENTATION fixedOrientation = Values.ORIENTATION.PORTRAIT;

    //记录Activity状态
    public final ComponentState componentState = ComponentState.create();

    //记录是不是首个界面，首个界面在进程重置后不需要销毁
    //可以重写beforeCreate方法，在beforeCreate
    protected Boolean isTopActivity = false;

    //是否在Activity结束时，结束进程
    protected Boolean killProcessOnDestroy = false;

    //是否在Activity结束时，自动切换到主任务栈的Activity
    protected Boolean returnMainTaskOnDestroy = false;

    //状态栏和导航栏颜色
    protected Integer statusBarColor = R.color.color_black_80;
    protected Integer navigationBarColor = R.color.color_black_90;

    //状态栏和导航栏适配
    protected Boolean floatStatusAndNavigationBar = true;
    protected Boolean customStatusAndNavigationBarColor = true;
    @Getter
    protected Boolean hasNavigationBar = null;

    //布局是否初始化完毕
    @Getter
    protected boolean isLayoutCompletion = false;

    //Activity顶级View
    public Window window;
    public View decorView;
    public ViewGroup rootView;

    //拍照保存路径
    String imageCapturePath;
    //录像保存路径
    String videoCapturePath;

    //坐标选取请求码
    public String requestCodeForLocationPick;
    //申请权限请求码
    public Integer requestCodeForPermission;

    //选取文件前是否拷贝
    boolean copyToPrivate = true;

    //进程重建恢复时，是否销毁当前窗口
    protected boolean killOnProcessReset() {
        return !isTopActivity;
    }

    //进程重建恢复
    protected void onProcessReset() {
    }

    //onCreate之前执行的代码
    //返回false表示结束当前Activity
    protected boolean beforeCreate() {
        try {
            //判断进程是否是被恢复的新进程
            if (!isTopActivity) {
                Bundle extras = getIntent().getExtras();
                if (extras != null && extras.containsKey("processId")) {
                    int processId = extras.getInt("processId");
                    if (processId != Process.myPid())
                        if (killOnProcessReset()) {
                            onProcessReset();
                            return false;
                        }
                }
            }
            //屏幕始终打开
            super.getWindow().setFlags(LayoutParams.FLAG_KEEP_SCREEN_ON, LayoutParams.FLAG_KEEP_SCREEN_ON);
            //不自动弹出键盘
            super.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            //固定横屏或竖屏显示
            setFixedOrientation(fixedOrientation);
        } catch (Throwable e) {
            CommonApplication.ctx.handleGlobalException(e);
        }
        return true;
    }

    //设置屏幕方向
    public void setFixedOrientation(Values.ORIENTATION fixedOrientation) {
        //总是竖屏显示
        if (fixedOrientation == Values.ORIENTATION.PORTRAIT)
            super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //总是横屏显示
        if (fixedOrientation == Values.ORIENTATION.LANDSCAPE)
            super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    //判断是不是横屏
    public boolean isHorizontalScreen() {
        return CommonApplication.isHorizontalScreen();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            //判断是否需要阻止Activity创建
            boolean create = beforeCreate();
            super.onCreate(savedInstanceState);
            //保存全局环境
            this.ctx = (T) this;
            this.handler = CommonApplication.handler;
            this.createTime = Times.millisOfNow();
            //加入任务栈
            this.myApplication().addToStack(this);
            //保持屏幕常亮
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            //设置切换动画
            getWindow().setAllowEnterTransitionOverlap(true);
            getWindow().setAllowReturnTransitionOverlap(true);
            Slide slide = new Slide();
            getWindow().setEnterTransition(slide);
            getWindow().setExitTransition(slide);
            //是否启动当前Activity
            if (create) {
                window = getWindow();
                //window.setStatusBarContrastEnforced(false);
                //window.setNavigationBarContrastEnforced(false);
                if (Device.sdkVersionCode() > SdkVersion.ANDROID_10) {
                    window.setStatusBarContrastEnforced(false);
                    window.setNavigationBarContrastEnforced(false);
                }
                componentState.value = ComponentState.StateValue.ALIVE;
                CommonApplication.currentActivity = this;
                EventBus.core.subscribe(this);
                floatStatusAndNavigationBar();
                customStatusAndNavigationBarColor();
                create();
                Handlers.postLater(handler, () -> {
                    isLayoutCompletion = true;
                    onLayoutCompletion();
                }, 1000);
            } else {
                finish();
            }
        } catch (Throwable e) {
            CommonApplication.ctx.handleGlobalException(e);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        Views.viewBinding(this, ctx);
        decorView = window.getDecorView();
        rootView = Views.getRootView(this);
    }

    @Override
    protected void onResume() {
        try {
            resumeTimes++;
            super.onResume();
            CommonApplication.currentActivity = this;
            //有些弹窗会改变布局，所以在弹出关闭时，要恢复布局
            if (!isFirstLauch()) {
                floatStatusAndNavigationBar();
                customStatusAndNavigationBarColor();
            }
            resume();
        } catch (Throwable e) {
            CommonApplication.ctx.handleGlobalException(e);
        }
    }

    @Override
    protected void onPause() {
        try {
            super.onPause();
            pause();
        } catch (Throwable e) {
            CommonApplication.ctx.handleGlobalException(e);
        }
    }

    @Override
    protected void onStart() {
        try {
            super.onStart();
            start();
        } catch (Throwable e) {
            CommonApplication.ctx.handleGlobalException(e);
        }
    }

    @Override
    protected void onStop() {
        try {
            super.onStop();
            stop();
        } catch (Throwable e) {
            CommonApplication.ctx.handleGlobalException(e);
        }
    }

    @Override
    protected void onDestroy() {
        try {
            destroy();
            componentState.value = ComponentState.StateValue.DISPOSED;
            if (CommonApplication.currentActivity == this)
                CommonApplication.currentActivity = null;
            super.onDestroy();
            EventBus.core.unsubscribe(this);
            myApplication().removeFromStack(this);

            //从最近任务列表中移除
            ActivityManager manager = getSystemService(ActivityManager.class);
            List<ActivityManager.AppTask> tasks = manager.getAppTasks();
            for (ActivityManager.AppTask task : tasks) {
                ActivityManager.RecentTaskInfo info = task.getTaskInfo();
                if (info.numActivities == 0)
                    task.finishAndRemoveTask();
            }

            //如果任务栈为空，结束进程
            tasks = manager.getAppTasks();
            int totalActivityNum = 0;
            for (ActivityManager.AppTask task : tasks) {
                ActivityManager.RecentTaskInfo info = task.getTaskInfo();
                totalActivityNum += info.numActivities;
            }
            if (killProcessOnDestroy && totalActivityNum == 0)
                myApplication().finishAllProcess();

            //将主任务栈带回前台
            if (returnMainTaskOnDestroy) {
                tasks = manager.getAppTasks();
                for (ActivityManager.AppTask task : tasks) {
                    ActivityManager.RecentTaskInfo info = task.getTaskInfo();
                    String action = info.baseIntent.getAction();
                    boolean isMainTask = Texts.equal(action, "android.intent.action.MAIN");
                    if (isMainTask && info.numActivities != 0)
                        task.moveToFront();
                }
            }
        } catch (Throwable e) {
            CommonApplication.ctx.handleGlobalException(e);
            super.onDestroy();
        }
    }

    //布局完全加载完成
    protected void onLayoutCompletion() {

    }

    //根据路由规则自动跳转到其它界面
    public void autoRedirect() {
        //查找Activity上的@Redirection注解，根据注解自动跳转
        String ruleName = AnnotationHandler.findRedirectionAnnotation(ctx);
        ViewRouter.autoRedirect(ctx, ruleName);
    }

    //根据路由规则自动跳转到其它界面
    public void autoRedirect(View view) {
        //查找View上的@Redirection注解，根据注解自动跳转
        String ruleName = AnnotationHandler.findRedirectionAnnotation(ctx, view);
        ViewRouter.autoRedirect(view, ruleName);
    }

    //onCreate代码，由子类自己定义
    protected void create() {
    }

    //子类定义，实现自己的方法
    protected void resume() {
    }

    //子类定义，实现自己的方法
    protected void pause() {
    }

    //子类定义，实现自己的方法
    protected void start() {
    }

    //子类定义，实现自己的方法
    protected void stop() {
    }

    //onDestroy代码，由子类自己定义
    protected void destroy() {
    }

    //申请权限
    public void requestPermission(String... permissions) {
        requestCodeForPermission = Codes.randomCode();
        requestPermissions(permissions, requestCodeForPermission);
    }

    //处理申请权限结果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        onPermissionResult();
    }

    //申请权限完成
    //建议在这个回调里，自己重现判断有没有权限，不要用requestPermission返回的结果
    protected void onPermissionResult() {

    }

    //解析布局
    public <T extends View> T inflate(int layoutId) {
        return (T) LayoutInflater.from(ctx).inflate(layoutId, null);
    }

    //获取所在的APP
    public <T extends CommonApplication> T myApplication() {
        return (T) super.getApplication();
    }

    //隐藏窗口
    public void hide() {
        super.moveTaskToBack(true);
    }

    //窗口跳转
    public void start(Class<? extends Activity> clazz, Map<String, Serializable> params) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra("processId", Process.myPid());
        if (params != null)
            for (String key : params.keySet())
                intent.putExtra(key, params.get(key));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        super.startActivity(intent);
    }

    //窗口跳转，并返回结果
    public void startForResult(Class<? extends Activity> clazz, int requestCode, Map<String, Serializable> params) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra("processId", Process.myPid());
        if (params != null)
            for (String key : params.keySet())
                intent.putExtra(key, params.get(key));
        super.startActivityForResult(intent, requestCode);
    }

    //窗口跳转
    public void start(Class<? extends Activity> clazz) {
        start(clazz, null);
    }

    //窗口跳转
    public void startLater(Class<? extends Activity> clazz) {
        postLater(() -> {
            start(clazz, null);
        }, 200);
    }

    //窗口跳转，并返回结果
    public void startForResult(Class<? extends Activity> clazz, int requestCode) {
        startForResult(clazz, requestCode, null);
    }

    //关闭
    @Override
    public void finish() {
        super.finish();
    }

    //延时关闭
    public void finishLater(long ms) {
        handler.postDelayed(this::finish, ms);
    }

    //跳转并结束自己
    public void startAndFinish(Class<? extends Activity> clazz) {
        startAndFinish(clazz, null);
    }

    //跳转并结束自己
    public void startAndFinishLater(Class<? extends Activity> clazz, long ms) {
        handler.postDelayed(() -> {
            startAndFinish(clazz, null);
        }, ms);
    }


    //跳转并结束自己
    public void startAndFinish(Class<? extends Activity> clazz, Map<String, Serializable> params) {
        start(clazz, params);
        this.finish();
    }

    //停止服务
    public void stopService(Class<? extends Service> clazz) {
        Intent start_activity = new Intent(this, clazz);
        super.stopService(start_activity);
    }

    //在主线程执行
    public void post(Action r) {
        Handlers.post(handler, r);
    }

    //在主线程延时执行
    public void postLater(Action r, long ms) {
        Handlers.postLater(handler, r, ms);
    }

    //等控件绑定窗口后再执行任务
    public void postAfterViewAttach(View view, Action r) {
        Views.postAfterViewAttach(view, r);
    }

    //开始一个定时任务
    public void startActivityTimer(Action action, long intervalMs) {
        WorkThread.postByInterval(action, intervalMs, componentState);
    }

    //设置点击事件
    public void onClick(@IdRes int viewId, Views.OnClick listener) {
        View view = findViewById(viewId);
        onClick(view, listener);
    }

    //设置点击事件
    public void onClick(View view, Views.OnClick listener) {
        Views.onClick(view, () -> {
            listener.onClick();
        });
    }

    //设置长按事件
    public void onLongClick(@IdRes int viewId, Views.OnLongClick listener) {
        View view = findViewById(viewId);
        onLongClick(view, listener);
    }

    //设置长按事件
    public void onLongClick(View view, Views.OnLongClick listener) {
        Views.onLongClick(view, () -> {
            listener.onLongClick();
        });
    }

    //模拟点击
    public void click(@IdRes int viewId) {
        View view = findViewById(viewId);
        view.performClick();
    }

    public void hideStatuBar() {
        super.getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
    }

    public void showStatuBar() {
        super.getWindow().clearFlags(LayoutParams.FLAG_FULLSCREEN);
    }

    public void hideActionBar() {
        super.getSupportActionBar().hide();
    }

    public void showActionBar() {
        super.getSupportActionBar().show();
    }

    public void changeBarColor(int statuBarColor, int actionBarDrawable, int avigationBarColor) {
        super.getWindow().getDecorView().setSystemUiVisibility(0x00000000);
        super.getWindow().setStatusBarColor(getResources().getColor(statuBarColor));
        super.getSupportActionBar().setBackgroundDrawable(getDrawable(actionBarDrawable));
        super.getWindow().setNavigationBarColor(getResources().getColor(avigationBarColor));
    }

    public void translucentMode() {
        super.getWindow().addFlags(LayoutParams.FLAG_TRANSLUCENT_STATUS);
        super.getWindow().addFlags(LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    public void immersiveMode() {
        super.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
    }

    public void exitImmersiveMode(boolean showStatuBar) {
        super.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        if (showStatuBar)
            showStatuBar();
    }

    //浮动状态栏和导航栏
    //浮动的状态栏和导航栏，会悬浮在layout布局之上，layout会铺满整个Activity
    protected void floatStatusAndNavigationBar() {
        if (!floatStatusAndNavigationBar)
            return;
        super.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
    }

    //自定义状态栏和导航栏颜色
    protected void customStatusAndNavigationBarColor() {
        if (!customStatusAndNavigationBarColor)
            return;
        super.getWindow().setStatusBarColor(getColor(statusBarColor));
        super.getWindow().setNavigationBarColor(getColor(navigationBarColor));
    }

    //去除黑边
    //执行此方法前，需要先判断手机有无虚拟按键
    public void removeBlackEdge() {
        LinkedList<View> list = Views.allWindowNode(ctx);
        //一般手机，第三个节点和首个节点是等高的
        //某些全面屏手机，第三个节点会比首个节点小一个导航栏的高度
        //这样就会导致手机底部多出一块黑边
        Views.size(list.get(2), null, list.get(0).getMeasuredHeight());
    }

    //通过解析Window布局，判断手机有无虚拟按键
    //通过这个可以判断出手机是不是全面屏
    public void detectNavigationBar() {
        int navigationBarHeight = Device.navigationBarHeight(ctx);
        int screenWidth = Device.getScreenSize(ctx).w;
        List<View> nodes = Views.allWindowNode(ctx);
        nodes = Collections.filter(nodes, node -> {
            if (node.getClass() != NavigationBarPlaceholder.class)
                if (node.getMeasuredHeight() == navigationBarHeight)
                    if (node.getMeasuredWidth() == screenWidth)
                        return true;
            return false;
        });
        hasNavigationBar = nodes.size() > 0;
        //全面屏需要移除黑边
        if (hasNavigationBar)
            removeBlackEdge();
    }

    //判断是否首次进入界面
    public boolean isFirstLauch() {
        return resumeTimes <= 1;
    }

    //选择文件
    public void pickFile() {
        int requestCode = Codes.CODE_PICK_FILE;
        pickFile(requestCode, MediaType.TYPE_ALL, true);
    }

    //ai图片选择
    public void aiPickFile() {
        Intent intent = new Intent(Actions.ACTION_PICK_FILE);
        intent.setType(MediaType.TYPE_IMAGE);
        startActivityForResult(intent, CODE_AI_IMAGE_SELECT);
    }

    //选择文件
    public void pickFile(int requestCode, String mediaType) {
        pickFile(requestCode, mediaType, true);
    }

    //选择文件
    public void pickFile(int requestCode, String mediaType, boolean copyToPrivate) {
        if (mediaType == null)
            mediaType = MediaType.TYPE_ALL;
        this.copyToPrivate = copyToPrivate;
        RequestCodeUtils.addFilePickCode(requestCode);
        Intent it = new Intent(Actions.ACTION_PICK_FILE);
        it.setType(mediaType);
        startActivityForResult(it, requestCode);
    }

    //拍照，路径需是png格式
    public void captureImage() {
        captureImage(Codes.CODE_IMAGE_CAPTURE, null);
    }

    //拍照，路径需是png格式
    public void captureImage(int requestCode, String path) {
        if (path == null)
            path = ProjectFile.getProjectImagePath(Texts.random() + ".png");
        this.imageCapturePath = path;
        RequestCodeUtils.addImageCaptureCode(requestCode);
        Intent intent = new Intent(Actions.ACTION_CAPTURE_IMAGE);
        Uri uri = Uris.fromFile(path);
        intent.putExtra(Keys.KEY_OUTPUT, uri);
        startActivityForResult(intent, requestCode);
    }

    //录像，路径需是mp4格式
    public void captureVideo() {
        captureVideo(Codes.CODE_VIDEO_CAPTURE, null);
    }

    //录像，路径需是mp4格式
    public void captureVideo(int requestCode, String path) {
        if (path == null)
            path = ProjectFile.getProjectVideoPath(Texts.random() + ".mp4");
        this.videoCapturePath = path;
        RequestCodeUtils.addVideoCaptureCode(requestCode);
        Intent intent = new Intent(Actions.ACTION_CAPTURE_VIDEO);
        Uri uri = Uris.fromFile(path);
        intent.putExtra(Keys.KEY_OUTPUT, uri);
        startActivityForResult(intent, requestCode);
    }

    //录音
    public void captureAudio() {
        captureAudio(Codes.CODE_AUDIO_CAPTURE, null);
    }

    //录音
    public void captureAudio(int requestCode, String path) {
        if (path == null) {
            String filename = Texts.random() + ".aac";
            path = ProjectFile.getProjectAudioPath(filename);
        }
        RequestCodeUtils.addAudioCaptureCode(requestCode);
        AudioRecorder recorder = AudioRecorder.create().storageLocation(path);
        recorder.onRecordFinish(storagePath -> {
            RequestCodeUtils.removeAudioCaptureCode(requestCode);
            //录音处理要延时一下，因为文件写出关闭需要一点时间
            MainThread.postLater(() -> {
                onAudioCapture(requestCode, storagePath);
                EventBus.core.emit("onFileSelect", requestCode, storagePath);
            }, 300);
        });
        recorder.show(ctx);
    }

    //处理选择文件和拍照结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            //判断是否处理成功
            boolean success = (resultCode == RESULT_OK);

         /*   if (data == null)
                return;*/

            //选文件
            if (success && RequestCodeUtils.isFilePickCode(requestCode)) {
                RequestCodeUtils.removeFilePickCode(requestCode);
                String path = Uris.uriToPath(data.getData());
                // onFilePickIntent(requestCode, data); // qian.api


                //将文件拷贝到项目目录下，方便统一管理
                if (path != null) {
                    if (Files.size(path) > 1024 * 1024 * 60) {
                        MainThread.post(new Action() {
                            @Override
                            public void run() throws Exception {
                                TipBox.tip("所选文件过大，请从新选择文件");
                            }
                        });
                        return;
                    }

                    if (copyToPrivate)
                        path = Files.copyToProjectDirectory(path);
                    onFilePick(requestCode, path);
                    EventBus.core.emit("onFileSelect", requestCode, path);
                } else {
                    TipBox.tip("不受支持的资源格式");
                }
                return;
            }

            if (success && requestCode == CODE_AI_IMAGE_SELECT) { //ai图片xuanxuan

                cropImageUri(data.getData());

                return;
            }

            if (success && requestCode == CODE_QR_IMAGE_COPE) { //扫描二维码
                EventBus.core.emit("onQRCaptureResult", requestCode, data);
                onQRCaptureResult(requestCode, data);

                return;
            }

            if (success && requestCode == CODE_AI_IMAGE_COPE) { //ai图片xuanxuan
                // imageUri = data.getData();
                onAiPickIntentData(this, data.getAction());
                return;
            }

            //拍照
            if (success && RequestCodeUtils.isImageCaptureCode(requestCode)) {

                RequestCodeUtils.removeImageCaptureCode(requestCode);
                // String path = Uris.uriToPath(data.getData());
      /*          if (Files.size(path) > 1024 * 1024 * 60) {
                    MainThread.post(new Action() {
                        @Override
                        public void run() throws Exception {
                            TipBox.tip("所选文件过大，请从新选择文件");
                        }
                    });
                    return;
                }*/


                onImageCapture(requestCode, imageCapturePath);
                EventBus.core.emit("onFileSelect", requestCode, imageCapturePath);
                return;
            }

            //录像
            if (success && RequestCodeUtils.isVideoCaptureCode(requestCode)) {
                RequestCodeUtils.removeVideoCaptureCode(requestCode);
                String path = Uris.uriToPath(data.getData());
                if (Files.size(path) > 1024 * 1024 * 60) {
                    MainThread.post(new Action() {
                        @Override
                        public void run() throws Exception {
                            TipBox.tip("所选文件过大，请从新选择文件");
                        }
                    });
                    return;
                }


                onVideoCapture(requestCode, videoCapturePath);
                EventBus.core.emit("onFileSelect", requestCode, videoCapturePath);
                return;
            }

            //没有封装的调用请求，请重写这个方法进行处理
            onActivityResult(requestCode, success, data);
        } catch (Throwable e) {
            CommonApplication.ctx.handleGlobalException(e);
        }
    }

    private Uri imageUri;

    public void cropImageUri(Uri uri) { //content://media/external/images/media/38080  content://media/external/images/media/38072


        String path = AndroidFile.getAndroidPublicAlbumDirectory() + "/.ao_dun_pick." + Bitmap.CompressFormat.JPEG.toString();
        File dexfiel = new File(path);

        if (dexfiel.exists())
            dexfiel.delete();
        imageUri = Uri.fromFile(dexfiel);

        Intent intent = new Intent("com.android.camera.action.CROP");


        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        intent.setDataAndType(uri, "image/*");

        //是否裁剪 content://media/external/images/media/38058   content://com.android.providers.media.documents/document/image%3A38058

        intent.putExtra("crop", "true");
        //是否保留比例

        intent.putExtra("scale", true);

        //输入图片的Uri，指定以后，可以在这个uri获得图片

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        //是否返回图片数据，可以不用，直接用uri就可以了

        intent.putExtra("return-data", false);

        //设置输入图片格式

        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        //是否关闭面部识别

        intent.putExtra("noFaceDetection", false); // no face detection

        //启动
        startActivityForResult(intent, CODE_AI_IMAGE_COPE);
    }

    //创建透明悬浮Dialog
    //注意，这个操作需要注册"SYSTEM_ALERT_WINDOW"权限，并开启"允许在应用上层显示"功能
    public void showTransparentAlertDialog() {
        WindowManager windowManager = CommonApplication.ctx.getSystemService(WindowManager.class);
        //设置window大小
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.height = 400;
        layoutParams.width = 400;
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.TOP | Gravity.RIGHT;
        //设置window类型，这个同时也决定了window显示的优先级
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        //设置窗体特性
        layoutParams.flags = 0;
        layoutParams.flags = layoutParams.flags | LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        //创建ContentView
        //由于原生的FrameLayout不能监听返回键，我们必须重写一个自己的FrameLayout
        FrameLayout contentView = new FrameLayout(this);
        //添加内容
        TextView textView = new TextView(this);
        textView.setText("");
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
        FrameLayout.LayoutParams lpChild = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lpChild.gravity = Gravity.CENTER;
        textView.setLayoutParams(lpChild);
        contentView.addView(textView);
        //显示window
        windowManager.addView(contentView, layoutParams);
    }

    //选文件回调
    protected void onFilePick(int requestCode, String path) {
    }

    //选文件Intent回调
    protected void onFilePickIntent(int requestCode, Intent path) {
    }

    //选文件Intent回调
    protected void onAiPickIntentData(Activity activity, String uri) {
    }

    //拍照回调
    protected void onImageCapture(int requestCode, String path) {
    }

    //录像回调
    protected void onVideoCapture(int requestCode, String path) {
    }

    //录音回调
    protected void onAudioCapture(int requestCode, String path) {
    }

    protected void onQRCaptureResult(int requestCode, Intent intent) {
    }

    //调用其它窗口返回结果
    protected void onActivityResult(int requestCode, boolean success, Intent intent) {
    }

    //选取地图坐标
    //子类需要重写这个方法，来指定如何选取坐标
    //坐标选取成功后，通过EventBus发送onLocationPicked事件
    public void pickLocation(String requestCode) {
    }

    @OnEvent(type = "onLocationPicked")
    public final void onLocationPicked(String type, String requestCode, double latitude, double longitude) {
        //只保留五位小数
        latitude = Maths.keepFloat2(latitude, 6);
        longitude = Maths.keepFloat2(longitude, 6);
        onLocationPicked(requestCode, latitude, longitude);
    }

    //子类需要重写这个方法，来指定如何处理选取的坐标
    public void onLocationPicked(String requestCode, double latitude, double longitude) {

    }
}
