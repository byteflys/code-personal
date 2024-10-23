package com.easing.commons.android.ui.control.test;

import android.view.View;

import com.easing.commons.android.annotation_processor.CallingStack;
import com.easing.commons.android.app.CommonFragment;
import com.easing.commons.android.code.Console;
import com.easing.commons.android.format.Maths;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.ui.dialog.TipBox;

public class TestFragment extends CommonFragment {

    Object value = Texts.stringify(Maths.randomInt(100, 999));

    public static TestFragment create(Object value) {
        TestFragment fragment = new TestFragment();
        fragment.value = value;
        return fragment;
    }

    @Override
    public View inflateView() {
        return new TestView(ctx).data(value);
    }

    @Override
    public void createView() {

    }

    @Override
    public void onSelected() {
        TipBox.tipInCenter(getClass().getSimpleName() + " Selected");
    }

    @Override
    public void onDeselected() {
        TipBox.tipInCenter(getClass().getSimpleName() + " Deselected");
    }
}
