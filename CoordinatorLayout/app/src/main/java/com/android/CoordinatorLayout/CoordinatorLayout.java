package com.android.CoordinatorLayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.NestedScrollingParent;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import java.lang.reflect.Constructor;

@SuppressWarnings("all")
public class CoordinatorLayout extends RelativeLayout implements NestedScrollingParent, ViewTreeObserver.OnGlobalLayoutListener {

    float lastX;
    float lastY;

    public CoordinatorLayout(Context context) {
        super(context);
    }

    public CoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            LayoutParams lp = (LayoutParams) v.getLayoutParams();
            if (lp.behavior != null)
                lp.behavior.onSizeChanged(this, v, w, h, oldw, oldh);
        }
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    //当NestedScrollingChild发生滑动时，会调用此方法来通知NestedScrollingParent滑动了多少距离
    //最常见的情景是，NestedScrollView通知CoordinatorLayout
    //target：一般为NestedScrollView
    //dyConsumed：NestedScrollView单次已消费滑动距离
    //dyUnconsumed：NestedScrollView单次未消费滑动距离
    //未消费的滑动距离是指，ScrollView已经滑动到顶端，手继续往下拉，但是控件内容缺没有继续滑动，手额外拉的这部分距离就是未消费的滑动距离
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            LayoutParams lp = (LayoutParams) v.getLayoutParams();
            //关键代码，转发滑动事件
            if (lp.behavior != null)
                lp.behavior.onNestedScroll(target, v, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        }
    }

    @Override
    public void onGlobalLayout() {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            LayoutParams lp = (LayoutParams) v.getLayoutParams();
            if (lp.behavior != null)
                lp.behavior.onLayoutFinish(this, v);
        }
    }

    public class LayoutParams extends RelativeLayout.LayoutParams {

        protected Behavior behavior;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.MyCoordinatorLayout);
            String clazzName = a.getString(R.styleable.MyCoordinatorLayout_behavior);
            behavior = parseBehaivor(context, attributeSet, clazzName);
            a.recycle();
        }

        protected Behavior parseBehaivor(Context context, AttributeSet attrs, String name) {
            try {
                Class<?> aClass = Class.forName(name, true, context.getClassLoader());
                Constructor c = aClass.getConstructor(new Class[]{Context.class, AttributeSet.class});
                c.setAccessible(true);
                return (Behavior) c.newInstance(context, attrs);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

