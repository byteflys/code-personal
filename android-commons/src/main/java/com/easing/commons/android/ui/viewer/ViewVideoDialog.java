package com.easing.commons.android.ui.viewer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.easing.commons.android.R;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.helper.callback.Action;
import com.easing.commons.android.manager.Device;
import com.easing.commons.android.value.measure.Size;
import com.easing.commons.android.view.Views;

//视频预览弹窗
public class ViewVideoDialog extends DialogFragment {

    AppCompatActivity ctx;

    String url = null;

    View root;

    //静态创建方法
    public static ViewVideoDialog create() {
        ViewVideoDialog dialog = new ViewVideoDialog();
        dialog.setCancelable(false);
        return dialog;
    }

    //静态创建方法
    public static ViewVideoDialog create(String url) {
        ViewVideoDialog dialog = new ViewVideoDialog();
        dialog.setCancelable(true);
        dialog.url = url;
        return dialog;
    }

    //设置播放地址
    public ViewVideoDialog url(String url) {
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
        root = getActivity().getLayoutInflater().inflate(R.layout.dialog_view_video, null);
        builder.setView(root);
        AlertDialog dialog = builder.create();
        //解析控件
        VideoView player = root.findViewById(R.id.player);
        player.setVideoPath(url);
        player.start();
        View okButton = root.findViewById(R.id.bt_ok);
        Views.onClick(okButton, () -> {
            player.stopPlayback();
            player.suspend();
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

    public ViewVideoDialog onClose(Action onClose) {
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

