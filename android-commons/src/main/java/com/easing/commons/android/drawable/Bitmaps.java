package com.easing.commons.android.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.manager.Dimens;
import com.easing.commons.android.value.color.Colors;
import com.easing.commons.android.value.measure.Size;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

import lombok.SneakyThrows;

@SuppressWarnings("all")
public class Bitmaps {

    public static Bitmap decodeBitmapFromResource(Context context, int drawableId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return BitmapFactory.decodeResource(context.getResources(), drawableId);
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap decodeBitmapFromFile(String path) {
        return BitmapFactory.decodeFile(path);
    }

    public static Bitmap decodeBitmapFromStream(InputStream is) {
        return BitmapFactory.decodeStream(is);
    }

    public static void loadImageToView(String path, ImageView iv) {
        Bitmap bitmap = Bitmaps.decodeBitmapFromFile(path);
        iv.setImageBitmap(bitmap);
    }

    public static int byteSize(Bitmap bitmap) {
        return bitmap.getByteCount();
    }

    @SneakyThrows
    public static void writeBitmapToFile(Bitmap bitmap, String dst, int quality) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, bos);
        bitmap.recycle();
        FileOutputStream fos = new FileOutputStream(dst);
        fos.write(bos.toByteArray());
        fos.flush();
        fos.close();
        bos.close();
    }

    @SneakyThrows
    public static byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        bitmap.recycle();
        byte[] bytes = bos.toByteArray();
        bos.close();
        return bytes;
    }

    //压缩图片文件，并保存至指定位置
    public static void compressImageFile(String src, String dst, int quality) {
        Bitmap bitmap = Bitmaps.decodeBitmapFromFile(src);
        Bitmaps.writeBitmapToFile(bitmap, dst, quality);
    }

    public static Size tryBitmapSize(Context context, int drawableId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), drawableId, opt);
        return new Size(opt.outWidth, opt.outHeight);
    }

    //创建Bitmap
    public static Bitmap create(int w, int h) {
        try {
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            return bitmap;
        } catch (Throwable e) {
            return null;
        }
    }

    //保存Bitmap到本地
    @SneakyThrows
    public static void save(Bitmap bitmap, String file) {
        FileOutputStream os = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        os.flush();
        os.close();
    }

    //Drawable转Bitmap
    public static Bitmap drawableToBitmap(@DrawableRes int drawableId) {
        Drawable drawable = CommonApplication.ctx.getResources().getDrawable(drawableId, null);
        return drawableToBitmap(drawable);
    }

    //Drawable转Bitmap
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

    //创建一个空白的Bitmap
    public static Bitmap emptyBitmap() {
        return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
    }

    //创建Drawable
    public static Drawable getDrawable(Integer drawableID) {
        if (drawableID == null) return null;
        return CommonApplication.ctx.getResources().getDrawable(drawableID, null);
    }

    //创建一个空的Drawable
    public static Drawable emptyDrawable() {
        return new BitmapDrawable(CommonApplication.ctx.getResources(), emptyBitmap());
    }

    //创建TextIconDrawable
    public static Drawable getTextIconDrawable(Integer drawableID, String text) {
        return new TextIconDrawable(drawableID, text).style(null, null, null, null, null, null);
    }

    //创建TextIconDrawable
    public static Drawable getTextIconDrawable(Integer drawableID, String text, Integer textSizeDp, Integer textColor, Integer textStrokeWidthDp, Integer drawableWidthDp, Integer drawableHeightDp, Integer textDrawablePaddingDp) {
        return new TextIconDrawable(drawableID, text).style(textSizeDp, textColor, textStrokeWidthDp, drawableWidthDp, drawableHeightDp, textDrawablePaddingDp);
    }

    //创建TextDrawable
    public static Drawable getTextDrawable(Integer drawableID, String text) {
        return getTextDrawable(drawableID, text, null, null, null, null, null);
    }

    //创建TextDrawable
    public static Drawable getTextDrawable(Integer drawableID, String text, Integer textSizeDp, Integer textColor, Integer textStrokeWidth, Integer drawableWidth, Integer drawableHeight) {
        //计算文字和图片大小
        if (textSizeDp == null)
            textSizeDp = 24;
        if (textStrokeWidth == null)
            textStrokeWidth = 5;
        if (textColor == null)
            textColor = Colors.LIGHT_BLUE;
        Drawable drawable = drawableID == null ? emptyDrawable() : getDrawable(drawableID);
        if (drawableWidth == null)
            drawableWidth = drawable.getIntrinsicWidth();
        if (drawableHeight == null)
            drawableHeight = drawable.getIntrinsicHeight();
        final int textHeight = Dimens.toPx(textSizeDp);
        //计算文本长度
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(textColor);
        paint.setTextSize(textHeight);
        paint.setStyle(Paint.Style.FILL);
        int textWidth = (int) paint.measureText(text);
        //计算合成后图标的大小
        final int bitmapWidth = textWidth > drawableWidth ? textWidth : drawableWidth;
        final int paddingHeight = Dimens.toPx(4);
        final int bitmapHeight = textHeight + paddingHeight + drawableHeight;

        //创建位图
        Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        //画图片
        Bitmap srcBitmap = drawableToBitmap(drawable);
        canvas.drawBitmap(srcBitmap, new Rect(0, 0, srcBitmap.getWidth(), srcBitmap.getHeight()), new Rect(bitmapWidth / 2 - drawableWidth / 2, textHeight + paddingHeight, bitmapWidth / 2 + drawableWidth / 2, textHeight + paddingHeight + drawableHeight), paint);

        //画文字
        paint.setStrokeWidth(textStrokeWidth);
        EmbossMaskFilter emboss = new EmbossMaskFilter(new float[]{1, 1, 1}, 0.4f, 6, 3.5f);
        paint.setMaskFilter(emboss);
        canvas.drawText(text, bitmapWidth / 2f - textWidth / 2f, textHeight, paint);
        return new BitmapDrawable(CommonApplication.ctx.getResources(), bitmap);
    }

    //将Image写入存储卡
    @SneakyThrows
    public static void writeImage(Image image, String path) {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        FileOutputStream os = new FileOutputStream(path);
        os.write(bytes);
        image.close();
        os.close();
    }

    //将Image进行翻转后，再写入存储卡
    //由于Camera采集的数据都是横向的，想要保存成竖向图片，就需要逆时针旋转90度
    @SneakyThrows
    public static void writeReversedImage(Image image, String path, Size s) {
        //解出Image数据
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        //横竖翻转
        Bitmap originalBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        Bitmap reversedBitmap = Bitmap.createBitmap(s.w, s.w, originalBitmap.getConfig());
        Canvas canvas = new Canvas(reversedBitmap);
        canvas.rotate(90, s.w / 2, s.w / 2);
        canvas.drawBitmap(
                originalBitmap, new Rect(0, 0, s.w, s.h), new Rect(0, 0, s.w, s.h), new Paint()
        );
        //去除黑边
        //由于旋转后的图片大小是[w,w]，比[w,h]要大，两侧会有黑边
        Bitmap finalBitmap = Bitmap.createBitmap(s.h, s.w, originalBitmap.getConfig());
        Canvas finalCanvas = new Canvas(finalBitmap);
        finalCanvas.drawBitmap(
                reversedBitmap, new Rect(s.w - s.h, 0, s.w / 2 + s.h, s.w), new Rect(0, 0, s.h, s.w), new Paint()
        );
        //写入文件
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
        finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
        originalBitmap.recycle();
        reversedBitmap.recycle();
        finalBitmap.recycle();
        image.close();
    }
}

