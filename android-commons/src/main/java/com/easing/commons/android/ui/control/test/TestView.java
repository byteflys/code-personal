package com.easing.commons.android.ui.control.test;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.easing.commons.android.manager.Dimens;
import com.easing.commons.android.manager.Fonts;
import com.easing.commons.android.manager.Fonts.Font;
import com.easing.commons.android.ui.optimize.CanvasPainter;
import com.easing.commons.android.value.color.Colors;
import com.easing.commons.android.value.measure.Size;
import com.easing.commons.android.view.Views;

import java.util.Random;

@SuppressWarnings("all")
public class TestView extends LinearLayout {

    Object data = "Hello World";

    Paint paint;
    Rect textBounds = new Rect();
    Float fontSize = (float) Dimens.toPx(30);

    public TestView(Context context) {
        this(context, null);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    // 初始化
    private void init(Context context, AttributeSet attrSet) {
        //尺寸铺满容器
        LayoutParams params = new LayoutParams(Views.MATCH_PARENT, Views.MATCH_PARENT);
        setLayoutParams(params);

        //随机取背景色
        int color = new Random().nextInt(0xFFFFFFFF - 0xFF000000) + 0xFF000000;
        setBackgroundColor(color);

        //背景色字体色互补
        paint = new Paint();
        int reverseColor = Colors.reverseColor(color, 0xFF);
        paint.setColor(reverseColor);
        paint.setTextSize(fontSize);
        paint.setTypeface(Fonts.getTypeface(this, Font.LIBIAN));
        paint.setAntiAlias(true);

        //设置画笔阴影
        MaskFilter blur = new BlurMaskFilter(0.5f, Blur.SOLID);
        paint.setMaskFilter(blur);
        paint.setShadowLayer(1f, 0.5f, 0.5f, reverseColor);
    }

    public TestView data(Object data) {
        this.data = data;
        invalidate();
        return this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Size size = Views.size(this);
        String text = data.toString();
        float textWidth = CanvasPainter.getTextWidth(paint, text);
        float textHeight = CanvasPainter.getTextHeight(paint);
        CanvasPainter.drawTextFromCenter(canvas, paint, text, size.w / 2, size.h / 2);
        invalidate();
    }
}

