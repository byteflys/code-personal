package com.easing.commons.android.ui.dialog;

import android.widget.EditText;

import com.easing.commons.android.app.CommonActivity;

//设置服务器弹窗
@SuppressWarnings("all")
public interface IServerSettingDialog {

    IServerSettingDialog activity(CommonActivity activity);

    IServerSettingDialog cancelable(boolean cancelable);

    IServerSettingDialog show();

    IServerSettingDialog close();

    IServerSettingDialog onAutoFill(OnAutoFill onAutoFill);

    IServerSettingDialog onConfirm(OnConfirm onConfirm);

    public interface OnAutoFill {

        void autofill(EditText ipEdit, EditText portEdit);
    }

    public interface OnConfirm {

        void onConfirm(String ip, String port);
    }
}

























