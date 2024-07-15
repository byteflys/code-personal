package com.android.architecture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

//放大镜效果，对手触区域进行局部特写
@SuppressWarnings("all")
public class MagnifierView extends View {

    //放大倍数
    int scale = 2;
    //放大半径
    int radius = 150;

    //原图
    Bitmap srcBitmap;
    //放大后的图
    Bitmap scaledBitmap;
    //局部放大图
    ShapeDrawable ovalShapeDrawable;

    //绘制时的控制位置的变换矩阵
    Matrix matrix;

    public MagnifierView(Context context) {
        this(context, null);
    }

    public MagnifierView(Context context, AttributeSet attributeSet) {
        super(context, null);
        srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.gai_lun);
        scaledBitmap = Bitmap.createScaledBitmap(srcBitmap, srcBitmap.getWidth() * scale, srcBitmap.getHeight() * scale, true);
        BitmapShader bitmapShader = new BitmapShader(scaledBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        ovalShapeDrawable = new ShapeDrawable(new OvalShape());
        ovalShapeDrawable.getPaint().setShader(bitmapShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画原图
        canvas.drawBitmap(srcBitmap, 0, 0, null);
        //没点击前不画
        if (matrix == null)
            return;
        //画局部放大图
        ovalShapeDrawable.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action != MotionEvent.ACTION_DOWN && action != MotionEvent.ACTION_MOVE)
            return true;
        int x = (int) event.getX();
        int y = (int) event.getY();
        //设置圆的绘制位置
        ovalShapeDrawable.setBounds(x - radius, y - radius, x + radius, y + radius);
        //通过矩阵控制BitmapShaper的位置，从而使放大区域正好显示在圆内
        if (matrix == null)
            matrix = new Matrix();
        matrix.setTranslate(-(x * scale - radius), -(y * scale - radius));
        ovalShapeDrawable.getPaint().getShader().setLocalMatrix(matrix);
        //重绘
        invalidate();
        return true;
    }
}

