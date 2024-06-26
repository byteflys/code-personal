package com.easing.commons.android.ui.control.selector;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.struct.ListArrayConvertor;
import com.easing.commons.android.ui.dialog.OptionDialog;
import com.easing.commons.android.ui.dialog.TipBox;

import java.util.List;

import androidx.appcompat.widget.AppCompatTextView;

//弹窗式选项框
public class OptionSelector<T> extends AppCompatTextView {

    private T[] options;
    private OnOptionSelect<T> listener;
    private NameTranslator<T> nameTranslator;

    private T selectedOption = null;
    private int selectedIndex = -1;

    private int lastIndex = -1;
    private T lastOption = null;
    private String lastText = null;

    public boolean isEnabled = true; //是否可编辑

    public OptionSelector(Context context, AttributeSet attrSet) {
        super(context, attrSet);
        init(context, attrSet);
    }

    private void init(Context context, AttributeSet attrSet) {
        setOnClickListener(v -> {
            if (options == null || options.length == 0) {
                TipBox.tipInCenter("无可用选项");
                return;
            }
            if (!isEnabled) //不可编辑
                return;

            OptionDialog.create((CommonActivity) context, options, option -> {
                return nameTranslator == null ? option.toString() : nameTranslator.name(option);
            }, (itemView, option, index) -> {
                lastIndex = selectedIndex;
                lastOption = selectedOption;
                lastText = getText().toString();
                selectedOption = option;
                selectedIndex = index;
                showSelectedOption();
                if (listener != null)
                    listener.onOptionSelect(itemView, option, index);
            }).show();
        });
    }

    //显示选中项
    private OptionSelector<T> showSelectedOption() {
        if (selectedOption == null)
            clearSelection();
        setText(nameTranslator == null ? selectedOption.toString() : nameTranslator.name(selectedOption));
        return this;
    }

    //取消选中
    public OptionSelector<T> clearSelection() {
        selectedIndex = -1;
        selectedOption = null;
        setText(null);
        return this;
    }

    public OptionSelector<T> select(int index) {

        //无数据
        if (index < 0 || index >= options.length)
            return clearSelection();

        //从初始选项中选中
        this.selectedIndex = index;
        this.selectedOption = options[index];
        showSelectedOption();
        return this;
    }

    public OptionSelector<T> select(T option) {

        //无数据
        if (option == null) {
            clearSelection();
            return this;
        }

        //从初始选项中选中
        for (int i = 0; i < options.length; i++)
            if (option.equals(options[i])) {
                this.selectedIndex = i;
                this.selectedOption = option;
                showSelectedOption();
                return this;
            }

        //无此选项，则取消选中
        clearSelection();
        return this;
    }

    //选中，并执行回调
    public OptionSelector<T> performSelection(int index) {
        select(index);
        if (listener != null)
            if (options != null)
                if (index >= 0 && index < options.length)
                    listener.onOptionSelect(null, options[index], index);
        return this;
    }

    //选中，并执行回调
    public OptionSelector<T> performSelection(T option) {
        select(option);
        if (listener != null)
            if (options != null)
                for (int index = 0; index < options.length; index++)
                    if (option == options[index])
                        listener.onOptionSelect(null, options[index], index);
        return this;
    }

    public int selectedIndex() {
        return selectedIndex;
    }

    public T selectedOption() {
        return selectedOption;
    }

    public boolean empty() {
        return selectedIndex == -1;
    }

    public T[] options() {
        return options;
    }

    public OptionSelector<T> options(T[] options) {
        this.options = options;
        return this;
    }

    public OptionSelector<T> options(List<T> options, ListArrayConvertor<T> convert) {
        this.options = convert.toArray(options);
        return this;
    }

    public OptionSelector<T> nameTranslator(NameTranslator<T> nameTranslator) {
        this.nameTranslator = nameTranslator;
        return this;
    }

    public OptionSelector<T> onOptionSelect(OnOptionSelect<T> listener) {
        this.listener = listener;
        return this;
    }

    public void restoreLastSelection() {
        selectedIndex = lastIndex;
        selectedOption = lastOption;
        setText(lastText);
    }

    public interface OnOptionSelect<T> {
        void onOptionSelect(TextView itemView, T option, int index);
    }

    public interface NameTranslator<T> {
        String name(T option);
    }


}
