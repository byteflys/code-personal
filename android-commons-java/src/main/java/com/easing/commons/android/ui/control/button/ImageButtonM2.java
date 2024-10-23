package com.easing.commons.android.ui.control.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easing.commons.android.view.Views;
import com.easing.commons.android.R;

//图标居左，文字居中，不可调整格式
@SuppressWarnings("all")
public class ImageButtonM2 extends LinearLayout {

    ViewGroup root;
    ImageView imageView;
    TextView textView;

    public ImageButtonM2(Context context) {
        this(context, null);
    }

    public ImageButtonM2(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    protected void init(Context context, AttributeSet attributeSet) {
        TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.ImageButtonM2);
        String text = array.getText(R.styleable.ImageButtonM2_text).toString();
        int textColor = array.getColor(R.styleable.ImageButtonM2_textColor, getResources().getColor(R.color.color_black_70));
        Drawable imageDrawable = array.getDrawable(R.styleable.ImageButtonM2_image);
        Drawable backgroundDrawable = array.getDrawable(R.styleable.ImageButtonM2_background);

        root = Views.inflate(context, R.layout.layout_image_button_m02);

        if (backgroundDrawable == null)
            backgroundDrawable = getResources().getDrawable(R.drawable.button_m06);
        setBackground(backgroundDrawable);

        imageView = root.findViewById(R.id.image);
        if (imageDrawable != null)
            imageView.setImageDrawable(imageDrawable);

        textView = root.findViewById(R.id.text);
        textView.setText(text);
        textView.setTextColor(textColor);

        super.setClickable(true);
        super.addView(root, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public ImageButtonM2 showIcon(boolean show) {
        imageView.setVisibility(show ? View.VISIBLE : View.GONE);
        return this;
    }

    public ImageButtonM2 buttonText(String text) {
        textView.setText(text);
        return this;
    }
}
