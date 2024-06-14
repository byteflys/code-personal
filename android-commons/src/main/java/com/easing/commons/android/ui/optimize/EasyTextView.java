package com.easing.commons.android.ui.optimize;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.easing.commons.android.R;

public class EasyTextView extends AppCompatTextView {

    boolean nonNull = false;

    public EasyTextView(Context context) {
        this(context, null);
    }

    public EasyTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    //初始化
    protected void init(Context context, AttributeSet attributeSet) {
        TypedArray attrs = context.obtainStyledAttributes(attributeSet, R.styleable.EasyTextView);
        nonNull = attrs.getBoolean(R.styleable.EasyTextView_nonNull, false);
    }

    //设置必填
    public EasyTextView setNonNull(boolean b) {
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

        if (!nonNull) return;

        TextPaint paint = getPaint();
        paint.setColor(0xFFFF0000);
        String text = getText().toString();
        float textWidth = CanvasPainter.getTextWidth(paint, text);
        float textHeight = CanvasPainter.getTextHeight(paint);
        float centerX = CanvasPainter.getTextCenterX(this, text);
        float centerY = CanvasPainter.getTextCenterY(this);
        float x = centerX + textWidth / 2;
        float y = centerY - textHeight / 2;
        CanvasPainter.drawTextFromLeftTop(canvas, paint, "*", x, y);
    }


}

