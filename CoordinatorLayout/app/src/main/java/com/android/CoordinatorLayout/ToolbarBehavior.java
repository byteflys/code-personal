package com.android.CoordinatorLayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

@SuppressWarnings("all")
public class ToolbarBehavior extends Behavior {

    Float threshold;

    public ToolbarBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray attributes = context.obtainStyledAttributes(attributeSet, R.styleable.MyCoordinatorLayout);
        threshold = attributes.getDimension(R.styleable.MyCoordinatorLayout_threshold, 0);
    }

    @Override
    public void onNestedScroll(View scrollView, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        //往下拉逐渐增加不透明度，往上拉逐渐减小不透明度
        float alpha = scrollView.getScrollY() / threshold;
        if (alpha > 1)
            alpha = 1;
        target.setAlpha(alpha);
        //设置标题
        Toolbar toolbar = (Toolbar) target;
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        toolbar.setTitle("CoordinatorLayout");
        toolbar.setSubtitle("a demo for coordinator layout");
    }
}

