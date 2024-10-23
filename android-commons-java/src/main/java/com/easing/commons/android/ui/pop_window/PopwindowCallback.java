package com.easing.commons.android.ui.pop_window;

import android.view.View;


public interface PopwindowCallback<T> {
    void onViewCallback(View view, T data);
}
