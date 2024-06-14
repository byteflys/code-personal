package com.easing.commons.android.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.easing.commons.android.R;
import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.manager.Device;
import com.easing.commons.android.value.color.Colors;
import com.easing.commons.android.view.Views;

@SuppressWarnings("all")
public class OptionDialogWithTitle<T> extends DialogFragment {

    private CommonActivity ctx;
    private View root;
    private String message;
    private T[] options;
    private OptionDialog.NameTranslator<T> nameTranslator;
    private OptionDialog.OnSelectListener<T> listener;

    private int textWidth;

    //静态创建方法
    public static <T> OptionDialogWithTitle<T> create(CommonActivity ctx, String message, T[] options, OptionDialog.OnSelectListener<T> listener) {
        OptionDialogWithTitle dialog = new OptionDialogWithTitle();
        dialog.setCancelable(false);
        dialog.ctx = ctx;
        dialog.message = message;
        dialog.options = options;
        dialog.listener = listener;
        dialog.root = Views.inflate(ctx, R.layout.layout_option_dialog_with_title);
        return dialog;
    }

    //静态创建方法
    public static <T> OptionDialogWithTitle<T> create(CommonActivity ctx, String message, T[] options, OptionDialog.NameTranslator<T> nameTranslator, OptionDialog.OnSelectListener<T> listener) {
        OptionDialogWithTitle dialog = OptionDialogWithTitle.create(ctx, message, options, listener);
        dialog.nameTranslator = nameTranslator;
        return dialog;
    }

    public OptionDialogWithTitle message(Object message) {
        this.message = String.valueOf(message);
        return this;
    }

    //显示
    public OptionDialogWithTitle<T> cancelable(boolean cancelable) {
        setCancelable(cancelable);
        return this;
    }

    //显示
    public void show() {
        FragmentManager manager = ctx.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(this, Texts.random(false, false));
        transaction.commitAllowingStateLoss();
    }

    //隐藏
    public void dispose() {
        dismissAllowingStateLoss();
        ctx = null;
        root = null;
        message = null;
        options = null;
        listener = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        TextView messageText = root.findViewById(R.id.text_msg);
        messageText.setText(message);
        LinearLayout container = root.findViewById(R.id.layout_items);
        for (T option : options) {
            //添加选项文本
            TextView tv = Views.inflate(ctx, R.layout.item_option_dialog);
            float length = tv.getPaint().measureText(String.valueOf(option));
            textWidth = Math.max(textWidth, (int) length);
            tv.setText(nameTranslator == null ? String.valueOf(option) : nameTranslator.name(option));
            tv.setTag(option);
            container.addView(tv);
            //添加分割线
            View split = new View(ctx);
            container.addView(split);
        }
        for (int i = 0; i < container.getChildCount(); i++) {
            if (i % 2 != 0) {
                View v = container.getChildAt(i);
                v.setLayoutParams(new LinearLayout.LayoutParams(Views.MATCH_PARENT, 1, 0));
                if (i != container.getChildCount() - 1)
                    v.setBackgroundColor(Colors.LIGHT_GREY);
                continue;
            }
            TextView tv = (TextView) container.getChildAt(i);
            tv.setLayoutParams(new LinearLayout.LayoutParams(Views.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
            tv.setOnClickListener(v -> {
                int index = (container.indexOfChild(v) + 1) / 2;
                if (listener != null)
                    listener.onSelect(tv, (T) v.getTag(), index);
                dispose();
            });
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setView(root);
        AlertDialog dialog = builder.create();
        return dialog;
    }


    @Override
    public void onStart() {
        super.onStart();
        int screenWidth = Device.getScreenSize(ctx).w;
        getDialog().getWindow().setLayout((int) (screenWidth * 0.9), Views.WRAP_CONTENT);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) root.getLayoutParams();
        layoutParams.width = Views.MATCH_PARENT;
        layoutParams.height = Views.WRAP_CONTENT;
        root.setLayoutParams(layoutParams);
    }

    //解析子控件
    public <T> T findView(int viewId) {
        return (T) root.findViewById(viewId);
    }

    //选择监听器
    public interface OnSelectListener<T> {
        void onSelect(TextView itemView, T option, int index);
    }

    //名称转换器
    public interface NameTranslator<T> {
        String name(T option);
    }
}
