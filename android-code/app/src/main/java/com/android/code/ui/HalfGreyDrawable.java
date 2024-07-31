package com.android.code.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

//图像左侧变灰效果
@SuppressWarnings("all")
public class HalfGreyDrawable extends Drawable {

    final Drawable baseDrawable;
    final Bitmap baseBitmap;

    //绘制灰色部分
    final Paint paint1 = new Paint();
    //绘制正常部分
    final Paint paint2 = new Paint();

    public HalfGreyDrawable(Drawable baseDrawable) {
        this.baseDrawable = baseDrawable;
        this.baseBitmap = drawableToBitmap(baseDrawable);
        ColorMatrix matrix = new ColorMatrix(new float[]{
                0.333F, 0.333F, 0.333F, 0, 0,
                0.333F, 0.333F, 0.333F, 0, 0,
                0.333F, 0.333F, 0.333F, 0, 0,
                0, 0, 0, 1, 0
        });
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        paint1.setColorFilter(filter);
        paint1.setAlpha(255);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        if (w <= 0) w = 4;
        if (h <= 0) h = 4;
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    //这里共实现了三个效果
    //一个是图像的半侧变灰效果
    //一个是变灰的位置带有动画效果，从左往右，再从右往左，如此反复
    //一个是图像支持局部显示
    @Override
    public void draw(Canvas canvas) {
        int level = getLevel();
        Rect bounds = getBounds();
        int w = bounds.right - bounds.left;
        int h = bounds.bottom - bounds.top;
        //绘制灰色部分
        {
            Rect srcRect = new Rect(bounds.left, bounds.top, bounds.left + w * level / 100, bounds.bottom);
            Rect canvasBounds = canvas.getClipBounds();
            int canvasW = canvasBounds.right - canvasBounds.left;
            int canvasH = canvasBounds.bottom - canvasBounds.top;
            Rect dstRect = new Rect(canvasBounds.left, canvasBounds.top, canvasBounds.left + canvasW * level / 100, canvasBounds.bottom);
            canvas.drawBitmap(baseBitmap, srcRect, dstRect, paint1);
        }
        //绘制正常部分
        {
            Rect srcRect = new Rect(bounds.left + w * level / 100, bounds.top, bounds.right, bounds.bottom);
            Rect canvasBounds = canvas.getClipBounds();
            int canvasW = canvasBounds.right - canvasBounds.left;
            int canvasH = canvasBounds.bottom - canvasBounds.top;
            Rect dstRect = new Rect(canvasBounds.left + canvasW * level / 100, canvasBounds.top, canvasBounds.right, canvasBounds.bottom);
            canvas.drawBitmap(baseBitmap, srcRect, dstRect, paint2);
        }
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setAlpha(int alpha) {
        paint1.setAlpha(alpha);
    }

    //不允许设置自己的ColorFilter
    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        throw new RuntimeException("Unsupported Operation");
    }

    @Override
    public int getIntrinsicWidth() {
        return baseDrawable.getIntrinsicWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return baseDrawable.getIntrinsicHeight();
    }

    //当我们只想绘制Drawable的部分区域时
    //Bounds可用来指示Drawable的绘制范围
    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        baseDrawable.setBounds(bounds);
        invalidateSelf();
    }

    //对于有状态变化效果的Drawable
    //Level可用于指示动画进度
    //Level的范围为0-100，具体怎么用，是由用户自己来决定的
    @Override
    protected boolean onLevelChange(int level) {
        invalidateSelf();
        return true;
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        baseDrawable.setBounds(left, top, right, bottom);
    }
}

