package com.easing.commons.android.ui.control.list.easy_list;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.easing.commons.android.R;
import com.easing.commons.android.manager.Dimens;
import com.easing.commons.android.ui.control.scroll.BounceScrollView;
import com.easing.commons.android.view.Views;

//使用方法
//调用horizontalLayout或verticalLayout来设置布局
//调用padding方法设置item间距
//调用addItem方法添加子控件
//也可以通过XML中的AttributeSet来配置
public class NonRecycleListView extends LinearLayout {

    Context context;

    Integer padding;

    public NonRecycleListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    protected void init(Context context, AttributeSet attributeSet) {
        this.context = context;
        TypedArray attrs = context.obtainStyledAttributes(attributeSet, R.styleable.NonRecycleListView);
        Integer orientation = attrs.getInt(R.styleable.NonRecycleListView_orientation, 1);
        Float padding = attrs.getDimension(R.styleable.NonRecycleListView_padding, Dimens.toPx(context, 10));
        if (orientation == 1)
            horizontalLayout();
        else
            verticalLayout();
        padding(padding.intValue());
    }

    public NonRecycleListView horizontalLayout() {
        setLayoutParams(new BounceScrollView.LayoutParams(Views.WRAP_CONTENT, Views.MATCH_PARENT));
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.LEFT);
        return this;
    }

    public NonRecycleListView verticalLayout() {
        setLayoutParams(new BounceScrollView.LayoutParams(Views.MATCH_PARENT, Views.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.TOP);
        return this;
    }

    public NonRecycleListView padding(int px) {
        padding = px;
        return this;
    }

    public <T extends View> T addItem(int layoutId) {

        //动态添加Item
        T view = Views.inflateAndAttach(context, layoutId, this, false);
        addView(view);

        //如果不是首个Item，需要设置边距
        if (getChildCount() > 1) {
            boolean horizontal = getOrientation() == LinearLayout.HORIZONTAL;
            if (horizontal)
                Views.margin(view, null, null, padding, null);
            else
                Views.margin(view, padding, null, null, null);
        }
        return view;
    }

    public <T extends View> T addItem(int layoutId, OnItemCreate onItemCreate) {
        T view = addItem(layoutId);
        onItemCreate.onItemCreate(view);
        return view;
    }

    public interface OnItemCreate {

        void onItemCreate(View itemView);
    }
}
