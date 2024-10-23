package com.easing.commons.android.view;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.easing.commons.android.helper.callback.SucceedCallBackListener;
import com.easing.commons.android.manager.Dimens;

/**
 * 多类型字体
 * <p>
 * SpanTextUtil.insertText(messageText, "那么：", "#666666"); //设置文字 和颜色
 * SpanTextUtil.insertEmpty(messageText, 5); //保留边距
 * SpanTextUtil.insertImage(messageText, drawable, 3); 图片
 */

public class SpanTextUtil {

    /**
     * 文字后面加图片
     *
     * @param drawable  图片
     * @param leftSpace 边距
     */
    public static void insertImage(TextView textView, Drawable drawable, int leftSpace) {

        if (textView == null || drawable == null)
            return;
        SpannableString ss = new SpannableString("a");

        int leftPx = Dimens.toPx(leftSpace);
        drawable.getBounds().left += leftPx;
        drawable.getBounds().right += leftPx;

        //居中对齐imageSpan
        CenterAlignImageSpan imageSpan = new CenterAlignImageSpan(drawable);
        //ImageSpan imageSpan = new ImageSpan(drawable);
        ss.setSpan(imageSpan, 0, "a".length(), ImageSpan.ALIGN_BASELINE);

        textView.append(ss);
    }

    /**
     * 文字后面的边距
     *

     * @param width 边距
     */
    public static void insertEmpty(TextView textView, int width) {
        Bitmap bitmap = null;
        SpannableString ss = new SpannableString("a");

        int pxWidth = Dimens.toPx(width);
        Drawable drawable = new BitmapDrawable(textView.getResources(), bitmap);
        drawable.setBounds(0, 0, pxWidth, 5);

        //居中对齐imageSpan
        CenterAlignImageSpan imageSpan = new CenterAlignImageSpan(drawable);
        ss.setSpan(imageSpan, 0, "a".length(), ImageSpan.ALIGN_BASELINE);

        textView.append(ss);
    }

    public static void insertText(TextView textView, String text, String color) {
        SpannableString ss = new SpannableString(text);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor(color)), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.append(ss);
    }

    /**
     * 带点击回调
     */
    public static SpannableString insertTextClick(TextView textView, String text, String color, SucceedCallBackListener<TextView> clickCallback) {
        SpannableString ss = new SpannableString(text);
        if (textView != null && textView.getMovementMethod() == null) {
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
        ss.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (clickCallback != null)
                    clickCallback.succeedCallBack(textView);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
                ds.setColor(Color.parseColor(color));//
            }


        }, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.append(ss);
        return ss;
    }

    public static void insertText(TextView textView, SpannableString ss) {
        textView.append(ss);
    }

}
