package com.easing.commons.android.ui_component.module_button;

import android.content.Context;
import android.util.AttributeSet;

import com.easing.commons.android.ui.control.image.RippledImageView;

public class ModuleButton extends RippledImageView {

    ModuleInfo info;

    public ModuleButton(Context context) {
        this(context, null);
    }

    public ModuleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        srcRippled = false;
    }
}
