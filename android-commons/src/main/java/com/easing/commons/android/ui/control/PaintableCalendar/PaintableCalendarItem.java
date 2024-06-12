package com.easing.commons.android.ui.control.PaintableCalendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatTextView;

import com.easing.commons.android.value.time.YMD;

public class PaintableCalendarItem extends AppCompatTextView {

    public boolean isTitle = false;
    public boolean isEnabled = false;
    public boolean isSelected = false;

    public int w;
    public int h;

    public YMD ymd = YMD.create(2020, 1, 1);

    public PaintableCalendarItem(Context context) {
        this(context, null);
    }

    public PaintableCalendarItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    protected void init(Context context, AttributeSet attributeSet) {
        setGravity(Gravity.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        w = getMeasuredWidth();
        h = getMeasuredHeight();
    }
}

