package com.easing.commons.android.ui_component.favorite_button;

import com.easing.commons.android.R;
import com.easing.commons.android.annotation_processor.Reflection;
import com.easing.commons.android.app.CommonActivity;

//界面收藏信息
@SuppressWarnings("all")
public class FavoritesInfo {

    //界面类
    public transient Class<? extends CommonActivity> activityClass;

    //界面类名
    public String activityClassName;

    //功能分组
    public String group = "暂未添加";

    //功能描述
    public String title = "暂未添加快捷入口";

    //界面图标
    public Integer icon = R.drawable.app_icon_m01;

    //获取Activity类型
    public Class<? extends CommonActivity> findClass() {
        return activityClass == null ? Reflection.findClass(activityClassName) : activityClass;
    }
}

