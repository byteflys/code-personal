package com.easing.commons.android.hybrid;

import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.code.Console;
import com.easing.commons.android.data.JSON;
import com.easing.commons.android.thread.Threads;
import com.easing.commons.android.ui.dialog.MessageDialogEx1;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.script.ScriptEngine;

import lombok.SneakyThrows;

@SuppressWarnings("all")
public class JavascriptBridge {

    protected transient ScriptEngine engine;

    public void engine(ScriptEngine engine) {
        this.engine = engine;
    }

    //Java调用Javascript
    @SneakyThrows
    public Object callJavascript(String javascriptFuncName, Map<String, Object> argMap) {
        if (argMap == null)
            argMap = new LinkedHashMap();
        Object result = engine.eval(javascriptFuncName + "(" + JSON.stringify(argMap) + ")");
        return result;
    }

    //Java调用Javascript
    @SneakyThrows
    public Object callJavascript(String javascriptFuncName, Object argObject) {
        if (argObject == null)
            argObject = new LinkedHashMap();
        Object result = engine.eval(javascriptFuncName + "(" + JSON.stringify(argObject) + ")");
        return result;
    }

    //Java调用Javascript
    @SneakyThrows
    public <T> T callJavascript(String javascriptFuncName, Map<String, Object> argMap, Type returnType) {
        Object result = callJavascript(javascriptFuncName, argMap);
        return toJavaObject(result, returnType);
    }

    //Java调用Javascript
    @SneakyThrows
    public <T> T callJavascript(String javascriptFuncName, Object argObject, Type returnType) {
        Object result = callJavascript(javascriptFuncName, argObject);
        return toJavaObject(result, returnType);
    }

    //Javascript返回的NativeObject转成Java的Object
    public <T> T toJavaObject(Object nativeObject, Type returnType) {
        if (nativeObject != null && "undefined".equals(nativeObject))
            return null;
        String json = JSON.stringify(nativeObject);
        return (T) JSON.parse(json, returnType);
    }

    //工具方法
    public void printVariable(Object variable) {
        if (variable != null && "undefined".equals(variable))
            variable = null;
        String json = JSON.stringify(variable);
        json = JSON.beautify(json);
        Console.info("JavascriptObject", json);
    }

    //工具方法
    public void alertVariable(Object variable) {
        if (variable != null && "undefined".equals(variable))
            variable = null;
        String json = JSON.stringify(variable);
        json = JSON.beautify(json);

        CommonActivity ctx = CommonApplication.currentActivity();
        MessageDialogEx1 dialog = MessageDialogEx1.create(ctx).message(json).cancelable(true).show();

        //阻塞JS线程，用于调试
        //弹窗关闭时，阻塞结束
        Threads.sleep(1000);
        while (dialog.isOpen())
            Threads.sleep(200);
    }

}
