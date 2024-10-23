package com.easing.commons.android.ui.listener;

import android.text.Editable;
import android.text.TextWatcher;

//EditText文本监听
public class TextChangeListener implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        onChange();
    }

    public void onChange() {

    }
}

