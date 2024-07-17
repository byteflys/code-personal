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

public class RoundImageView_SrcATop extends View {

    //圆角矩形
    Bitmap dst;
    //真实图像
    Bitmap src;

    public RoundImageView_SrcATop(Context context) {
        this(context, null);
    }

    public RoundImageView_SrcATop(Context context, AttributeSet attrs) {
        super(context, attrs);
        src = BitmapFactory.decodeResource(getResources(), R.drawable.gai_lun, null);
        dst = BitmapFactory.decodeResource(getResources(), R.drawable.shader_round_rect, null);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.argb(255, 150, 150, 150));
        canvas.saveLayer(0, 0, 9999, 9999, null);
        Paint paint = new Paint();
        canvas.drawBitmap(dst, new Rect(0, 0, dst.getWidth(), dst.getHeight()), new Rect(100, 100, 600, 500), paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(src, new Rect(0, 0, src.getWidth(), src.getHeight()), new Rect(100, 100, 600, 500), paint);
    }
}

