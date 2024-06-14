package com.easing.commons.android.ui.listener;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.easing.commons.android.ui.dialog.TipBox;

import java.util.function.IntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//文本输入限制器
@SuppressWarnings("all")
public class TextInputLimiter implements TextWatcher {

    EditText edit;

    String oldText = "";

    String characterTip = "不支持的字符";

    String countTip = "超出最大文字数量";

    String regex = ".*";

    Integer maxCount = 9999;

    IntFunction<Void> characterCountCallback;

    public TextInputLimiter edit(EditText edit) {
        this.edit = edit;
        edit.addTextChangedListener(this);
        return this;
    }

    public TextInputLimiter tip(String characterTip, String countTip) {
        this.characterTip = characterTip;
        this.countTip = countTip;
        return this;
    }

    public TextInputLimiter regex(String regex) {
        this.regex = regex;
        return this;
    }

    public TextInputLimiter maxCount(Integer maxCount) {
        this.maxCount = maxCount;
        return this;
    }

    public TextInputLimiter characterCountCallback(IntFunction<Void> characterCountCallback) {
        this.characterCountCallback = characterCountCallback;
        return this;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String newText = s.toString();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(newText);
        boolean match = matcher.matches();
        boolean exceed = newText.length() > maxCount;
        if (exceed) {
            edit.setText(oldText);
            edit.setSelection(edit.getText().length());
            TipBox.tipInCenter(countTip);
        } else if (!match) {
            edit.setText(oldText);
            edit.setSelection(edit.getText().length());
            TipBox.tipInCenter(characterTip);
        } else {
            if (characterCountCallback != null)
                characterCountCallback.apply(s.toString().length());
            oldText = newText;
        }
    }
}

