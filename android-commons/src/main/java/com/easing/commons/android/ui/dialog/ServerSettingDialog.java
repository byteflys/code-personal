package com.easing.commons.android.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.easing.commons.android.R;
import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.view.Views;

//设置服务器弹窗
@SuppressWarnings("all")
public class ServerSettingDialog extends DialogFragment implements IServerSettingDialog {

    CommonActivity activity;

    OnAutoFill onAutoFill;
    OnConfirm onConfirm;

    @Override
    public ServerSettingDialog activity(CommonActivity activity) {
        this.activity = activity;
        return this;
    }

    @Override
    public ServerSettingDialog cancelable(boolean cancelable) {
        setCancelable(cancelable);
        return this;
    }

    @Override
    public ServerSettingDialog onAutoFill(OnAutoFill onAutoFill) {
        this.onAutoFill = onAutoFill;
        return this;
    }

    @Override
    public ServerSettingDialog onConfirm(OnConfirm onConfirm) {
        this.onConfirm = onConfirm;
        return this;
    }

    @Override
    public ServerSettingDialog show() {
        FragmentManager manager = activity.getSupportFragmentManager();
        show(manager, Texts.random());
        return this;
    }

    @Override
    public ServerSettingDialog close() {
        dismissAllowingStateLoss();
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //根据布局创建弹窗
        View root = Views.inflate(activity, R.layout.md001_dialog_server_setting);
        AlertDialog dialog = new AlertDialog.Builder(activity).setView(root).create();

        //解析控件
        EditText ipEdit = root.findViewById(R.id.edit_ip);
        EditText portEdit = root.findViewById(R.id.edit_port);
        View okButton = root.findViewById(R.id.bt_ok);

        //自动填充上次的服务器地址
        if (onAutoFill != null)
            onAutoFill.autofill(ipEdit, portEdit);

        //保存主机地址
        okButton.setOnClickListener(v -> {
            String ip = ipEdit.getText().toString();
            String port = portEdit.getText().toString();
            //检查服务器地址格式
            if (!checkFormat(ip, port))
                return;
            //保存服务器地址
            if (onConfirm != null)
                onConfirm.onConfirm(ip, port);
        });

        return dialog;
    }

    //检查服务器地址格式
    protected boolean checkFormat(String ip, String port) {
        if (Texts.isEmpty(ip)) {
            TipBox.tipInCenter("请填写服务器地址");
            return false;
        }
        if (Texts.isEmpty(port)) {
            TipBox.tipInCenter("请填写服务器端口");
            return false;
        }
        if (!Texts.isIp(ip) && !Texts.isDomain(ip)) {
            TipBox.tipInCenter("服务器地址格式错误");
            return false;
        }
        if (!Texts.isPort(port)) {
            TipBox.tipInCenter("服务器端口格式错误");
            return false;
        }
        return true;
    }

}
