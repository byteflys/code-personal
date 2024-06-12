package com.easing.commons.android.ui.dialog;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;

import com.easing.commons.android.code.Console;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.manager.Device;
import com.easing.commons.android.manager.Dimens;
import com.easing.commons.android.value.apk.PackageVersion;
import com.easing.commons.android.value.measure.Size;
import com.easing.commons.android.view.Views;
import com.easing.commons.android.R;

//自定义Toast
@SuppressWarnings("all")
public class TipBox {

    static Context ctx;
    static Handler handler;

    volatile static Toast toast;

    //绑定应用上下文
    public static void init(Context ctx) {
        TipBox.ctx = ctx;
        TipBox.handler = new Handler();
    }

    //取消当前toast
    public static void cancel() {
        handler.post(() -> {
            if (toast != null)
                toast.cancel();
        });
    }

    //弹出toast
    public static void toast(Object message, @LayoutRes Integer layoutID, Integer bottomMarginDp, Handler handler) {
        final int layout = layoutID == null ? R.layout.layout_tip_box_default : layoutID;
        final int paddingHeight = bottomMarginDp == null ? 0 : Dimens.toPx(bottomMarginDp);
        if (handler == null) handler = TipBox.handler;
        handler.post(() -> {
            if (toast != null)
                toast.cancel();
            View root = LayoutInflater.from(ctx).inflate(layout, null);
            TextView textView = Views.findView(root, R.id.text);
            textView.setText(message == null ? "NULL" : message.toString());
            View paddingView = Views.findView(root, R.id.view_padding);
            Views.size(paddingView, null, paddingHeight);
            toast = new Toast(ctx);
            toast.setView(root);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        });
    }

    //在屏幕顶部显示toast
    public static void tipAtTop(Object message) {
        tipAtTopWithMargin(message, 0);
    }

    //在屏幕顶部显示toast
    public static void tipAtTopWithMargin(Object message, int dp) {
        if (handler == null) handler = TipBox.handler;
        handler.post(() -> {
            if (toast != null)
                toast.cancel();
            View root = LayoutInflater.from(ctx).inflate(R.layout.layout_tip_box_02, null);
            View wrapper = root.findViewById(R.id.wrapper);
            Size screenSize = Device.screenSize();
            Views.size(wrapper, screenSize.w, screenSize.h);
            TextView textView = Views.findView(root, R.id.text);
            View paddingView = Views.findView(root, R.id.view_padding);
            textView.setText(message == null ? "NULL" : message.toString());
            Views.size(paddingView, 0, Dimens.toPx(dp));
            toast = new Toast(ctx);
            toast.setView(root);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        });
    }

    static int count = 0;

    //在屏幕中央显示toast
    public static void tipInCenter(Object message) {
        if (handler == null) handler = TipBox.handler;
        handler.post(() -> {
            if (toast != null)
                toast.cancel();
            View root = LayoutInflater.from(ctx).inflate(R.layout.layout_tip_box_01, null);
            View wrapper = root.findViewById(R.id.wrapper);
            Size screenSize = Device.screenSize();
            Views.size(wrapper, screenSize.w, screenSize.h);
            TextView textView = Views.findView(root, R.id.text);
            textView.setText(message == null ? "NULL" : message.toString());
            toast = new Toast(ctx);
            toast.setView(root);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        });
    }

    //在屏幕中央显示toast
    public static void tipInCenterLong(Object message) {
        if (handler == null) handler = TipBox.handler;
        handler.post(() -> {
            if (toast != null)
                toast.cancel();
            View root = LayoutInflater.from(ctx).inflate(R.layout.layout_tip_box_01, null);
            View wrapper = root.findViewById(R.id.wrapper);
            Size screenSize = Device.screenSize();
            Views.size(wrapper, screenSize.w, screenSize.h);
            TextView textView = Views.findView(root, R.id.text);
            textView.setText(message == null ? "NULL" : message.toString());
            toast = new Toast(ctx);
            toast.setView(root);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        });
    }

    //在屏幕中央显示toast
    public static void tipInCenterLong(Object message, @LayoutRes int layoutResId) {
        if (handler == null) handler = TipBox.handler;
        handler.post(() -> {
            if (toast != null)
                toast.cancel();
            View root = LayoutInflater.from(ctx).inflate(layoutResId, null);
            View wrapper = root.findViewById(R.id.wrapper);
            Size screenSize = Device.screenSize();
            Views.size(wrapper, screenSize.w, screenSize.h);
            TextView textView = Views.findView(root, R.id.text);
            textView.setText(message == null ? "NULL" : message.toString());
            toast = new Toast(ctx);
            toast.setView(root);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        });
    }

    //使用默认样式显示toast
    public static void tip(Object message) {
        TipBox.toast(message, null, null, null);
    }

    //使用默认样式显示toast
    public static void tipWithMargin(Object message, Integer bottomMarginDp) {
        TipBox.toast(message, null, bottomMarginDp, null);
    }

    //在debug模式下弹出toast，在release模式下不弹
    public static void tipOnDebug(PackageVersion apkVersion, Object message) {
        if (apkVersion != PackageVersion.RELEASE)
            TipBox.tip(message);
    }

    //弹窗或打印到控制台
    public static void tipOrPrint(boolean tip, Object message) {
        if (tip)
            TipBox.tip(message);
        else
            Console.error(message);
    }

    //通过toast显示array
    public static void tipForArray(Object... messageArray) {
        String arrayToString = Texts.arrayToString(messageArray);
        TipBox.tip(arrayToString);
    }

    //在toast中通过多行显示array
    public static void tipAsMultiline(Object... messageArray) {
        String message = String.valueOf(messageArray[0]);
        for (int i = 1; i < messageArray.length; i++)
            message = message + "\n" + String.valueOf(messageArray[i]);
        TipBox.tip(message);
    }

    //在调试模式下，通过toast显示array
    public static void tipForArrayOnDebug(PackageVersion apkVersion, Object... messageArray) {
        String arrayToString = Texts.arrayToString(messageArray);
        if (apkVersion != PackageVersion.RELEASE)
            TipBox.tip(arrayToString);
    }
}
