package com.easing.commons.android.ui.optimize;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.TextPaint;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.ColorRes;

import com.easing.commons.android.helper.exception.BizException;
import com.easing.commons.android.value.color.Colors;

@SuppressWarnings("all")
public class CanvasPainter {

    //获取文字宽度
    public static float getTextWidth(Paint paint, String text) {
        float width = paint.measureText(text);
        return width;
    }

    //获取文字高度
    public static float getTextHeight(Paint paint) {
        float height = (paint.getTextSize() + 0.00000007F) / 0.7535F;
        return height;
    }

    //获取文字水平中心位置
    public static float getTextCenterX(TextView textView, String text) {
        TextPaint paint = textView.getPaint();
        float textWidth = getTextWidth(paint, text);
        int gravity = textView.getGravity();
        if ((gravity & Gravity.LEFT) == Gravity.LEFT)
            return textView.getPaddingLeft() + textWidth / 2;
        if ((gravity & Gravity.RIGHT) == Gravity.RIGHT)
            return textView.getMeasuredWidth() - textView.getPaddingRight() - textWidth / 2;
        if ((gravity & Gravity.CENTER_HORIZONTAL) == Gravity.CENTER_HORIZONTAL)
            return textView.getPaddingLeft() + (textView.getMeasuredWidth() - textView.getPaddingLeft() - textView.getPaddingRight()) / 2;
        throw BizException.of("unknown horizontal alignment");
    }

    //获取文字竖直中心位置
    public static float getTextCenterY(TextView textView) {
        TextPaint paint = textView.getPaint();
        float textHeight = getTextHeight(paint);
        int gravity = textView.getGravity();
        if ((gravity & Gravity.TOP) == Gravity.TOP)
            return textView.getPaddingTop() + textHeight / 2;
        if ((gravity & Gravity.BOTTOM) == Gravity.BOTTOM)
            return textView.getMeasuredHeight() - textView.getPaddingBottom() - textHeight / 2;
        if ((gravity & Gravity.CENTER_VERTICAL) == Gravity.CENTER_VERTICAL)
            return textView.getPaddingTop() + (textView.getMeasuredHeight() - textView.getPaddingTop() - textView.getPaddingBottom()) / 2;
        throw BizException.of("unknown vertical alignment");
    }

    //从中间开始绘制文字
    public static void drawTextFromCenter(Canvas canvas, Paint paint, String text, Number centerX, Number centerY) {
        float textWidth = getTextWidth(paint, text);
        Paint.FontMetrics metrics = paint.getFontMetrics();
        canvas.drawText(text, 0, text.length(), centerX.floatValue() - textWidth / 2, centerY.floatValue() - (metrics.ascent + metrics.descent) / 2, paint);
    }

    //从左下角开始绘制文字
    public static void drawTextFromLeftBottom(Canvas canvas, Paint paint, String text, Number left, Number bottom) {
        float textHeight = getTextHeight(paint);
        Paint.FontMetrics metrics = paint.getFontMetrics();
        canvas.drawText(text, 0, text.length(), left.floatValue(), bottom.floatValue() - textHeight / 2 - (metrics.ascent + metrics.descent) / 2, paint);
    }

    //从左上角开始绘制文字
    public static void drawTextFromLeftTop(Canvas canvas, Paint paint, String text, Number left, Number top) {
        float textHeight = getTextHeight(paint);
        Paint.FontMetrics metrics = paint.getFontMetrics();
        canvas.drawText(text, 0, text.length(), left.floatValue(), top.floatValue() + textHeight / 2 - (metrics.ascent + metrics.descent) / 2, paint);
    }

    //从顶部中间开始绘制文字
    public static void drawTextFromTopCenter(Canvas canvas, Paint paint, String text, Number centerX, Number top) {
        float textWidth = getTextWidth(paint, text);
        float textHeight = getTextHeight(paint);
        Paint.FontMetrics metrics = paint.getFontMetrics();
        canvas.drawText(text, 0, text.length(), centerX.floatValue() - textWidth / 2, top.floatValue() + textHeight / 2 - (metrics.ascent + metrics.descent) / 2, paint);
    }

