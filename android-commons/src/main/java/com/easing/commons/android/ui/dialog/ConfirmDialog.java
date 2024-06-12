package com.easing.commons.android.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;

import android.view.View;
import android.widget.TextView;

import com.easing.commons.android.R;
import com.easing.commons.android.helper.callback.Action;
import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.thread.MainThread;
import com.easing.commons.android.ui.control.button.ImageButtonM2;

//确认框
public class ConfirmDialog extends DialogFragment {

    private String message = "";

    private CommonActivity ctx;

    private TextView messageText;

    private Action okAction;
    private Action noAction;

    //静态创建方法
    public static ConfirmDialog create() {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setCancelable(false);
        return dialog;
    }

    //静态创建方法
    public static ConfirmDialog create(Object msg) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setCancelable(false);
        dialog.message = msg.toString();
        return dialog;
    }

    //设置消息
    public ConfirmDialog message(Object msg) {
        message = msg.toString();
        return this;
    }

    //显示
    public ConfirmDialog show(CommonActivity ctx) {
        this.ctx = ctx;
        FragmentManager manager = ctx.getSupportFragmentManager();
        MainThread.postLater(() -> {
            super.show(manager, "ConfirmDialog");
        }, 200);
        return this;
    }

    //关闭
    public ConfirmDialog close() {
        dismissAllowingStateLoss();
        return this;
    }

    public ConfirmDialog okAction(Action okAction) {
        this.okAction = okAction;
        return this;
    }

    public ConfirmDialog noAction(Action noAction) {
        this.noAction = noAction;
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        View root = getActivity().getLayoutInflater().inflate(R.layout.layout_confirm_dialog, null);
        builder.setView(root);
        AlertDialog dialog = builder.create();
        //解析控件
        messageText = root.findViewById(R.id.text_msg);
        ImageButtonM2 okButton = root.findViewById(R.id.bt_ok);
        ImageButtonM2 noButton = root.findViewById(R.id.bt_no);
        messageText.setText(message);
        okButton.setOnClickListener(v -> {
            if (okAction != null)
                okAction.runAndPostException();
            dismissAllowingStateLoss();
        });
        noButton.setOnClickListener(v -> {
            if (noAction != null)
                noAction.runAndPostException();
            dismissAllowingStateLoss();
        });
        return dialog;
    }
}
