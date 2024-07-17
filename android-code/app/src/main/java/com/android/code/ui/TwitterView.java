package com.android.code.ui;import com.android.code.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;import com.android.code.R;

@SuppressWarnings("all")
public class TwitterView extends View {

    //推特图标
    Bitmap dst;
    //用于裁剪图标的蒙板
    Bitmap src;

    Paint paint = new Paint();

    public TwitterView(Context context) {
        this(context, null);
    }

    public TwitterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        dst = BitmapFactory.decodeResource(getResources(), R.drawable.twiter, null);
        src = BitmapFactory.decodeResource(getResources(), R.drawable.twiter_mask, null);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画图标
        canvas.drawBitmap(dst, new Rect(0, 0, dst.getWidth(), dst.getHeight()), new Rect(100, 100, 400, 500), paint);
        //画蒙板
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
        canvas.drawBitmap(src, new Rect(0, 0, src.getWidth(), src.getHeight()), new Rect(100, 100, 400, 500), paint);
        paint.setXfermode(null);
    }
}

