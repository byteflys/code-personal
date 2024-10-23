package com.easing.commons.android.ui_component.tab_layout;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.easing.commons.android.manager.Dimens;
import com.easing.commons.android.view.Views;

public class TextTabItem extends AppCompatTextView implements VerticalTabItem {

    boolean selected;

    @Override
    public void selected(boolean selected) {
        boolean needInvalidate = this.selected != selected;
        this.selected = selected;
        if (needInvalidate) invalidate();
    }

    @Override
    public boolean selected() {
        return selected;
    }

    @Override
    public void setTitle(String title) {
        setText(title);
    }

    @Override
    public void textSize(int dp) {
        setTextSize(Dimens.DP, dp);
    }

    @Override
    public void textColor(int colorResource) {
        setTextColor(getContext().getResources().getColor(colorResource));
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    public TextTabItem(Context context) {
        this(context, null);
    }

    public TextTabItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        Views.onClick(this, () -> {
            VerticalTabLayout parent = (VerticalTabLayout) getParent().getParent().getParent().getParent();
            String title = getText().toString();
            parent.select(title);
        });
    }
}
