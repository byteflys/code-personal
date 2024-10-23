package com.easing.commons.android.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.easing.commons.android.R;
import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.code.Console;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.manager.Services;
import com.easing.commons.android.thread.Threads;
import com.easing.commons.android.thread.WorkThread;
import com.easing.commons.android.ui.control.button.ImageButtonM2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//输入框
@SuppressWarnings("all")
public class InputDialog extends DialogFragment {

    Lock lock = new ReentrantLock();

    DialogState state = DialogState.CREATED;

    Integer openTimes = 0;

    CommonActivity ctx;

    TextView messageText;
    EditText contentEdit;
    ImageButtonM2 okButton;

    String bufferTitle = "";
    String title = "";
    String content = "";

    boolean enableMultiLineInput = false;

    boolean autoFocus = false;

    OnInput onInput;
    OnCancel onCancel;

    //静态创建方法
    public static InputDialog create(CommonActivity ctx) {
        InputDialog dialog = new InputDialog();
        dialog.ctx = ctx;
        return dialog;
    }

    public InputDialog title(Object title) {
        this.bufferTitle = Texts.toString(title);
        return this;
    }

    public InputDialog content(Object content) {
        this.content = Texts.toString(content);
        return this;
    }

    public InputDialog onInput(OnInput onInput) {
        this.onInput = onInput;
        return this;
    }

    public InputDialog onCancel(OnCancel onCancel) {
        this.onCancel = onCancel;
        return this;
    }

    //是否可取消
    public InputDialog cancelable(boolean cancelable) {
        setCancelable(cancelable);
        return this;
    }

    //是否自动获得焦点
    public InputDialog autoFocus(boolean autoFocus) {
        this.autoFocus = autoFocus;
        return this;
    }

    //自动确认
    public InputDialog autoConfirm() {
        if (state == DialogState.OPEN)
            okButton.performClick();
        return this;
    }

    //允许多行显示
    public InputDialog enableMultiLineInput(boolean enableMultiLineInput) {
        this.enableMultiLineInput = enableMultiLineInput;
        return this;
    }

    //显示
    public InputDialog show() {
        synchronized (openTimes) {
            openTimes++;
        }
        final String tempTitle = bufferTitle;
        WorkThread.post(() -> {
            lock.lock();
            while (state != DialogState.CREATED && state != DialogState.CLOSED) {
                Console.info(state);
                Threads.sleep(100);
            }
            ctx.post(() -> {
                title = tempTitle;
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
    public InputDialog close() {
        synchronized (openTimes) {
            if (openTimes < 1) return this;
            openTimes--;
        }
        WorkThread.post(() -> {
            lock.lock();
            while (state != DialogState.OPEN)
                Threads.sleep(100);
            ctx.post(() -> {
                state = DialogState.CLOSING;
                dismissAllowingStateLoss();
            });
            lock.unlock();
        });
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        View root = getActivity().getLayoutInflater().inflate(R.layout.layout_input_dialog, null);
        builder.setView(root);
        AlertDialog dialog = builder.create();
        //解析控件
        messageText = root.findViewById(R.id.text_msg);
        contentEdit = root.findViewById(R.id.edit_content);
        contentEdit.setText(content);
        contentEdit.setSingleLine(!enableMultiLineInput);
        okButton = root.findViewById(R.id.bt_ok);
        messageText.setText(title);
        okButton.setOnClickListener(v -> {
            if (onInput == null)
                close();
            else
                onInput.onInput(contentEdit.getText().toString());
        });
        //更新状态
        state = DialogState.OPEN;
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        contentEdit.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                contentEdit.requestFocus();
                contentEdit.setSelection(contentEdit.getText().length());
                if (autoFocus)
                    Services.openKeyboard(contentEdit);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                contentEdit.clearFocus();
                Services.closeKeyboard(contentEdit);
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        state = DialogState.CLOSED;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (onCancel != null)
            onCancel.onCancel();
    }

    //未指定OnInput时，需要手动关闭
    public interface OnInput {

        void onInput(String content);
    }

    public interface OnCancel {

        void onCancel();
    }
}
