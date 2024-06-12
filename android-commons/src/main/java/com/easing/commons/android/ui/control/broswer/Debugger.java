package com.easing.commons.android.ui.control.broswer;

import android.webkit.JavascriptInterface;

import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.code.Console;
import com.easing.commons.android.data.JSON;
import com.easing.commons.android.ui.dialog.MessageDialog;

//向Javascript注入一个Debugger对象
//用于打印调试或异常信息到Java层
public class Debugger {

    @JavascriptInterface
    public void log(String data) {
        String beautifyJson = JSON.beautify(data);
        Console.info("WebViewDebugger\n" + beautifyJson);
    }

    @JavascriptInterface
    public void error(String data) {
        String beautifyJson = JSON.beautify(data);
        Console.error("WebViewDebugger\n" + beautifyJson);
    }

    @JavascriptInterface
    public void alert(String data) {
        String beautifyJson = JSON.beautify(data);
        MessageDialog.create(CommonApplication.currentActivity()).message(beautifyJson).showWithoutIcon();
    }
}
