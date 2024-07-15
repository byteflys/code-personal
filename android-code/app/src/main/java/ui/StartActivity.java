package com.android.architecture;

import android.widget.FrameLayout;

import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.manager.Permissions;
import com.easing.commons.android.ui.control.title_bar.TitleBar;
import com.easing.commons.android.ui.dialog.BottomSlideDialog;
import com.easing.commons.android.view.Views;

import butterknife.BindView;
import lombok.SneakyThrows;

@SuppressWarnings("all")
public class StartActivity extends CommonActivity<StartActivity> {

    @BindView(R.id.titleBar)
    TitleBar titleBar;

    @BindView(R.id.container)
    FrameLayout container;

    BottomSlideDialog dialog;

    @Override
    protected boolean beforeCreate() {
        statusBarColor = R.color.color_transparent;
        navigationBarColor = R.color.color_black;
        return super.beforeCreate();
    }

    @Override
    @SneakyThrows
    protected void create() {
        setContentView(R.layout.activity_start);
        titleBar.setBackgroundResource(R.drawable.color_black);
        titleBar.setTitleText("Shader");
        requestPermission(Permissions.STORAGE);
        dialog = BottomSlideDialog.create(ctx, R.layout.dialog_menu);
        onClick(container, dialog::show);
        init();
    }

    protected void init() {
        //LinearGradient实现霓虹灯效果
        dialog.onClick(R.id.menu1, () -> {
            container.removeAllViews();
            container.addView(new LinearGradientTextView(ctx), new FrameLayout.LayoutParams(Views.MATCH_PARENT, Views.MATCH_PARENT));
            dialog.close();
        });
        //BitmapShader+LinearGradient实现渐变爱心效果
        dialog.onClick(R.id.menu2, () -> {
            container.removeAllViews();
            container.addView(new LinearGradientHeartView(ctx), new FrameLayout.LayoutParams(Views.MATCH_PARENT, Views.MATCH_PARENT));
            dialog.close();
        });
        //SweepGradient实现雷达图
        dialog.onClick(R.id.menu3, () -> {
            container.removeAllViews();
            container.addView(new RadarView(ctx), new FrameLayout.LayoutParams(Views.MATCH_PARENT, Views.MATCH_PARENT));
            dialog.close();
        });
        //BitmapShader和ShapeDrawable实现雷达图
        dialog.onClick(R.id.menu4, () -> {
            container.removeAllViews();
            container.addView(new MagnifierView(ctx), new FrameLayout.LayoutParams(Views.MATCH_PARENT, Views.MATCH_PARENT));
            dialog.close();
        });
    }
}


