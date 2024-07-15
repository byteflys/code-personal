package com.android.architecture;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;

@SuppressWarnings("all")
public class LinearGradientTextView extends androidx.appcompat.widget.AppCompatTextView {

    Shader shader;

    float w;
    float h;
    float textWidth;
    float translateX;

    public LinearGradientTextView(Context context) {
        this(context, null);
    }

    public LinearGradientTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    protected void init(Context context, AttributeSet attributeSet) {
        //设置测试文字
        setText("ABCDEFGHIJKLMN\nABCDEFGHIJKL\nABCDEFGHIJ");
        setTextColor(Color.WHITE);
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
        setPadding(50, 50, 50, 50);
        //获得TextView的画笔
        Paint paint = getPaint();
        //计算渐变位置
        String text = getText().toString();
        textWidth = paint.measureText(text.split("\n")[0]);
        w = textWidth / text.length() * 4; //渐变4个字符的长度
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        h = fontMetrics.bottom - fontMetrics.top;
        //为画笔设置Shader效果
        shader = new LinearGradient(
                -w, h, 0, h, //渐变线参考线，参考线的垂线上所有点色彩值都一样
                new int[]{0x22FFFFFF, 0xFFFFFFFF, 0x22FFFFFF}, //渐变色
                new float[]{0.0F, 0.5F, 1.0F}, ///渐变色的位置
                Shader.TileMode.CLAMP //当绘制区域大于图形本身时，重复绘制最后一个像素的色彩值
        );
        paint.setShader(shader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //渐变位置随时间进行偏移，从而形成霓虹灯效果
        translateX += 10;
        if (translateX > textWidth)
            translateX = translateX % textWidth;
        //偏移后再绘制
        Matrix matrix = new Matrix();
        matrix.postTranslate(translateX, 0);
        shader.setLocalMatrix(matrix);
        //绘制文字
        super.onDraw(canvas);
        //刷新下一帧
        postInvalidateDelayed(10);
    }
}

