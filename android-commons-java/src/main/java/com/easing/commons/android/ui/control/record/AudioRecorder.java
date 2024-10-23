package com.easing.commons.android.ui.control.record;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import com.easing.commons.android.R;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.io.ProjectFile;
import com.easing.commons.android.manager.Fonts;
import com.easing.commons.android.thread.WorkThread;
import com.easing.commons.android.io.Files;
import com.easing.commons.android.thread.MainThread;
import com.easing.commons.android.view.Views;

//这个类使用了AudioRecordService，必须在清单中注册服务，否则录音文件为空
@SuppressWarnings("all")
public class AudioRecorder extends DialogFragment {

    AppCompatActivity ctx;

    String storagePath;

    Chronometer timer;

    TextView cancelButton;
    TextView recordButton;

    OnRecordFinish onRecordFinish;

    boolean recording = false;
    boolean autoCancelOnFinish = true;

    //静态创建方法
    public static AudioRecorder create() {
        AudioRecorder fragment = new AudioRecorder();
        fragment.setCancelable(false);
        return fragment;
    }

    public AudioRecorder storageLocation(String storagePath) {
        if (storagePath == null) {
            String filename = Texts.random() + ".aac";
            storagePath = ProjectFile.getProjectAudioPath(filename);
        }
        this.storagePath = storagePath;
        return this;
    }

    public AudioRecorder onRecordFinish(OnRecordFinish onRecordFinish) {
        this.onRecordFinish = onRecordFinish;
        return this;
    }

    public AudioRecorder autoCancelOnFinish(boolean autoCancelOnFinish) {
        this.autoCancelOnFinish = autoCancelOnFinish;
        return this;
    }

    //显示
    public void show(AppCompatActivity ctx) {
        this.ctx = ctx;
        FragmentManager manager = ctx.getSupportFragmentManager();
        show(manager, Texts.random());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        View root = Views.inflate(ctx, R.layout.dialog_audio_recorder);
        builder.setView(root);
        AlertDialog dialog = builder.create();

        //解析控件
        timer = root.findViewById(R.id.timer);
        cancelButton = root.findViewById(R.id.bt_cancel);
        recordButton = root.findViewById(R.id.bt_capture);
        Fonts.bindFont(root, Fonts.Font.LIBIAN);

        //取消录音
        Views.onClick(cancelButton, () -> {
            cancelRecord();
            dismissAllowingStateLoss();
        });

        //开始或停止录音
        Views.onClick(recordButton, () -> {
            recording = !recording;
            if (recording)
                startRecord();
            else
                stopRecord();
        });

        return dialog;
    }

    //开始录音
    private void startRecord() {
        recording = true;
        recordButton.setText("停止");
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        Intent intent = new Intent(ctx, AudioRecordService.class);
        Files.createFile(storagePath);
        intent.putExtra("path", storagePath);
        ctx.startService(intent);
        //防止快速点击，MediaRecorder无法及时响应
        recordButton.setEnabled(false);
        MainThread.postLater(() -> recordButton.setEnabled(true), 500);
    }

    //结束录音
    private void stopRecord() {
        recording = false;
        recordButton.setText("开始");
        timer.stop();
        timer.setText("00:00");
        Intent intent = new Intent(ctx, AudioRecordService.class);
        ctx.stopService(intent);
        if (onRecordFinish != null)
            onRecordFinish.onFinish(storagePath);
        if (autoCancelOnFinish)
            dismiss();
        //防止快速点击，MediaRecorder无法及时响应
        recordButton.setEnabled(false);
        MainThread.postLater(() -> recordButton.setEnabled(true), 500);
    }

    //取消录音
    private void cancelRecord() {
        if (!recording) return;
        recording = false;
        recordButton.setText("开始");
        timer.stop();
        timer.setText("00:00");
        Intent intent = new Intent(ctx, AudioRecordService.class);
        ctx.stopService(intent);
        WorkThread.postLater(() -> Files.deleteFile(storagePath), 500);
    }

    public interface OnRecordFinish {

        void onFinish(String storagePath);
    }
}
