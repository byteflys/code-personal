package com.easing.commons.android.ui.control.selection_layout;

import android.view.View;

import androidx.annotation.IdRes;

import java.util.List;

public interface ISelectionLayout {

    List<View> children();

    ISelectionLayout selectView(View selectedView);

    ISelectionLayout selectViewById(Integer viewId);

    ISelectionLayout onViewSelectedListener(OnViewSelectedListener listener);

    default <T extends View> T findView(@IdRes int id) {
        View parent = (View) this;
        return parent.findViewById(id);
    }

    interface OnViewSelectedListener<T extends View> {

        void onViewSelected(T selectedView);
    }
}
