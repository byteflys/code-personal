package com.easing.commons.android.ui_component.ModuleLayout2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.easing.commons.android.R;
import com.easing.commons.android.helper.callback.Action;
import com.easing.commons.android.helper.callback.DataCallback;
import com.easing.commons.android.manager.Dimens;
import com.easing.commons.android.module.Module;
import com.easing.commons.android.thread.MainThread;
import com.easing.commons.android.time.LaunchTime;
import com.easing.commons.android.ui.control.layout.FallsLayout;
import com.easing.commons.android.ui_component.module_button.ModuleInfo;
import com.easing.commons.android.view.Views;

import java.util.List;

//模块布局，支持模块分组功能
@SuppressWarnings("all")
public class ModuleLayout2 extends FallsLayout {

    int moduleMargin;
    int moduleWidth;

    Action onLayoutComplete;

    private DataCallback<ModuleInfo> click;

    public ModuleLayout2(Context context) {
        this(context, null);
    }

    public ModuleLayout2(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    //初始化
    protected void init(Context context, AttributeSet attributeSet) {
        super.init(context, attributeSet);
    }

    public void setModuleLongClick(DataCallback<ModuleInfo> click) {
        this.click = click;
    }

    //设置模块列表
    public void modules(List<ModuleInfo> infos) {
        post(() -> {
            //移除旧控件
            measureViewSize();
            removeAllViews();
            //动态添加模块按钮
            for (ModuleInfo info : infos) {
                boolean isMain = info.subModuleInfos == null;
                //添加主模块
                if (isMain) {
                    View mainModuleLayout = Views.inflateAndAttach(context, R.layout.md001_module_layout2_main, this, false);
                    ImageView iconImage = Views.findView(mainModuleLayout, R.id.iconImage);
                    TextView titleText = Views.findView(mainModuleLayout, R.id.titleText);
                    iconImage.setImageResource(info.icon);
                    titleText.setText(info.name);
                    Views.size(mainModuleLayout, moduleWidth, null);
                    addView(mainModuleLayout);
                    bindModuleEvent(mainModuleLayout, info);
                }
                //添加分组
                else {
                    ViewGroup moduleGroupLayout = Views.inflateAndAttach(context, R.layout.md001_module_layout2_group, this, false);
                    TextView titleText = Views.findView(moduleGroupLayout, R.id.titleText);
                    titleText.setText(info.name);
                    Views.size(moduleGroupLayout, moduleWidth, Views.WRAP_CONTENT);
                    Views.size(moduleGroupLayout, moduleWidth, null);
                    addView(moduleGroupLayout);
                    //添加子模块
                    for (ModuleInfo subInfo : info.subModuleInfos) {
                        View splitView = Views.createEmptyView(context, 0, 10);
                        moduleGroupLayout.addView(splitView);
                        View subModuleLayout = Views.inflateAndAttach(context, R.layout.md001_module_layout2_secondary, moduleGroupLayout, false);
                        ImageView iconImage2 = Views.findView(subModuleLayout, R.id.iconImage);
                        TextView titleText2 = Views.findView(subModuleLayout, R.id.titleText);
                        iconImage2.setImageResource(subInfo.icon);
                        titleText2.setText(subInfo.name);
                        moduleGroupLayout.addView(subModuleLayout);
                        bindModuleEvent(subModuleLayout, subInfo);
                    }
                }
            }
        });
    }


    //绑定模块事件
    protected void bindModuleEvent(View button, ModuleInfo info) {
        Views.onClick(button, () -> {
            //模块功能暂未开放
            if (info.clazz == null)
                return;
            //防止重复操作
            setEnabled(false);
            MainThread.postLater(() -> setEnabled(true), 1000);
            //启动模块
            Module module = info.module();
            module.register();
            module.launch();
        });

        button.setOnTouchListener(new OnTouchListener() {

            Long lastTouchTime = null;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    lastTouchTime = LaunchTime.time;
                }
                if (action == MotionEvent.ACTION_MOVE) {
                    long now = LaunchTime.time;
                    if (lastTouchTime != null)
                        if (now - lastTouchTime > 5 * 1000L)
                            if (click != null) {
                                lastTouchTime = null;
                                click.onDataChange(info);
                            }
                }
                if (action == MotionEvent.ACTION_UP) {
                    lastTouchTime = null;
                }
                if (action == MotionEvent.ACTION_CANCEL) {
                    lastTouchTime = null;
                }
                return false;
            }
        });
//        Views.onLongClick(button, () -> {
//            if (click != null)
//                click.onDataChange(info);
//        });
    }

    //测量控件大小
    protected void measureViewSize() {
        moduleMargin = Dimens.toPx(5);
        moduleWidth = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - moduleMargin * 4) / 2;
    }

    //设置布局加载完成回调
    public void onLayoutComplete(Action callback) {
        this.onLayoutComplete = callback;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (onLayoutComplete != null)
            onLayoutComplete.runAndPostException();
    }
}
