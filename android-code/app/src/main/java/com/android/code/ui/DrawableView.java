package com.android.code.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.android.code.R;

@SuppressWarnings("all")
public class DrawableView extends View {

    Drawable drawable;

    //动画进度
    int level = 0;
    //动画方向
    //先从左往右，到达最右侧，再从右往左
    int positive = 1;

    public DrawableView(Context context) {
        this(context, null);
    }

    public DrawableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Drawable baseDrawable = context.getResources().getDrawable(R.drawable.gai_lun);
        drawable = new HalfGreyDrawable(baseDrawable);
        //只绘制整个图像中间80%的部分
        //这里主要是为了演示Drawable.setBounds的作用
        drawable.setBounds(
                baseDrawable.getIntrinsicWidth() * 1 / 10,
                baseDrawable.getIntrinsicHeight() * 1 / 10,
                baseDrawable.getIntrinsicWidth() * 9 / 10,
                baseDrawable.getIntrinsicHeight() * 9 / 10
        );
        //Drawable变化时，重新绘制View
        //也可以不用Callback，直接invalidate
        //这里主要是为了演示Drawable.invalidateSelf的刷新机制
        drawable.setCallback(new Drawable.Callback() {

            @Override
            public void invalidateDrawable(Drawable d) {
                invalidate();
            }

            @Override
            public void scheduleDrawable(Drawable d, Runnable r, long t) {

            }

            @Override
            public void unscheduleDrawable(Drawable d, Runnable r) {

            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //裁剪Canvas，只在右下角部分绘制
        //这里主要是为了演示Canvas.clipRect的作用
        Rect clipRect = new Rect();
        Gravity.apply(
                Gravity.RIGHT | Gravity.BOTTOM,
                getMeasuredWidth() / 2,
                getMeasuredHeight() / 2,
                new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight()),
                clipRect
        );
        canvas.clipRect(clipRect);
        //画HalfGreyDrawable
        drawable.draw(canvas);
        //增加Drawable动画效果
        postDelayed(() -> {
            level += positive;
            if (level == 0)
                positive = 1;
            if (level == 100)
                positive = -1;
            drawable.setLevel(level);
        }, 20);
    }
}