package com.easing.commons.android.ui.control.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.easing.commons.android.manager.Device;
import com.easing.commons.android.value.color.Colors;
import com.easing.commons.android.view.Views;

/**
 * 状态栏
 */
public class StatusBarPlaceholder extends View {

    public StatusBarPlaceholder(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    protected void init(Context context, AttributeSet attributeSet) {
        setBackgroundColor(Colors.TRANSPARENT);
        post(() -> {
            int statuBarHeight = Device.statuBarHeight(getContext());
            Views.size(this, null, statuBarHeight);
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
    }
}
