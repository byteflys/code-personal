package com.android.code.ui;import com.android.code.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.android.code.R;

public class HeartDiagramView extends View {

    //心电图
    Bitmap dst;
    //用来裁剪心电图的矩形区域
    Bitmap src;

    //心电图起始位置
    int dx = 0;

    Paint paint;

    public HeartDiagramView(Context context) {
        this(context, null);
    }

    public HeartDiagramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        dst = BitmapFactory.decodeResource(getResources(), R.drawable.heart_diagram, null);
        src = Bitmap.createBitmap(dst.getWidth(), dst.getHeight(), Bitmap.Config.ARGB_8888);
        paint = new Paint();
        paint.setColor(Color.RED);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        dx += 20;
        if (dx > dst.getWidth())
            dx = dx % dst.getWidth();
        //画一个左半边透明，右半边不透明的SRC
        //这样通过DST_IN模式与DST进行叠加后，显示的就是右半边的心电图
        Canvas bitmapCanvas = new Canvas(src);
        bitmapCanvas.drawColor(Color.RED, PorterDuff.Mode.CLEAR);
        bitmapCanvas.drawRect(dst.getWidth() - dx, 0, dst.getWidth(), dst.getHeight(), paint);
        //画心电图
        canvas.drawBitmap(dst, new Rect(0, 0, dst.getWidth(), dst.getHeight()), new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight() / 3), paint);
        //画心电图裁剪矩形
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(src, new Rect(0, 0, src.getWidth(), src.getHeight()), new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight() / 3), paint);
        paint.setXfermode(null);
        postInvalidateDelayed(10);
    }
}

