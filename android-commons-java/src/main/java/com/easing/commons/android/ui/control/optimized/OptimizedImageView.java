package com.easing.commons.android.ui.control.optimized;

import android.content.Context;
import android.util.AttributeSet;

import com.easing.commons.android.time.Times;
import com.easing.commons.android.ui.control.image.RippledImageView;
import com.easing.commons.android.view.Views;

//优化响应时间，连续点击无效
//自带波纹效果，不用自己设置多种图片状态
public class OptimizedImageView extends RippledImageView {

    Views.OnClick listener;
    long lastClickTime = 0;

    public OptimizedImageView(Context context) {
        this(context, null);
    }

    public OptimizedImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    protected void init(Context context, AttributeSet attributeSet) {
        Views.onClick(this, () -> {
            if (listener != null && Times.millisOfNow() - lastClickTime > 400) {
                lastClickTime = Times.millisOfNow();
                listener.onClick();
            }
        });
    }

    public void onClick(Views.OnClick listener) {
        this.listener = listener;
    }


}
