package com.android.code.ui;import com.android.code.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class InvertImageView_SrcIn extends View {

    //原图
    Bitmap origin;
    //倒影
    Bitmap dst;
    //倒过来的图片
    Bitmap src;

    public InvertImageView_SrcIn(Context context) {
        this(context, null);
    }

    public InvertImageView_SrcIn(Context context, AttributeSet attrs) {
        super(context, attrs);
        origin = BitmapFactory.decodeResource(getResources(), R.drawable.gai_lun, null);
        dst = BitmapFactory.decodeResource(getResources(), R.drawable.shader_invert, null);
        Matrix matrix = new Matrix();
        matrix.postScale(1, -1);
        src = Bitmap.createBitmap(origin, 0, 0, origin.getWidth(), origin.getHeight(), matrix, false);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.argb(255, 150, 150, 150));
        Paint paint = new Paint();
        canvas.drawBitmap(origin, new Rect(0, 0, origin.getWidth(), origin.getHeight()), new Rect(100, 100, 600, 600), paint);
        canvas.saveLayer(0, 0, 9999, 9999, null);
        canvas.drawBitmap(dst, new Rect(0, 0, dst.getWidth(), dst.getHeight()), new Rect(100, 600, 600, 1000), paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src, new Rect(0, 0, src.getWidth(), src.getHeight()), new Rect(100, 600, 600, 1000), paint);
    }
}

