package com.easing.commons.android.ui.control.edit;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import java.util.Arrays;
import java.util.List;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

//自动补全输入框
public class AutoCompleteEdit extends AppCompatAutoCompleteTextView {

    private int threshold = 0;

    private FilterableTextAdapter adapter;
    private boolean isEnabled = true;

    public AutoCompleteEdit(Context context) {
        this(context, null);
    }

    public AutoCompleteEdit(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    protected void init(Context context, AttributeSet attributeSet) {

        //下拉项适配器
        adapter = new FilterableTextAdapter();
        adapter.autoCompleteEdit = this;
        setAdapter(adapter);

        //文字改变时，更新建议项
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s);
            }
        });

        //获得焦点时，更新建议项
        setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && isEnabled)
                showDropDown();
        });

        //有焦点重新点击时，也显示建议项
        if (isEnabled)
            setOnClickListener(v -> {
                if (getText().toString().isEmpty())
                    showDropDown();
            });
    }

    @Override
    public boolean enoughToFilter() {
        return getText().length() >= threshold;
    }

    //设置字符匹配数量
    public AutoCompleteEdit threshold(int threshold) {
        this.threshold = threshold;
        return this;
    }

    //更新全部可选数据
    public AutoCompleteEdit reset(List<String> datas) {
        adapter.reset(datas);
        adapter.getFilter().filter(getText().toString());
        return this;
    }

    //更新全部可选数据
    public AutoCompleteEdit reset(String[] datas) {
        adapter.reset(Arrays.asList(datas));
        adapter.getFilter().filter(getText().toString());
        return this;
    }

    //下拉选项更新时，执行回调
    public AutoCompleteEdit onFilter(FilterableTextAdapter.OnFilter onFilter) {
        adapter.onFilter((items, count) -> {
            onFilter.onFilter(items, count);
            setDropDownHeight(600);
            if (isEnabled)
                showDropDown();
        });
        return this;
    }

}
