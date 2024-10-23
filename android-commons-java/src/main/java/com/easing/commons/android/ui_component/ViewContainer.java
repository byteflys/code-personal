package com.easing.commons.android.ui_component;

import android.view.View;

//UI容器，可以自定义子项和布局
public interface ViewContainer {

    //设置布局
    default ViewContainer layout(Layout layout) {
        return this;
    }

    //设置布局并添加控件
    default ViewContainer init(Layout layout, View... items) {
        return this;
    }

    //添加子项
    default ViewContainer add(View item) {
        return this;
    }

    //移除子项
    default ViewContainer remove(View item) {
        return this;
    }

    //设置列数
    default ViewContainer column(int count) {
        return this;
    }

    //设置Item宽度
    default ViewContainer width(int dp) {
        return this;
    }

    //设置Item高度
    default ViewContainer height(int dp) {
        return this;
    }
}
