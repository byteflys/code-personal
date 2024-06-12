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
public class ImageTwoTextButton extends FrameLayout {

    protected ViewHolder holder = new ViewHolder();

    public ImageTwoTextButton(Context context) {
        this(context, null);
    }

    public ImageTwoTextButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    protected void init(Context context, AttributeSet attributeSet) {

        //设置背景
        setBackgroundResource(R.drawable.container_back_white_ripple);

        //加载根布局
        View root = Views.inflate(context, R.layout.layout_image_two_text_button);
        addView(root);
        Views.viewBinding(holder, root);

        //解析自定义属性
        TypedArray attrs = context.obtainStyledAttributes(attributeSet, R.styleable.ImageTwoTextButton);
        Drawable drawable = attrs.getDrawable(R.styleable.ImageTwoTextButton_image);
        int imagePadding = (int) attrs.getDimension(R.styleable.ImageTwoTextButton_imagePadding, Dimens.toPx(10));
        int rightIconPadding = (int) attrs.getDimension(R.styleable.ImageTwoTextButton_rightIconPadding, Dimens.toPx(25));
        CharSequence primaryText = attrs.getText(R.styleable.ImageTwoTextButton_primaryText);
        CharSequence secondaryText = attrs.getText(R.styleable.ImageTwoTextButton_secondaryText);
        Drawable background = attrs.getDrawable(R.styleable.ImageTwoTextButton_background);
        Drawable rightIcon = attrs.getDrawable(R.styleable.ImageTwoTextButton_rightIcon);
        Boolean underline = attrs.getBoolean(R.styleable.ImageTwoTextButton_underline, true);
        Boolean showRightIcon = attrs.getBoolean(R.styleable.ImageTwoTextButton_showRightIcon, true);

        //设置属性
        if (drawable != null)
            holder.image.setImageDrawable(drawable);
        holder.image.setPadding(imagePadding, imagePadding, imagePadding, imagePadding);
        holder.rightIcon.setPadding(rightIconPadding, rightIconPadding, rightIconPadding, rightIconPadding);
        if (primaryText != null)
            holder.primaryText.setText(primaryText);
        if (secondaryText != null)
            holder.secondaryText.setText(secondaryText);
        if (background != null)
            setBackground(background);
        if (rightIcon != null)
            holder.rightIcon.setImageDrawable(rightIcon);
        Views.visibility(holder.underline, underline ? Views.VISIBLE : Views.INVISIBLE);
        Views.visibility(holder.rightIcon, showRightIcon ? Views.VISIBLE : Views.GONE);

        //可点击
        setClickable(true);
    }

    public ImageTwoTextButton primaryText(String primaryText) {
        Views.text(holder.primaryText, primaryText);
        return this;
    }

    public ImageTwoTextButton secondaryText(String secondaryText) {
        Views.text(holder.secondaryText, secondaryText);
        return this;
    }

    public ImageTwoTextButton rightIcon(@DrawableRes int drawableId) {
        holder.rightIcon.setImageResource(drawableId);
        return this;
    }

    public ImageTwoTextButton showRightIcon(boolean show) {
        Views.visibility(holder.rightIcon, show ? Views.VISIBLE : Views.INVISIBLE);
        return this;
    }

    public static class ViewHolder {

        @BindView(R2.id.root)
        View root;
        @BindView(R2.id.image)
        ImageView image;
        @BindView(R2.id.text_primary)
        TextView primaryText;
        @BindView(R2.id.text_secondary)
        TextView secondaryText;
        @BindView(R2.id.underline)
        View underline;
        @BindView(R2.id.icon_right)
        ImageView rightIcon;
    }
}
