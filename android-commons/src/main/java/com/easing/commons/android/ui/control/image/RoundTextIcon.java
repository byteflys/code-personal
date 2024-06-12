package com.easing.commons.android.ui.control.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatTextView;

import com.easing.commons.android.R;
import com.easing.commons.android.format.Maths;
import com.easing.commons.android.manager.Dimens;
import com.easing.commons.android.manager.Fonts;
import com.easing.commons.android.value.color.Colors;

@SuppressWarnings("all")
public class RoundTextIcon extends AppCompatTextView {

    public static final transient int RED = 0xffff6655;
    public static final transient int GREEN = 0xff44bb66;
    public static final transient int BLUE = 0xff44aaff;
    public static final transient int ORANGE = 0xffffbb44;
    public static final transient int BROWN = 0xffbb7733;
    public static final transient int[] COLORS = {RED, GREEN, BLUE, ORANGE, BROWN};

    int textColor;

    int circleColor;

    boolean drawBorder;
    int borderColor;
    int borderWidth;

    Paint circlePaint;
    Paint borderPaint;

    int radius;
    int centerX;
    int centerY;

    public RoundTextIcon(Context context) {
        this(context, null);
    }

    public RoundTextIcon(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attrSet) {

        //读取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrSet, R.styleable.RoundTextIcon);
        textColor = typedArray.getColor(R.styleable.RoundTextIcon_textColor, Colors.WHITE);
        circleColor = typedArray.getColor(R.styleable.RoundTextIcon_circleColor, COLORS[Maths.randomInt(0, 4)]);
        drawBorder = typedArray.getBoolean(R.styleable.RoundTextIcon_border, false);
        borderColor = typedArray.getColor(R.styleable.RoundTextIcon_borderColor, Colors.LIGHT_BLUE);
        borderWidth = typedArray.getDimensionPixelSize(R.styleable.RoundTextIcon_borderWidth, Dimens.toPx(context, 4));
        typedArray.recycle();

        //画圆
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(circleColor);

        //画边框
        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderWidth);

        //文字居中
        setTextColor(textColor);
        setGravity(Gravity.CENTER);
        Fonts.bindFont(this, Fonts.Font.LIBIAN);
    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);
        centerX = w / 2;
        centerY = h / 2;
        radius = Math.min(w, h) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //绘制圆形背景
        canvas.drawCircle(centerX, centerY, radius, circlePaint);

        //绘制边框
        if (drawBorder)
            canvas.drawCircle(centerX, centerY, radius - borderWidth / 2, borderPaint);

        //画文字
        super.onDraw(canvas);
    }

    //设置圆圈颜色
    public void setCircleColor(int circleColor) {
        this.circleColor = Colors.getColor(circleColor);
        circlePaint.setColor(this.circleColor);
        invalidate();
    }

    //设置边框颜色
    public void setBorderColor(int borderColor) {
        this.borderColor = Colors.getColor(borderColor);
        borderPaint.setColor(this.borderColor);
        invalidate();
    }
}
