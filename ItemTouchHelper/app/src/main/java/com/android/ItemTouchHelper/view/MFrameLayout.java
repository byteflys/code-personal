package com.android.ItemTouchHelper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.android.ItemTouchHelper.util.Console;

public class MFrameLayout extends FrameLayout {

    public MFrameLayout(Context context) {
        super(context);
    }

    public MFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        Console.debug("ItemView.dispatchTouchEvent");
        boolean ret = super.dispatchTouchEvent(e);
        Console.debug("=>", "ItemView.dispatchTouchEvent", ret);
        return ret;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        Console.debug("ItemView.onInterceptTouchEvent");
        boolean ret = super.onInterceptTouchEvent(e);
        Console.debug("=>", "ItemView.onInterceptTouchEvent", ret);
        return ret;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        Console.debug("ItemView.onTouchEvent");
        boolean ret = super.onTouchEvent(e);
        Console.debug("=>", "ItemView.onTouchEvent", ret);
        return ret;
    }
}

