package com.easing.commons.android.ui.form.item;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.GridLayout;

import com.easing.commons.android.ui.form.FormCanvas;
import com.easing.commons.android.ui.optimize.EasyEditText;
import com.easing.commons.android.view.Views;

public class FormInput extends EasyEditText {

    int[] itemLocation;
    int[] parentLocation;

    public FormInput(Context context) {
        this(context, null);
    }

    public FormInput(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected void init(Context context, AttributeSet attributeSet) {
        super.init(context, attributeSet);
        //计算item和parent位置
        getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (!(getParent() instanceof GridLayout))
                return;
            itemLocation = Views.location(this);
            parentLocation = Views.location((GridLayout) getParent());
        });
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        FormCanvas.drawBorder(this, canvas, itemLocation, parentLocation);
    }

}
