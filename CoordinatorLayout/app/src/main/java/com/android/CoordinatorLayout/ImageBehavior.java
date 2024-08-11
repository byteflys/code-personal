package com.android.CoordinatorLayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

@SuppressWarnings("all")
public class ImageBehavior extends Behavior {

    Float minHeight;
    Float maxHeight;

    public ImageBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray attributes = context.obtainStyledAttributes(attributeSet, R.styleable.MyCoordinatorLayout);
        minHeight = attributes.getDimension(R.styleable.MyCoordinatorLayout_min_height, 0);
        maxHeight = attributes.getDimension(R.styleable.MyCoordinatorLayout_max_height, 0);
    }

    @Override
    public void onNestedScroll(View scrollView, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        int scroll = scrollView.getScrollY();
        //最顶端往上拉，滑动值变大，图片逐渐变小
        if (scroll > 0) {
            ViewGroup.LayoutParams layoutParams = target.getLayoutParams();
            layoutParams.height = layoutParams.height - Math.abs(dyConsumed);
            if (layoutParams.height < minHeight)
                layoutParams.height = minHeight.intValue();
            target.setLayoutParams(layoutParams);
        }
        //最顶端往下拉，滑动值不变，图片逐渐增大
        if (scroll == 0) {
            ViewGroup.LayoutParams layoutParams = target.getLayoutParams();
            layoutParams.height = layoutParams.height + Math.abs(dyUnconsumed);
            if (layoutParams.height >= maxHeight)
                layoutParams.height = maxHeight.intValue();
            target.setLayoutParams(layoutParams);
        }
    }
}

