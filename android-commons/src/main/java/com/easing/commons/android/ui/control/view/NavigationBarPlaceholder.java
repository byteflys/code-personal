package com.easing.commons.android.ui.control.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.manager.Device;
import com.easing.commons.android.value.color.Colors;
import com.easing.commons.android.view.Views;

public class NavigationBarPlaceholder extends View {

    public NavigationBarPlaceholder(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    protected void init(Context context, AttributeSet attributeSet) {
        setBackgroundColor(Colors.TRANSPARENT);
        post(() -> {
            CommonActivity activity = (CommonActivity) getContext();
            activity.detectNavigationBar();
            if (activity.getHasNavigationBar()) {
                int navigationBarHeight = Device.navigationBarHeight(getContext());
                Views.size(this, null, navigationBarHeight);
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
    }

}
