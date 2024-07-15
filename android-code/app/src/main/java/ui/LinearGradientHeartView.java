package com.android.architecture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

@SuppressWarnings("all")
public class LinearGradientHeartView extends View {

    public LinearGradientHeartView(Context context) {
        this(context, null);
    }

    public LinearGradientHeartView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    protected void init(Context context, AttributeSet attributeSet) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getContext().getResources().getDrawable(R.drawable.icon_heart);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        //取BitmapShader和LinearGradient交集处的色彩叠加值
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        LinearGradient linearGradient = new LinearGradient(0, 0, w, h, Color.GREEN, Color.BLUE, Shader.TileMode.CLAMP);
        ComposeShader composeShader = new ComposeShader(bitmapShader, linearGradient, PorterDuff.Mode.MULTIPLY);
        //为画笔设置着色器效果
        Paint paint = new Paint();
        paint.setShader(composeShader);
        //绘制Rect，同时附带Shader特效
        //移到中间，放大绘制，这样看起来更明显
        Matrix matrix = new Matrix();
        matrix.postTranslate(200, 200);
        matrix.postScale(2, 2, w / 2, h / 2);
        canvas.setMatrix(matrix);
        canvas.drawRect(0, 0, w+2000, h, paint);
        invalidate();
    }
}

