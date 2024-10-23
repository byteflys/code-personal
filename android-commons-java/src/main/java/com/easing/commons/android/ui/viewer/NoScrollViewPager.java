package com.easing.commons.android.ui.viewer;

import android.content.Context;

import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * 不能滑动的viewpager
 */
public class NoScrollViewPager extends ViewPager {
    private boolean noScroll = true; //true 代表不能滑动 //false 代表能滑动

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        return !noScroll && super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return !noScroll && super.onInterceptTouchEvent(arg0);
    }

}