    //从底部中间开始绘制文字
    public static void drawTextFromBottomCenter(Canvas canvas, Paint paint, String text, Number centerX, Number bottom) {
        float textWidth = getTextWidth(paint, text);
        float textHeight = getTextHeight(paint);
        Paint.FontMetrics metrics = paint.getFontMetrics();
        canvas.drawText(text, 0, text.length(), centerX.floatValue() - textWidth / 2, bottom.floatValue() - textHeight / 2 - (metrics.ascent + metrics.descent) / 2, paint);
    }

    //画矩形色块
    public static void drawColorRect(Canvas canvas, Number left, Number top, Number right, Number bottom, @ColorRes int colorId) {
        canvas.save();
        canvas.clipRect(new Rect(left.intValue(), top.intValue(), right.intValue(), bottom.intValue()));
        canvas.drawColor(Colors.getColor(colorId));
        canvas.restore();
    }

    //TODO => 渐变色填充有问题
    //画矩形渐变色块
    public static void drawGradientRect(Canvas canvas, int left, int top, int right, int bottom, @ColorRes int startColorId, @ColorRes int endColorId) {
        Rect rect = new Rect(left, top, right, bottom);
        Paint paint = new Paint();
        int startColor = Colors.getColor(startColorId);
        int endColor = Colors.getColor(endColorId);
        paint.setShader(new LinearGradient(0, 0, 1, 1, new int[]{startColor, endColor}, null, Shader.TileMode.CLAMP));
        canvas.save();
        canvas.clipRect(rect);
        canvas.drawRect(rect, paint);
        canvas.restore();
    }

    //画直线
    public static void drawLine(Canvas canvas, Paint paint, Number x1, Number y1, Number x2, Number y2) {
        canvas.drawLine(x1.floatValue(), y1.floatValue(), x2.floatValue(), y2.floatValue(), paint);
    }

    //画矩形
    public static void drawRect(Canvas canvas, Paint paint, Number left, Number top, Number right, Number bottom) {
        canvas.drawRect(left.floatValue(), top.floatValue(), right.floatValue(), bottom.floatValue(), paint);
    }

    //画圆角矩形
    public static void drawRoundRect(Canvas canvas, Paint paint, Number left, Number top, Number right, Number bottom, Number rx, Number ry) {
        canvas.drawRoundRect(left.floatValue(), top.floatValue(), right.floatValue(), bottom.floatValue(), rx.floatValue(), ry.floatValue(), paint);
    }

    //画圆形
    public static void drawCircle(Canvas canvas, Paint paint, Number cx, Number cy, Number radius) {
        canvas.drawCircle(cx.floatValue(), cy.floatValue(), radius.floatValue(), paint);
    }

    //画三角形
    public static void drawTriAngle(Canvas canvas, Paint paint, Number left, Number right, Number top, Number bottom) {
        Path path = new Path();
        path.moveTo(left.floatValue(), top.floatValue());
        path.lineTo(right.floatValue(), top.floatValue());
        path.lineTo((left.floatValue() + right.floatValue()) / 2, bottom.floatValue());
        path.lineTo(left.floatValue(), top.floatValue());
        canvas.drawPath(path, paint);
    }

    //画Bitmap
    public static void drawBitmap(Bitmap bitmap, Canvas canvas, Paint paint, Number left, Number top, Number right, Number bottom) {
        Rect srcRect = rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Rect dstRect = rect(left, top, right, bottom);
        canvas.drawBitmap(bitmap, srcRect, dstRect, paint);
    }

    //创建Rect
    public static Rect rect(Number left, Number top, Number right, Number bottom) {
        return new Rect(left.intValue(), top.intValue(), right.intValue(), bottom.intValue());
    }

    //创建RectF
    public static RectF rectF(Number left, Number top, Number right, Number bottom) {
        return new RectF(left.intValue(), top.intValue(), right.intValue(), bottom.intValue());
    }

}
