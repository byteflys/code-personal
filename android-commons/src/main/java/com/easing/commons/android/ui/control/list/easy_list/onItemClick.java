package com.easing.commons.android.ui.control.list.easy_list;

import android.view.View;

/**
 * item 点击回调
 */
public interface onItemClick<T> {
    //单击事件
    void onClick(View view, T data, int position);

    //长按事件
    void onLongClick(View view, T data, int position);

}
