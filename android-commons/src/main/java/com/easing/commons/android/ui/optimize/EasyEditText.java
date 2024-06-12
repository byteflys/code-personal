package com.easing.commons.android.ui.optimize;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.easing.commons.android.R;

public class EasyEditText extends AppCompatEditText {

    boolean nonNull = false;

    public EasyEditText(Context context) {
        this(context, null);
    }

    public EasyEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    //初始化
    protected void init(Context context, AttributeSet attributeSet) {
        TypedArray attrs = context.obtainStyledAttributes(attributeSet, R.styleable.EasyEditText);
        nonNull = attrs.getBoolean(R.styleable.EasyEditText_nonNull, false);
    }

    //设置必填
    public EasyEditText setNonNull(boolean b) {
        this.nonNull = b;
        invalidate();
        return this;
    }

    //获取必填
    public boolean isNonNull() {
        return nonNull;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    public void setEnabled(boolean isEnabled) {
        setFocusable(isEnabled);
        setFocusableInTouchMode(isEnabled);
    }
}

