package com.easing.commons.android.ui.control.list.easy_list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import lombok.Setter;

//带有拦截触摸事件功能的RecyclerView
public class BaseListView extends BounceRecyclerView {

    @Setter
    boolean interceptable = false;

    public BaseListView(Context context) {
        this(context, null);
    }

    public BaseListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        enableBounceEffect(true);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (interceptable)
            return true;
        return super.onInterceptTouchEvent(e);
    }
}
