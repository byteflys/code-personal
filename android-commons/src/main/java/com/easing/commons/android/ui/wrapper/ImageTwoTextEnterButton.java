package com.easing.commons.android.ui.wrapper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import com.easing.commons.android.R;
import com.easing.commons.android.R2;
import com.easing.commons.android.manager.Dimens;
import com.easing.commons.android.view.Views;

import butterknife.BindView;

@SuppressWarnings("all")
public class ImageTwoTextEnterButton extends FrameLayout {

    protected ViewHolder holder = new ViewHolder();

    public ImageTwoTextEnterButton(Context context) {
        this(context, null);
    }

    public ImageTwoTextEnterButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    protected void init(Context context, AttributeSet attributeSet) {
        View root = Views.inflate(context, R.layout.layout_image_two_text_enter_button);
        addView(root);
        Views.viewBinding(holder, root);

        //清空默认文本
        holder.primaryText.setText("");
        holder.secondaryText.setText("");

        //读取属性
        TypedArray attrs = context.obtainStyledAttributes(attributeSet, R.styleable.ImageTwoTextEnterButton);
        Drawable leftIcon = attrs.getDrawable(R.styleable.ImageTwoTextEnterButton_leftIcon);
        Drawable rightIcon = attrs.getDrawable(R.styleable.ImageTwoTextEnterButton_rightIcon);
        int leftPadding = (int) attrs.getDimension(R.styleable.ImageTwoTextEnterButton_leftPadding, Dimens.toPx(5));
        int rightPadding = (int) attrs.getDimension(R.styleable.ImageTwoTextEnterButton_rightPadding, Dimens.toPx(10));
        CharSequence primaryText = attrs.getText(R.styleable.ImageTwoTextEnterButton_primaryText);
        CharSequence secondaryText = attrs.getText(R.styleable.ImageTwoTextEnterButton_secondaryText);
        Drawable background = attrs.getDrawable(R.styleable.ImageTwoTextEnterButton_background);
        Boolean underline = attrs.getBoolean(R.styleable.ImageTwoTextEnterButton_underline, true);

        //设置属性
        if (leftIcon != null)
            holder.leftIconImage.setImageDrawable(leftIcon);
        if (rightIcon != null)
            holder.rightIconImage.setImageDrawable(rightIcon);
        holder.leftIconImage.setPadding(leftPadding, leftPadding, leftPadding, leftPadding);
        holder.rightIconImage.setPadding(rightPadding, rightPadding, rightPadding, rightPadding);
        if (primaryText != null)
            holder.primaryText.setText(primaryText);
        if (primaryText != null)
            holder.secondaryText.setText(secondaryText);
        if (background != null)
            holder.root.setBackground(background);
        Views.visibility(holder.underline, underline ? Views.VISIBLE : Views.GONE);

        //点击事件
        setClickable(true);
    }

    public ImageTwoTextEnterButton leftIcon(@DrawableRes int drawableId) {
        holder.leftIconImage.setImageResource(drawableId);
        return this;
    }

    public ImageTwoTextEnterButton rightIcon(@DrawableRes int drawableId) {
        holder.rightIconImage.setImageResource(drawableId);
        return this;
    }

    public ImageTwoTextEnterButton primaryText(String primaryText) {
        Views.text(holder.primaryText, primaryText);
        return this;
    }

    public ImageTwoTextEnterButton secondaryText(String secondaryText) {
        Views.text(holder.secondaryText, secondaryText);
        return this;
    }

    public static class ViewHolder {

        @BindView(R2.id.root)
        View root;
        @BindView(R2.id.text_primary)
        TextView primaryText;
        @BindView(R2.id.text_secondary)
        TextView secondaryText;
        @BindView(R2.id.image_left_icon)
        ImageView leftIconImage;
        @BindView(R2.id.image_right_icon)
        ImageView rightIconImage;
        @BindView(R2.id.underline)
        View underline;
    }
}
