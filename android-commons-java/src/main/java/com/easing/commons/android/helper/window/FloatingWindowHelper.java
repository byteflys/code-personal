package com.easing.commons.android.helper.window;

import static android.content.Context.WINDOW_SERVICE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.easing.commons.android.manager.Device;

import java.util.HashMap;
import java.util.Map;

/**
 * 悬浮窗功能实现帮助类
 */
public class FloatingWindowHelper {
    private WindowManager mWindowManager;
    private Map<View, WindowManager.LayoutParams> mChildViewMap;

    private Context context;
    private static FloatingWindowHelper floatingWindowHelper;

    private FloatingWindowHelper(Context context) {
        mWindowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        this.context = context;
    }


    public static FloatingWindowHelper initFloating(Context context) {

        if (floatingWindowHelper == null)
            floatingWindowHelper = new FloatingWindowHelper(context);
        return floatingWindowHelper;
    }

    private int gravity = Gravity.TOP;

    /**
     * 设置gravity
     */
    public void setViewGravity(int gravity) {
        this.gravity = gravity;
    }

    /**
     * 创建模板 WindowManager.LayoutParams 对象
     */
    private WindowManager.LayoutParams createLayoutParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity =/* Gravity.CENTER_HORIZONTAL | */gravity;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        return layoutParams;
    }

    /**
     * 添加并显示悬浮View
     *
     * @param view    要悬浮的View
     * @param canMove 是否可以拖动
     */
    public void addView(View view, int y, boolean canMove) {
        if (mWindowManager == null) {
            return;
        }
        if (mChildViewMap == null) {
            mChildViewMap = new HashMap<>();
        }
        if (!mChildViewMap.containsKey(view)) {
            WindowManager.LayoutParams layoutParams = createLayoutParams();

            mChildViewMap.put(view, layoutParams);
            mWindowManager.addView(view, layoutParams);

            if (canMove) {
                view.setOnTouchListener(new FloatingOnTouchListener());
            }
        }
    }

    private int rotation = 0;

    /**
     * 判断是否存在该悬浮View
     */
    public boolean contains(View view) {
        if (mChildViewMap != null) {
            return mChildViewMap.containsKey(view);
        }
        return false;
    }


    /**
     * 移除指定悬浮View
     */
    public void removeView(View view) {
        if (mWindowManager == null) {
            return;
        }
        if (mChildViewMap != null) {
            mChildViewMap.remove(view);
        }
        mWindowManager.removeView(view);
    }

    /**
     * 根据 LayoutParams 更新悬浮 View 布局
     */
    public void updateViewLayout(View view, WindowManager.LayoutParams layoutParams) {
        if (mWindowManager == null) {
            return;
        }
        if (mChildViewMap != null && mChildViewMap.containsKey(view)) {
            mChildViewMap.put(view, layoutParams);
            mWindowManager.updateViewLayout(view, layoutParams);
        }
    }

    /**
     * 获取指定悬浮View的 LayoutParams
     */
    public WindowManager.LayoutParams getLayoutParams(View view) {
        if (mChildViewMap.containsKey(view)) {
            return mChildViewMap.get(view);
        }
        return null;
    }


    public void clear() {
        if (mWindowManager == null) {
            return;
        }
        for (View view : mChildViewMap.keySet()) {
            mWindowManager.removeView(view);
        }
        mChildViewMap.clear();
    }

    public void destroy() {
        if (mWindowManager == null) {
            return;
        }
        for (View view : mChildViewMap.keySet()) {
            mWindowManager.removeView(view);
        }
        mChildViewMap.clear();
        mChildViewMap = null;
        mWindowManager = null;
        floatingWindowHelper = null;
    }

    /**
     * 判断是否拥有悬浮窗权限
     *
     * @param isApplyAuthorization 是否申请权限
     */
    public static boolean canDrawOverlays(Context context, boolean isApplyAuthorization) {
        //Android 6.0 以下无需申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //判断是否拥有悬浮窗权限，无则跳转悬浮窗权限授权页面
            if (Settings.canDrawOverlays(context)) {
                return true;
            } else {
                if (isApplyAuthorization) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
                    if (context instanceof Service) {
                        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    }
                    context.startActivity(intent);
                    return false;
                } else {
                    return false;
                }
            }
        } else {
            return true;
        }
    }


    /**
     * 处理触摸事件实现悬浮View拖动效果
     */
    public class FloatingOnTouchListener implements View.OnTouchListener {
        private int MOVEx;
        private int MOVEy;

        private int downX, downY;

        private boolean isSizeView = false;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (mChildViewMap.containsKey(view)) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        WindowManager.LayoutParams layoutPara = mChildViewMap.get(view);

                        downX = layoutPara.x;
                        downY = layoutPara.y;

                        MOVEx = (int) event.getRawX();
                        MOVEy = (int) event.getRawY();

                        break;
                    case MotionEvent.ACTION_MOVE:

                        WindowManager.LayoutParams layoutParams = mChildViewMap.get(view);
                        int nowX = (int) event.getRawX();
                        int nowY = (int) event.getRawY();


                        if (nowY - MOVEy > 10 ||
                                nowX - MOVEx > 10 ||
                                MOVEy - nowY > 10 ||
                                MOVEx - nowX > 10) { //移动距离太小则不移动


                            if (!isSizeView) { //移动

                                int movedX = nowX - MOVEx;
                                int movedY = nowY - MOVEy;
                                MOVEx = nowX;
                                MOVEy = nowY;
                                layoutParams.x = layoutParams.x + movedX;
                                layoutParams.y = layoutParams.y + movedY;

                            } else { //设置大小

                                if (rotation == 0) { //未旋转

                                    layoutParams.width = Math.max((nowX - layoutParams.x), 500);
                                    layoutParams.height = Math.max((nowY - layoutParams.y), 400);

                                } else { //旋转了90度
                                    layoutParams.width = Math.max((nowY - layoutParams.y), 500);
                                    layoutParams.height = Math.max((nowX - layoutParams.x), 400);

                                }
                                layoutParams.x = downX;
                                layoutParams.y = downY;

                           /* if (layoutParams.width < 500) {
                                layoutParams.width = 500;
                            }
                            if (layoutParams.height < 500) {
                                layoutParams.height = 500;
                            }*/


                                if (layoutParams.width > Device.screenWidth()) {
                                    layoutParams.width = Device.screenWidth();
                                }

                          /*  WindowManager.LayoutParams layoutParams1 = (WindowManager.LayoutParams) view.getLayoutParams();
                            layoutParams1.height = layoutParams.height;
                            layoutParams1.width = layoutParams.width;
                            view.setLayoutParams(layoutParams1);*/

                            }
                            mWindowManager.updateViewLayout(view, layoutParams);
                        }


                        break;
                    default:
                        break;
                }
            }
            return view.onTouchEvent(event);
        }
    }

}
