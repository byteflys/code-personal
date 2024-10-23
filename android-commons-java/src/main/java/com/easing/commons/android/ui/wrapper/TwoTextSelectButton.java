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
import com.easing.commons.android.value.color.Colors;
import com.easing.commons.android.view.Views;

import butterknife.BindView;

@SuppressWarnings("all")
public class TwoTextSelectButton extends FrameLayout {

    protected ViewHolder holder = new ViewHolder();

    protected Views.OnClick onTitleClick;
    protected Views.OnClick onButtonClick;

    public TwoTextSelectButton(Context context) {
        this(context, null);
    }

    public TwoTextSelectButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    protected void init(Context context, AttributeSet attributeSet) {
        View root = Views.inflateAndAttach(context, R.layout.layout_two_text_select_button, this, true);
        Views.viewBinding(holder, root);

        TypedArray attrs = context.obtainStyledAttributes(attributeSet, R.styleable.TwoTextSelectButton);
        Boolean selected = attrs.getBoolean(R.styleable.TwoTextSelectButton_selected, false);
        Drawable selectIcon = attrs.getDrawable(R.styleable.TwoTextSelectButton_icon);
        CharSequence primaryText = attrs.getText(R.styleable.TwoTextSelectButton_primaryText);
        CharSequence secondaryText = attrs.getText(R.styleable.TwoTextSelectButton_secondaryText);
        Drawable background = attrs.getDrawable(R.styleable.TwoTextSelectButton_background);
        Boolean underline = attrs.getBoolean(R.styleable.TwoTextSelectButton_underline, true);
        int underlineColor = attrs.getColor(R.styleable.TwoTextSelectButton_underlineColor, Colors.getColor(R.color.color_grey));

        holder.selectButton.setSelected(selected);
        if (selectIcon != null)
            holder.selectButton.setImageDrawable(selectIcon);
        if (primaryText != null)
            holder.primaryText.setText(primaryText);
        if (secondaryText != null)
            holder.secondaryText.setText(secondaryText);
        if (background != null)
            holder.root.setBackground(background);
        Views.visibility(holder.underline, underline ? Views.VISIBLE : Views.GONE);
        holder.underline.setBackgroundColor(underlineColor);

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

    public TwoTextSelectButton primaryText(String primaryText) {
        Views.text(holder.primaryText, primaryText);
        return this;
    }

    public TwoTextSelectButton secondaryText(String secondaryText) {
        Views.text(holder.secondaryText, secondaryText);
        return this;
    }

    public TwoTextSelectButton onTitleClick(Views.OnClick onTitleClick) {
        this.onTitleClick = onTitleClick;
        return this;
    }

    public TwoTextSelectButton onButtonClick(Views.OnClick onButtonClick) {
        this.onButtonClick = onButtonClick;
        return this;
    }

    public TwoTextSelectButton selected(boolean selected) {
        holder.selectButton.setSelected(selected);
        return this;
    }

    public boolean selected() {
        return holder.selectButton.isSelected();
    }

    public static class ViewHolder {

        @BindView(R2.id.root)
        View root;
        @BindView(R2.id.text_primary)
        TextView primaryText;
        @BindView(R2.id.text_secondary)
        TextView secondaryText;
        @BindView(R2.id.bt_selected)
        ImageView selectButton;
        @BindView(R2.id.underline)
        View underline;
    }
}
