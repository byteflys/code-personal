package com.easing.commons.android.ui.control.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.easing.commons.android.value.color.Colors;
import com.easing.commons.android.view.Views;

public class AspectImageView extends AppCompatImageView {

    public AspectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {

    }

    //高度不变，自动按比例调整宽度
    public void autoAdaptWidth() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        int h = getMaxHeight();
        int w = h * bitmap.getWidth() / bitmap.getHeight();
        Views.size(this, w, h);
        setScaleType(ScaleType.FIT_XY);
    }

    //宽度不变，自动按比例调整高度
    public void autoAdaptHeight() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        int w = getMeasuredWidth();
        int h = w * bitmap.getHeight() / bitmap.getWidth();
        Views.size(this, w, h);
        setScaleType(ScaleType.FIT_XY);
    }

}
