package com.easing.commons.android.ui.form.item;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.easing.commons.android.ui.form.FormCanvas;
import com.easing.commons.android.view.Views;

public class FormLinearLayout extends LinearLayout {

    int[] itemLocation;
    int[] parentLocation;

    public FormLinearLayout(Context context) {
        this(context, null);
    }

    public FormLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        //计算item和parent位置
        getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (!(getParent() instanceof GridLayout))
                return;
            itemLocation = Views.location(this);
            parentLocation = Views.location((GridLayout) getParent());

            new Canvas();
        });
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        FormCanvas.drawBorder(this, canvas, itemLocation, parentLocation);
    }

}
