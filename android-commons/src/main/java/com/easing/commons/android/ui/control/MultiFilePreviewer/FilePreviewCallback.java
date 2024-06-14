package com.easing.commons.android.ui.control.MultiFilePreviewer;

import android.widget.FrameLayout;

import com.easing.commons.android.app.CommonActivity;

public interface FilePreviewCallback {

    default String getTitle(String file) {
        return null;
    }

    default void addItem(CommonActivity activity, String file, FrameLayout frameLayout) {
    }

    default void selectItem(CommonActivity activity, String file, FrameLayout frameLayout) {
    }

    default void disposeItem(CommonActivity activity, String file, FrameLayout frameLayout) {
    }
}

