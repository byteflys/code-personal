package com.easing.commons.android.ui.wrapper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.easing.commons.android.R;
import com.easing.commons.android.R2;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.view.InputType;
import com.easing.commons.android.view.Views;

import butterknife.BindView;

@SuppressWarnings("all")
public class TitleContentButton extends FrameLayout {

    protected ViewHolder holder = new ViewHolder();

    protected Views.OnClick onTitleClick;
    protected Views.OnClick onButtonClick;

    protected boolean isShowPasswrod = false;

    public TitleContentButton(Context context) {
        this(context, null);
    }

    public TitleContentButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    protected void init(Context context, AttributeSet attributeSet) {
        View root = Views.inflate(context, R.layout.layout_title_content_button);
        addView(root);
        Views.viewBinding(holder, root);

        //读取属性
        TypedArray attrs = context.obtainStyledAttributes(attributeSet, R.styleable.TitleContentButton);
        Boolean editable = attrs.getBoolean(R.styleable.TitleContentButton_editable, false);
        CharSequence title = attrs.getText(R.styleable.TitleContentButton_title);
        CharSequence content = attrs.getText(R.styleable.TitleContentButton_content);
        Drawable background = attrs.getDrawable(R.styleable.TitleContentButton_background);
        Boolean underline = attrs.getBoolean(R.styleable.TitleContentButton_underline, true);
        Boolean passwrodLine = attrs.getBoolean(R.styleable.TitleContentButton_passwordLine, false);

        //设置属性
        if (title != null)
            holder.titleText.setText(title);
        if (content != null)
            holder.contentText.setText(content);
        if (background != null)
            holder.root.setBackground(background);
        holder.contentText.setEnabled(editable);
        Views.visibility(holder.underline, underline ? Views.VISIBLE : Views.GONE);

        holder.show_password_button.setVisibility(passwrodLine ? VISIBLE : GONE);

        if (passwrodLine) {
            holder.show_password_button.setVisibility(VISIBLE);

            Texts.setEditTextMaxLength( holder.contentText,32);
            holder.contentText.setHint("输入密码");
            holder.show_password_button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    isShowPasswrod = !isShowPasswrod;
                    Views.isShowEditViewPassword(holder.contentText, isShowPasswrod);
                    holder.show_password_button.setImageResource(isShowPasswrod ? R.drawable.edit_password_show : R.drawable.edit_password_hind);

                }
            });

            Views.isShowEditViewPassword(holder.contentText, isShowPasswrod);

        } else {

            holder.show_password_button.setVisibility(GONE);
        }


    }

    public TitleContentButton titleText(String titleText) {
        Views.text(holder.titleText, titleText);
        return this;
    }

    public TitleContentButton contentText(String contentText) {
        Views.text(holder.contentText, contentText);
        return this;
    }

    public String contentText() {
        return Views.text(holder.contentText);
    }

    public TitleContentButton editable(boolean editable) {
        holder.contentText.setEnabled(editable);
        return this;
    }

    public TitleContentButton inputType(InputType inputType) {
        Views.inputType(holder.contentText, inputType);
        return this;
    }

    public TitleContentButton inputLimit(String expression) {
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

        @BindView(R2.id.show_password_button)
        ImageView show_password_button;
    }
}
