package com.easing.commons.android.ui_component.favorite_button;

import android.content.Context;
import android.util.AttributeSet;

import com.easing.commons.android.ui.control.image.RippledImageView;

public class FavoritesButton extends RippledImageView {

    FavoritesInfo info;

    public FavoritesButton(Context context) {
        this(context, null);
    }

    public FavoritesButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        srcRippled = false;
    }
}
