package com.easing.commons.android.ui.control.image;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;

import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.AppCompatImageView;

import com.easing.commons.android.R;
import com.easing.commons.android.data.Data;
import com.easing.commons.android.time.Times;
import com.easing.commons.android.value.color.Colors;

@SuppressWarnings("all")
public class RippledImageView extends AppCompatImageView {

    //前景波纹化
    protected boolean srcRippled = true;
    //背景波纹化
    protected boolean backgroundRippled = true;

    //限制频繁操作
    protected boolean limitFrequence = false;
    //频繁操作间隔
    protected long frequenceInterval = 1000L;
    //是否可以点击
    protected boolean clickable = true;

    //波纹颜色
    protected int rippleColor = Colors.BLACK_10;

    //上次操作时间
    protected final Data<Long> lastClickTime = Data.create(0L);

    public RippledImageView(Context context) {
        this(context, null);
    }

    public RippledImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    //初始化
    protected void init(Context context, AttributeSet attributeSet) {
        TypedArray attrs = context.obtainStyledAttributes(attributeSet, R.styleable.RippleImageView);
        rippleColor = attrs.getColor(R.styleable.RippleImageView_rippleColor, Colors.BLACK_05);
        limitFrequence = attrs.getBoolean(R.styleable.RippleImageView_limitFrequence, false);
        frequenceInterval = attrs.getInteger(R.styleable.RippleImageView_frequenceInterval, 1000);
        clickable = attrs.getBoolean(R.styleable.RippleImageView_clickable, true);
        Boolean selected = attrs.getBoolean(R.styleable.RippleImageView_selected, false);
        setSelected(selected);
        setClickable(clickable);
        setImageDrawable(getDrawable());
        setBackground(getBackground());
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        Drawable drawable = getResources().getDrawable(resId, null);
        setImageDrawable(drawable);
    }

    @Override
    public void setBackgroundResource(@DrawableRes int resId) {
        Drawable drawable = getResources().getDrawable(resId, null);
        setBackground(drawable);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        boolean rippled = drawable instanceof RippleDrawable;
        if (srcRippled && !rippled) {
            RippleDrawable rippleDrawable = new RippleDrawable(ColorStateList.valueOf(rippleColor), drawable, null);
            super.setImageDrawable(rippleDrawable);
        } else super.setImageDrawable(drawable);
    }

    @Override
    public void setBackground(Drawable drawable) {
        boolean rippled = drawable instanceof RippleDrawable;
        if (backgroundRippled && !rippled) {
            RippleDrawable rippleDrawable = new RippleDrawable(ColorStateList.valueOf(rippleColor), drawable, null);
            super.setBackground(rippleDrawable);
        } else super.setBackground(drawable);
    }

    //选中，或取消选中，返回最终状态
    public boolean toggleSelection() {
        boolean selected = isSelected();
        setSelected(!selected);
        return !selected;
    }

    //限制频繁点击
    @Override
    synchronized public void setOnClickListener(OnClickListener listener) {

        //执行点击操作，并限制频率
        super.setOnClickListener(v -> {
            long time = Times.millisOfNow();
            long last = lastClickTime.data;
            if (limitFrequence && time - last < frequenceInterval && listener != null) {
//                TipBox.tipInCenter("操作频繁");
            } else {
                lastClickTime.set(time);
                if (listener != null)
                    listener.onClick(this);
            }
        });
    }
}
