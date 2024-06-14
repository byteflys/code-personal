package com.easing.commons.android.ui.control.MultiFilePreviewer;

import android.widget.FrameLayout;

import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.ui.control.attach_viewer.Attach;

public interface UrlFilePreviewCallback {

    default String getTitle(Attach file) {
        return null;
    }

    default void addItem(CommonActivity activity, Attach file, FrameLayout frameLayout) {
    }

    default void selectItem(CommonActivity activity, Attach file, FrameLayout frameLayout) {
    }

    default void disposeItem(CommonActivity activity, Attach file, FrameLayout frameLayout) {
    }
}

