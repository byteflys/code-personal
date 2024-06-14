package com.easing.commons.android.ui.control.image;

import android.content.Context;
import android.util.AttributeSet;

@SuppressWarnings("all")
public class BackgroundRippledImageView extends RippledImageView {

    public BackgroundRippledImageView(Context context) {
        this(context, null);
    }

    public BackgroundRippledImageView(Context context, AttributeSet attrSet) {
        super(context, attrSet);
        srcRippled = false;
        backgroundRippled = true;
    }

}
