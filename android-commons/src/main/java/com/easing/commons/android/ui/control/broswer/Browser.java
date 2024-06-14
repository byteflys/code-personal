package com.easing.commons.android.ui.control.broswer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.util.AttributeSet;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.code.Console;
import com.easing.commons.android.data.JSON;
import com.easing.commons.android.ui.dialog.MessageDialog;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("all")
public class Browser extends WebView {

    public Browser(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //浏览器设置
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setSupportZoom(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        //请求处理
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView wv, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("file:///"))
                    return false;
                //非HTTP协议调用本地应用打开
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    CommonApplication.ctx.startActivity(intent);
                } catch (Exception e) {
                    Console.error(e);
                }
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //允许不安全的HTTPS证书
                handler.proceed();
            }
        });

        //内核处理
        setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                //无内容时隐藏浏览器
                if (view.getUrl().equalsIgnoreCase("about:blank"))
                    setAlpha(0);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                //JS弹窗改成APP弹窗
                MessageDialog.create((CommonActivity) context).message(message).showWithoutIcon();
                result.cancel();
                return true;
            }

            @Override
            public void onPermissionRequest(PermissionRequest request) {
                //允许摄像头等权限
                request.grant(request.getResources());
            }
        });

        //注入调试工具
        //Javascript可通过Debugger对象，来调用Java层打印调试信息
        addJavascriptObject(new Debugger(), "debugger");
    }

    //注入一个Java对象方法到Javascript的window对象上，从而使得Javascript可以调用Java代码
    //只有被@JavascriptInterface注解和public关键字修饰的方法，才能被加载到Javascript的window对象中
    public Browser addJavascriptObject(Object windowObject) {
        addJavascriptInterface(windowObject, "java");
        return this;
    }

    //注入一个Java对象方法到Javascript的window对象上，从而使得Javascript可以调用Java代码
    //只有被@JavascriptInterface注解和public关键字修饰的方法，才能被加载到Javascript的window对象中
    public Browser addJavascriptObject(Object windowObject, String objectName) {
        addJavascriptInterface(windowObject, objectName);
        return this;
    }

    //Java调用Javascript方法
    public Browser callJavascript(String javascriptFuncName, Map<String, Object> argMap) {
        if (argMap == null) argMap = new LinkedHashMap();
        loadUrl("javascript:" + javascriptFuncName + "(" + JSON.stringify(argMap) + ")");
        return this;
    }

    //Java调用Javascript方法
    public Browser callJavascript(String javascriptFuncName, Object argObject) {
        if (argObject == null) argObject = new LinkedHashMap();
        loadUrl("javascript:" + javascriptFuncName + "(" + JSON.stringify(argObject) + ")");
        return this;
    }

    //Java调用Javascript方法
    public Browser callJavascript(String javascriptFuncName, String argJson) {
        if (argJson == null) argJson = "{}";
        loadUrl("javascript:" + javascriptFuncName + "(" + argJson + ")");
        return this;
    }

    //打开assets文件夹中的页面
    public Browser openAsset(String resource) {
        loadUrl("file:///android_asset/" + resource);
        return this;
    }

    //打开网页
    public Browser openWebPage(String url) {
        loadUrl(url);
        return this;
    }

}
