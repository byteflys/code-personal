package com.android.code.ui;import com.android.code.R;import com.android.code.R;

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
import android.view.View;



@SuppressWarnings("all")
public class EraserView extends View {

    //原图
    Bitmap dst;
    //橡皮擦路径组成的图像
    Bitmap src;

    //橡皮擦路径
    float lastX;
    float lastY;
    Path path = new Path();

    Paint paint = new Paint();

    public EraserView(Context context) {
        this(context, null);
    }

    public EraserView(Context context, AttributeSet attrs) {
        super(context, attrs);
        dst = BitmapFactory.decodeResource(getResources(), R.drawable.gai_lun, null);
        src = Bitmap.createBitmap(700, 700, Bitmap.Config.ARGB_8888);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(40);
        paint.setStyle(Paint.Style.STROKE);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画原图
        canvas.drawBitmap(dst, new Rect(0, 0, dst.getWidth(), dst.getHeight()), new Rect(100, 100, 700, 700), paint);
        //画橡皮擦路径组成的图像
        Canvas bitmapCanvas = new Canvas(src);
        bitmapCanvas.drawPath(path, paint);
        //画橡皮擦路径
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
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

