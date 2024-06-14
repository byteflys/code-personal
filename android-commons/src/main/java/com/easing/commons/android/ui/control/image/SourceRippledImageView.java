package com.easing.commons.android.ui.control.image;

import android.content.Context;
import android.util.AttributeSet;

@SuppressWarnings("all")
public class SourceRippledImageView extends RippledImageView {

    public SourceRippledImageView(Context context) {
        this(context, null);
    }

    public SourceRippledImageView(Context context, AttributeSet attrSet) {
        super(context, attrSet);
    }

    @Override
    protected void init(Context context, AttributeSet attributeSet) {
        srcRippled = true;
        backgroundRippled = false;
        super.init(context, attributeSet);
    }
}
