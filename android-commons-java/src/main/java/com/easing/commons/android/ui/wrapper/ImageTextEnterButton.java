package com.easing.commons.android.ui.wrapper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.easing.commons.android.R;
import com.easing.commons.android.R2;
import com.easing.commons.android.manager.Dimens;
import com.easing.commons.android.view.Views;

import butterknife.BindView;

@SuppressWarnings("all")
public class ImageTextEnterButton extends FrameLayout {

    protected ViewHolder holder = new ViewHolder();

    public ImageTextEnterButton(Context context) {
        this(context, null);
    }

    public ImageTextEnterButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    protected void init(Context context, AttributeSet attributeSet) {
        View root = Views.inflate(context, R.layout.layout_image_text_enter_button);
        addView(root);
        Views.viewBinding(holder, root);

        //读取属性
        TypedArray attrs = context.obtainStyledAttributes(attributeSet, R.styleable.ImageTextEnterButton);
        Drawable leftIcon = attrs.getDrawable(R.styleable.ImageTextEnterButton_leftIcon);
        Drawable rightIcon = attrs.getDrawable(R.styleable.ImageTextEnterButton_rightIcon);
        int leftPadding = (int) attrs.getDimension(R.styleable.ImageTextEnterButton_leftPadding, Dimens.toPx(5));
        int rightPadding = (int) attrs.getDimension(R.styleable.ImageTextEnterButton_rightPadding, Dimens.toPx(10));
        CharSequence title = attrs.getText(R.styleable.ImageTextEnterButton_title);
        Drawable background = attrs.getDrawable(R.styleable.ImageTextEnterButton_background);
        Boolean underline = attrs.getBoolean(R.styleable.ImageTextEnterButton_underline, true);

        //设置属性
        if (leftIcon != null)
            holder.leftIconImage.setImageDrawable(leftIcon);
        if (rightIcon != null)
            holder.rightIconImage.setImageDrawable(rightIcon);
        holder.leftIconImage.setPadding(leftPadding, leftPadding, leftPadding, leftPadding);
        holder.rightIconImage.setPadding(rightPadding, rightPadding, rightPadding, rightPadding);
        if (title != null)
            holder.titleText.setText(title);
        if (background != null)
            holder.root.setBackground(background);
        Views.visibility(holder.underline, underline ? Views.VISIBLE : Views.GONE);

        //点击事件
        setClickable(true);
    }

    public ImageTextEnterButton title(String title) {
        Views.text(holder.titleText, title);
        return this;
    }

    public ImageTextEnterButton textSize(int dpSize) {
        holder.titleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, Dimens.toPx(dpSize));
        return this;
    }

    public static class ViewHolder {

        @BindView(R2.id.root)
        View root;
        @BindView(R2.id.text_title)
        TextView titleText;
        @BindView(R2.id.image_left_icon)
        ImageView leftIconImage;
        @BindView(R2.id.image_right_icon)
        ImageView rightIconImage;
        @BindView(R2.id.underline)
        View underline;
    }
}
