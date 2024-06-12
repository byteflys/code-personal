package com.easing.commons.android.ui_component.container;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridLayout;

import com.easing.commons.android.manager.Dimens;
import com.easing.commons.android.ui_component.ViewContainer;
import com.easing.commons.android.ui_component.Layout;
import com.easing.commons.android.view.Views;

//菜单按钮容器
@SuppressWarnings("all")
public class GridContainer extends GridLayout implements ViewContainer {

    int padding = Dimens.toPx(10);

    int column = 3;

    public GridContainer(Context context) {
        this(context, null);
    }

    public GridContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    //初始化
    private void init(Context context, AttributeSet attrs) {
        setColumnCount(column);
        post(() -> {
            int width = getMeasuredWidth() / 3;
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View view = getChildAt(i);
                Views.size(view, width, width);
            }
        });
    }

    @Override
    public GridContainer init(Layout layout, View... items) {
        removeAllViews();
        for (View item : items)
            add(item);
        return this;
    }

    @Override
    public GridContainer add(View item) {
        addView(item);
        return this;
    }

    @Override
    public GridContainer remove(View item) {
        removeView(item);
        return this;
    }

    @Override
    public ViewContainer column(int count) {
        setColumnCount(count);
        return this;
    }

}
