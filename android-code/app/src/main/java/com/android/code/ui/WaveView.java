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
import android.view.View;import com.android.code.R;

@SuppressWarnings("all")
public class WaveView extends View {

    //圆形遮罩
    Bitmap dst;
    //波浪图
    Bitmap src;

    //波浪起始位置
    float ratio1 = 0;
    float ratio2 = 0.15F;

    Paint paint = new Paint();

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        dst = BitmapFactory.decodeResource(getResources(), R.drawable.shader_circle, null);
        src = BitmapFactory.decodeResource(getResources(), R.drawable.wave, null);
        paint.setColor(Color.GRAY);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        ratio1 += 0.0010F;
        ratio2 = ratio1 + 0.10F;
        if (ratio2 >= 0.95F)
            ratio1 = 0.10F;
        ratio2 = ratio1 + 0.10F;
        //画背景圆
        canvas.drawCircle(350, 350, 250, paint);
        //画圆形遮罩
        canvas.saveLayer(0, 0, getWidth(), getHeight(), null);
        canvas.drawBitmap(dst, new Rect(0, 0, dst.getWidth(), dst.getHeight()), new Rect(100, 100, 600, 600), paint);
        //画波浪图的指定区域
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src, new Rect((int) (src.getWidth() * ratio1), 0, (int) (src.getWidth() * ratio2), src.getHeight()), new Rect(100, 100, 600, 600), paint);
        paint.setXfermode(null);
        postInvalidateDelayed(10);
    }
}

