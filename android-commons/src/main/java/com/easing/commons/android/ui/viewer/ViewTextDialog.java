package com.easing.commons.android.ui.viewer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.easing.commons.android.R;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.helper.callback.Action;
import com.easing.commons.android.io.Files;
import com.easing.commons.android.io.Streams;
import com.easing.commons.android.manager.Device;
import com.easing.commons.android.value.measure.Size;
import com.easing.commons.android.view.Views;

//文本预览弹窗
public class ViewTextDialog extends DialogFragment {

    AppCompatActivity ctx;

    String url = null;

    View root;

    //静态创建方法
    public static ViewTextDialog create() {
        ViewTextDialog dialog = new ViewTextDialog();
        dialog.setCancelable(false);
        return dialog;
    }

    //静态创建方法
    public static ViewTextDialog create(String url) {
        ViewTextDialog dialog = new ViewTextDialog();
        dialog.setCancelable(true);
        dialog.url = url;
        return dialog;
    }

    //设置文本路径
    public ViewTextDialog url(String url) {
        this.url = url;
        return this;
    }

    //显示
    public void show(AppCompatActivity ctx) {
        this.ctx = ctx;
        FragmentManager manager = ctx.getSupportFragmentManager();
        super.show(manager, Texts.random());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        root = getActivity().getLayoutInflater().inflate(R.layout.dialog_view_text, null);
        builder.setView(root);
        AlertDialog dialog = builder.create();
        //解析控件
        TextView textView = root.findViewById(R.id.text);
        String text = "";
        if (Files.length(url) > 1024 * 1024)
            text = "文件太大，无法预览";
        else
            text = Streams.stringFromFile(url);
        Views.text(textView, text);
        View okButton = root.findViewById(R.id.bt_ok);
        Views.onClick(okButton, () -> {
            dismiss();
        });
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Size screenSize = Device.getScreenSize(ctx);
        int w = (int) (screenSize.w * 0.9);
        int h = (int) (screenSize.h * 0.9);
        getDialog().getWindow().setLayout(w, h);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.color_white);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) root.getLayoutParams();
        layoutParams.width = w;
        layoutParams.height = h;
        layoutParams.gravity = Gravity.CENTER;
        root.setLayoutParams(layoutParams);
    }


    Action onClose;

    public ViewTextDialog onClose(Action onClose) {
        this.onClose = onClose;
        return this;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onClose != null)
            onClose.runAndPostException();
    }
}

