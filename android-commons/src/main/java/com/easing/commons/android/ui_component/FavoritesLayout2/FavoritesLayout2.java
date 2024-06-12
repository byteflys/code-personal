package com.easing.commons.android.ui_component.FavoritesLayout2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.easing.commons.android.R;
import com.easing.commons.android.annotation_processor.AnnotationHandler;
import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.preference.Preference;
import com.easing.commons.android.struct.StringList;
import com.easing.commons.android.thread.MainThread;
import com.easing.commons.android.ui.control.list.easy_list.NonRecycleListView;
import com.easing.commons.android.ui_component.favorite_button.ActivityFavorites;
import com.easing.commons.android.ui_component.favorite_button.FavoritesInfo;
import com.easing.commons.android.view.Views;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("all")
public class FavoritesLayout2 extends NonRecycleListView {

    //最小收藏数，不够时用空白填充
    protected int minItemCount;

    public FavoritesLayout2(Context context) {
        this(context, null);
    }

    public FavoritesLayout2(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        verticalLayout();
    }

    public void setMininumItemCount(int count) {
        minItemCount = count;
    }

    //刷新布局
    public void invalidateFavorites() {
        StringList clazzNames = Preference.get("activityFavorites", StringList.class, new StringList());
        List<FavoritesInfo> infos = new LinkedList();
        for (String clazzName : clazzNames) {
            FavoritesInfo info = new FavoritesInfo();
            info.activityClassName = clazzName;
            info.activityClass = info.findClass();
            ActivityFavorites annotation = AnnotationHandler.findAnnotation(info.activityClass, ActivityFavorites.class);
            info.group = annotation.group();
            info.title = annotation.title();
            info.icon = AnnotationHandler.findDrawable(annotation.icon());
            infos.add(info);
        }
        addFavorites(infos);
    }

    //设置收藏列表
    public void addFavorites(List<FavoritesInfo> infos) {
        post(() -> {

            //移除旧控件
            removeAllViews();

            //添加快捷入口
            for (int i = 0; i < infos.size(); i++) {

                //添加Item
                FavoritesInfo info = infos.get(i);
                View root = addItem(R.layout.md001_favorites_layout2);
                View container = root.findViewById(R.id.container);

                //设置入口标题图标
                TextView groupText = root.findViewById(R.id.groupText);
                TextView titleText = root.findViewById(R.id.titleText);
                groupText.setText(info.group);
                titleText.setText(info.title);
                ImageView iconImage = root.findViewById(R.id.iconImage);
                iconImage.setImageResource(info.icon);

                //设置点击事件
                Views.onClick(container, () -> {
                    //防止重复操作
                    setEnabled(false);
                    MainThread.postLater(() -> setEnabled(true), 1000);
                    //启动Activity
                    Class<? extends CommonActivity> activityClass = info.findClass();
                    CommonApplication.ctx.startActivity(activityClass);
                });
            }

            //添加占位入口
            for (int i = infos.size(); i < minItemCount; i++) {
                //添加Item
                View root = addItem(R.layout.md001_favorites_layout2);
            }
        });
    }

}
