package com.easing.commons.android.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.easing.commons.android.R;
import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.helper.callback.Action;
import com.easing.commons.android.thread.Threads;
import com.easing.commons.android.thread.WorkThread;
import com.easing.commons.android.view.Views;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//消息框
@SuppressWarnings("all")
public class MessageDialogEx1 extends DialogFragment {

    Lock lock = new ReentrantLock();

    DialogState state = DialogState.CREATED;

    CommonActivity ctx;

    String bufferMessage = "";
    String message = "";

    String buttonText = "OK";

    Integer openTimes = 0;

    Action onClose;

    boolean showIcon = false;

    //静态创建方法
    public static MessageDialogEx1 create(CommonActivity ctx) {
        MessageDialogEx1 dialog = new MessageDialogEx1();
        dialog.ctx = ctx;
        dialog.setCancelable(false);
        return dialog;
    }

    //是否可权限
    public MessageDialogEx1 cancelable(boolean cancelable) {
        setCancelable(cancelable);
        return this;
    }

    //设置消息
    public MessageDialogEx1 message(Object message) {
        bufferMessage = String.valueOf(message);
        return this;
    }

    //设置按钮文字
    public MessageDialogEx1 title(Object buttonText) {
        this.buttonText = String.valueOf(buttonText);
        return this;
    }

    //关闭时的消息监听器
    public MessageDialogEx1 onClose(Action onClose) {
        this.onClose = onClose;
        return this;
    }

    //显示
    public MessageDialogEx1 show() {
        synchronized (openTimes) {
            openTimes++;
        }
        final String tempMessage = bufferMessage;
        WorkThread.post(() -> {
            lock.lock();
            while (state != DialogState.CREATED && state != DialogState.CLOSED)
                Threads.sleep(100);
            ctx.post(() -> {
                showIcon = false;
                message = tempMessage;
                FragmentManager manager = ctx.getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(this, Texts.random(false, false));
                transaction.commitAllowingStateLoss();
            });
            state = DialogState.OPENING;
            lock.unlock();
        });
        return this;
    }

    //关闭
    public MessageDialogEx1 close() {
        synchronized (openTimes) {
            if (openTimes < 1) return this;
            openTimes--;
        }
        WorkThread.post(() -> {
            lock.lock();
            while (state != DialogState.OPEN)
                Threads.sleep(100);
            ctx.post(() -> {
                dismissAllowingStateLoss();
                state = DialogState.CLOSING;
            });
            lock.unlock();
        });
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        View root = getActivity().getLayoutInflater().inflate(R.layout.layout_message_dialog_ex1, null);
        builder.setView(root);
        AlertDialog dialog = builder.create();
        //解析控件
        TextView messageText = root.findViewById(R.id.text_msg);
        TextView okButton = root.findViewById(R.id.bt_ok);
        messageText.setText(message);
        okButton.setText(buttonText);
        Views.onClick(okButton, () -> {
            state = DialogState.CLOSING;
            dismissAllowingStateLoss();
            //窗口关闭时的事件回调
            if (onClose != null)
                onClose.runAndPostException();
        });
        //更新状态
        state = DialogState.OPEN;
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        state = DialogState.CLOSED;
    }

    //判断弹窗是否打开
    public boolean isOpen() {
        return state == DialogState.OPEN || state == DialogState.OPENING;
    }
}
