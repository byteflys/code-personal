package com.easing.commons.android.ui.wrapper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.easing.commons.android.R;
import com.easing.commons.android.R2;
import com.easing.commons.android.view.InputType;
import com.easing.commons.android.view.Views;

import butterknife.BindView;

@SuppressWarnings("all")
public class TitleContentEdit extends FrameLayout {

    protected ViewHolder holder = new ViewHolder();

    protected Views.OnClick onTitleClick;
    protected Views.OnClick onButtonClick;

    public TitleContentEdit(Context context) {
        this(context, null);
    }

    public TitleContentEdit(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    protected void init(Context context, AttributeSet attributeSet) {
        View root = Views.inflate(context, R.layout.layout_title_content_hint);
        addView(root);
        Views.viewBinding(holder, root);

        //读取属性
        TypedArray attrs = context.obtainStyledAttributes(attributeSet, R.styleable.TitleContentEdit);
        Boolean editable = attrs.getBoolean(R.styleable.TitleContentEdit_editable, false);
        CharSequence title = attrs.getText(R.styleable.TitleContentEdit_title);
        CharSequence content = attrs.getText(R.styleable.TitleContentEdit_content);
        CharSequence hint = attrs.getText(R.styleable.TitleContentEdit_hint);
        Drawable background = attrs.getDrawable(R.styleable.TitleContentEdit_background);
        Boolean underline = attrs.getBoolean(R.styleable.TitleContentEdit_underline, true);

        //设置属性
        if (title != null)
            holder.titleText.setText(title);
        if (content != null)
            holder.contentText.setText(content);
        if (hint != null)
            holder.contentText.setHint(hint);
        if (background != null)
            holder.root.setBackground(background);
        holder.contentText.setEnabled(editable);
        Views.visibility(holder.underline, underline ? Views.VISIBLE : Views.GONE);
    }

    public TitleContentEdit titleText(String titleText) {
        Views.text(holder.titleText, titleText);
        return this;
    }

    public TitleContentEdit contentText(String contentText) {
        Views.text(holder.contentText, contentText);
        return this;
    }

    public String contentText() {
        return Views.text(holder.contentText);
    }

    public TitleContentEdit editable(boolean editable) {
        holder.contentText.setEnabled(editable);
        return this;
    }

    public TitleContentEdit inputType(InputType inputType) {
        Views.inputType(holder.contentText, inputType);
        return this;
    }

    public TitleContentEdit inputLimit(String expression) {
        Views.inputLimit(holder.contentText, expression);
        return this;
    }

    public static class ViewHolder {

        @BindView(R2.id.root)
        View root;
        @BindView(R2.id.text_title)
        TextView titleText;
        @BindView(R2.id.text_content)
        EditText contentText;
        @BindView(R2.id.underline)
        View underline;
    }
}
