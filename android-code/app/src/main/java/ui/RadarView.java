package com.android.architecture;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

@SuppressWarnings("all")
public class RadarView extends View {

    Paint circlePaint;
    Paint radarPaint;

    float degree;

    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    protected void init(Context context, AttributeSet attributeSet) {
        //画圆环的画笔
        circlePaint = new Paint();
        circlePaint.setStrokeWidth(5);
        circlePaint.setColor(Color.RED);
        circlePaint.setStyle(Paint.Style.STROKE);
        //画辐射图的画笔
        radarPaint = new Paint();
        radarPaint.setColor(Color.RED);
        radarPaint.setStyle(Paint.Style.FILL);
        //辐射效果
        SweepGradient radialGradient = new SweepGradient(
                300, 300,
                new int[]{Color.TRANSPARENT, Color.RED},
                null
        );
        radarPaint.setShader(radialGradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //计算旋转角度
        degree += 10;
        if (degree > 360)
            degree = degree % 360;
        //平移旋转画布
        Matrix matrix = new Matrix();
        matrix.postRotate(degree, 300, 300);
        matrix.postTranslate(200, 200);
        canvas.setMatrix(matrix);
        //画环形
        for (int r = 50; r <= 300; r += 50)
            canvas.drawCircle(300, 300, r, circlePaint);
        //画辐射图
        canvas.drawCircle(300, 300, 300, radarPaint);
        //刷新下一帧
        postInvalidateDelayed(50);
    }
}

