package com.easing.commons.android.ui.wrapper;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class _ViewTemplate extends FrameLayout {

    public _ViewTemplate(Context context) {
        this(context, null);
    }

    public _ViewTemplate(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    protected void init(Context context, AttributeSet attributeSet) {

    }
}
