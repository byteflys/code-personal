package com.android.code.ui;import com.android.code.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;import com.android.code.R;

@SuppressWarnings("all")
public class ScratchCard extends View {

    //刮刮卡原图
    Bitmap dst1;
    //刮刮卡结果
    Bitmap dst2;
    //橡皮擦路径组成的图像
    Bitmap src;

    //橡皮擦路径
    float lastX;
    float lastY;
    Path path = new Path();

    Paint paint = new Paint();

    public ScratchCard(Context context) {
        this(context, null);
    }

    public ScratchCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        dst1 = BitmapFactory.decodeResource(getResources(), R.drawable.card, null);
        dst2 = BitmapFactory.decodeResource(getResources(), R.drawable.card_result, null);
        src = Bitmap.createBitmap(700, 400, Bitmap.Config.ARGB_8888);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(40);
        paint.setStyle(Paint.Style.STROKE);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //将橡皮擦路径画成图像
        Canvas bitmapCanvas = new Canvas(src);
        bitmapCanvas.drawPath(path, paint);
        //画刮刮卡
        canvas.drawBitmap(dst1, new Rect(0, 0, dst1.getWidth(), dst1.getHeight()), new Rect(100, 100, 700, 400), paint);
        //新建一个图层，保证刮刮卡的像素不参与运算
        canvas.saveLayer(0, 0, getWidth(), getHeight(), null);
        //画刮刮卡结果图像
        canvas.drawBitmap(dst2, new Rect(0, 0, dst2.getWidth(), dst2.getHeight()), new Rect(100, 100, 700, 400), paint);
        //画橡皮擦路径
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(src, 0, 0, paint);
        paint.setXfermode(null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            path.moveTo(x, y);
            lastX = x;
            lastY = y;
            //View的默认行为是return false，直接结束事件流，这样就无法触发Move事件了
            return true;
        }
        if (action == MotionEvent.ACTION_MOVE) {
            float x = event.getX();
            float y = event.getY();
            path.quadTo(lastX, lastY, (lastX + x) / 2, (lastY + y) / 2);
            lastX = x;
            lastY = y;
        }
        invalidate();
        return super.onTouchEvent(event);
    }
}

