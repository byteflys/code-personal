package com.easing.commons.android.ui.wrapper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.easing.commons.android.R;
import com.easing.commons.android.R2;
import com.easing.commons.android.view.Views;

import butterknife.BindView;

@SuppressWarnings("all")
public class TextSelectButton extends FrameLayout {

    protected ViewHolder holder = new ViewHolder();

    protected Views.OnClick onTitleClick;
    protected Views.OnClick onButtonClick;

    public TextSelectButton(Context context) {
        this(context, null);
    }

    public TextSelectButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    protected void init(Context context, AttributeSet attributeSet) {
        View root = Views.inflate(context, R.layout.layout_text_select_button);
        addView(root);
        Views.viewBinding(holder, root);

        //读取属性
        TypedArray attrs = context.obtainStyledAttributes(attributeSet, R.styleable.TextSelectButton);
        Drawable selectIcon = attrs.getDrawable(R.styleable.TextSelectButton_icon);
        CharSequence title = attrs.getText(R.styleable.TextSelectButton_title);
        Boolean selected = attrs.getBoolean(R.styleable.TextSelectButton_selected, false);
        Drawable background = attrs.getDrawable(R.styleable.TextSelectButton_background);
        Boolean underline = attrs.getBoolean(R.styleable.TextSelectButton_underline, true);

        //设置属性
        if (title != null)
            holder.titleText.setText(title);
        if (selectIcon != null)
            holder.selectButton.setImageDrawable(selectIcon);
        if (background != null)
            holder.root.setBackground(background);
        Views.visibility(holder.underline, underline ? Views.VISIBLE : Views.GONE);
        holder.selectButton.setSelected(selected);

        //点击事件
        setClickable(true);
        holder.selectButton.setClickable(true);
        Views.onClick(this, () -> {
            if (onTitleClick != null)
                onTitleClick.onClick();
        });
        Views.onClick(holder.selectButton, () -> {
            if (onButtonClick != null)
                onButtonClick.onClick();
        });
    }

    public TextSelectButton titleText(String titleText) {
        Views.text(holder.titleText, titleText);
        return this;
    }

    public TextSelectButton onTitleClick(Views.OnClick onTitleClick) {
        this.onTitleClick = onTitleClick;
        return this;
    }

    public TextSelectButton onButtonClick(Views.OnClick onButtonClick) {
        this.onButtonClick = onButtonClick;
        return this;
    }

    public TextSelectButton selected(boolean selected) {
        holder.selectButton.setSelected(selected);
        return this;
    }

    public boolean selected() {
        return holder.selectButton.isSelected();
    }

    public static class ViewHolder {

        @BindView(R2.id.root)
        View root;
        @BindView(R2.id.text_title)
        TextView titleText;
        @BindView(R2.id.bt_selected)
        ImageView selectButton;
        @BindView(R2.id.underline)
        View underline;
    }
}
