package com.android.CoordinatorLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

@SuppressWarnings("all")
public class Behavior {

    public Behavior(Context context, AttributeSet attrs) {

    }

    public void onLayoutFinish(View parent, View child) {

    }

    public void onSizeChanged(View parent, View child, int w, int h, int oldw, int oldh) {

    }

    public boolean onTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
        return false;
    }

    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        return false;
    }

    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    public void onStopNestedScroll(View child) {

    }

    public void onNestedScrollAccepted(View child, View target, int axes) {

    }

    public void onNestedScroll(View scrollView, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

    }

    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {

    }

    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }
}

