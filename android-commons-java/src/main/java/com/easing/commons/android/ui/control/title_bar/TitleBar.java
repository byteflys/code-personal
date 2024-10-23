package com.easing.commons.android.ui.control.title_bar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;

import com.easing.commons.android.R;
import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.view.Views;

//默认布局只有标题和返回按钮
//如果有其它按钮，模仿默认布局添加按钮，然后设置布局和点击事件即可
@SuppressWarnings("all")
public class TitleBar extends FrameLayout {

    View root;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    protected void init(Context context, AttributeSet attributeSet) {
        setLayout(R.layout.layout_title_bar);
        setTitleText("标题栏");
        setRootBackground(R.drawable.color_transparent);
        showReturnButton(true);
    }

    public TitleBar setLayout(@LayoutRes int layoutID) {
        root = Views.inflate(getContext(), layoutID);
        removeAllViews();
        addView(root);
        onClick(R.id.bt_return, () -> {
            CommonActivity activity = (CommonActivity) getContext();
            activity.finish();
        });
        return this;
    }

    public <T extends View> T getView(@IdRes int viewId) {
        T view = root.findViewById(viewId);
        return view;
    }

    public TitleBar setTitleText(String text) {
        TextView textView = root.findViewById(R.id.text_title);
        textView.setText(text);
        return this;
    }

    public TitleBar setRootBackground(@DrawableRes int drawableID) {
        root.setBackgroundResource(drawableID);
        return this;
    }

    public TitleBar setStatuBarBackground(@DrawableRes int drawableID) {
        root.findViewById(R.id.status_bar).setBackgroundResource(drawableID);
        return this;
    }

    public TitleBar setTitleBarBackground(@DrawableRes int drawableID) {
        root.findViewById(R.id.layout_title).setBackgroundResource(drawableID);
        return this;
    }

    public TitleBar showReturnButton(boolean show) {
        root.findViewById(R.id.bt_return).setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        return this;
    }

    public TitleBar setReturnButtonColor(@ColorRes int colorID) {
        ImageView imageView = root.findViewById(R.id.bt_return);
        imageView.setImageTintList(ColorStateList.valueOf(getResources().getColor(colorID, null)));
        return this;
    }

    public TitleBar setTitleColor(@ColorRes int colorID) {
        TextView textView = root.findViewById(R.id.text_title);
        textView.setTextColor(getResources().getColor(colorID, null));
        return this;
    }

    public TitleBar onClick(@IdRes int viewId, Views.OnClick listener) {
        Views.onClick(root.findViewById(viewId), listener);
        return this;
    }
}
