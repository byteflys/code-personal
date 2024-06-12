package com.easing.commons.android.drawable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;

import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.code.Console;
import com.easing.commons.android.manager.Dimens;
import com.easing.commons.android.ui.optimize.CanvasPainter;
import com.easing.commons.android.value.color.Colors;

import java.util.LinkedHashMap;
import java.util.Map;

//文字图标合成Drawable
@SuppressWarnings("all")
public class TextIconDrawable extends Drawable {

    String titleText;
    Drawable iconDrawable;

    Paint titlePaint = new Paint();

    Integer textSize;
    Integer textColor;
    Integer textStrokeWidth;
    Integer drawableWidth;
    Integer drawableHeight;
    Integer textWidth;
    Integer textHeight;
    Integer textDrawablePadding;
    Integer totalWidth;
    Integer totalHeight;

    Rect displayBound;

    public static final Map<Integer, Drawable> drawableCache = new LinkedHashMap();
    public static final Map<Integer, Bitmap> bitmapCache = new LinkedHashMap();

    public TextIconDrawable(@DrawableRes int drawableId, String titleText) {
        this.iconDrawable = CommonApplication.ctx.getResources().getDrawable(drawableId);
        this.titleText = titleText;
    }

    public TextIconDrawable style(Integer textSizeDp, Integer textColor, Integer textStrokeWidthDp, Integer drawableWidthDp, Integer drawableHeightDp, Integer textDrawablePaddingDp) {
        if (textSizeDp == null)
            textSizeDp = 10;
        if (textColor == null)
            textColor = Colors.RED;
        if (textStrokeWidthDp == null)
            textStrokeWidthDp = 0;
        if (drawableWidthDp == null)
            drawableWidthDp = 32;
        if (drawableHeightDp == null)
            drawableHeightDp = 32;
        if (textDrawablePaddingDp == null)
            textDrawablePaddingDp = 5;
        this.textSize = Dimens.toPx(textSizeDp);
        this.textColor = textColor;
        this.textStrokeWidth = Dimens.toPx(textStrokeWidthDp);
        this.drawableWidth = Dimens.toPx(drawableWidthDp);
        this.drawableHeight = Dimens.toPx(drawableHeightDp);
        this.textDrawablePadding = Dimens.toPx(textDrawablePaddingDp);
        //设置画笔样式
        EmbossMaskFilter emboss = new EmbossMaskFilter(new float[]{1, 1, 1}, 0.4F, 6, 3.5F);
        titlePaint.setAntiAlias(true);
        titlePaint.setColor(textColor);
        titlePaint.setTextSize(textSize);
        titlePaint.setStyle(Paint.Style.FILL);
        titlePaint.setStrokeWidth(textStrokeWidth);
        titlePaint.setMaskFilter(emboss);
        //计算文本大小
        textWidth = (int) titlePaint.measureText(titleText);
        textHeight = textSize;
        //计算合成后的总大小
        totalWidth = Math.max(textWidth, drawableWidth);
        totalHeight = textHeight + textDrawablePadding + drawableHeight;
        displayBound = new Rect(0, 0, totalWidth, totalHeight);
        return this;
    }

    @Override
    public void draw(Canvas canvas) {
        //画图标
        canvas.save();
        canvas.translate((totalWidth - drawableWidth) / 2 + displayBound.left, textHeight + textDrawablePadding + displayBound.top);
        canvas.scale((float) drawableWidth / (float) iconDrawable.getIntrinsicWidth(), (float) drawableHeight / (float) iconDrawable.getIntrinsicHeight());
        iconDrawable.setBounds(0, 0, iconDrawable.getIntrinsicWidth(), iconDrawable.getIntrinsicHeight());
        iconDrawable.draw(canvas);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) iconDrawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();
        Console.info("BitmapHashCode", bitmap.hashCode());
        canvas.restore();
        //画标题
        CanvasPainter.drawTextFromTopCenter(canvas, titlePaint, titleText, totalWidth / 2 + displayBound.left, 0 + displayBound.top);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return totalWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return totalHeight;
    }

    @Override
    public void setAlpha(int alpha) {
        titlePaint.setAlpha(alpha);
        iconDrawable.setAlpha(alpha);
    }

    @Override
    public void setBounds(Rect displayBound) {
        this.displayBound = displayBound;
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        throw new RuntimeException("Unsupported Operation");
    }
}

