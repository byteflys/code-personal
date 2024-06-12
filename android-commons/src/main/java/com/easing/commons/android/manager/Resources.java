package com.easing.commons.android.manager;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;

import com.easing.commons.android.app.CommonApplication;

import lombok.SneakyThrows;

import java.io.InputStream;

public class Resources {

    @SneakyThrows
    public static byte[] readAssetBytes(Context ctx, String path) {
        InputStream in = ctx.getAssets().open(path);
        byte[] buffer = new byte[in.available()];
        in.read(buffer);
        in.close();
        return buffer;
    }

    @SneakyThrows
    public static byte[] readAssetBytes(String path) {
        InputStream in = CommonApplication.ctx.getAssets().open(path);
        byte[] buffer = new byte[in.available()];
        in.read(buffer);
        in.close();
        return buffer;
    }

    @SneakyThrows
    public static String readAssetText(Context ctx, String path, String encode) {
        if (encode == null || encode.equals(""))
            encode = "UTF-8";
        InputStream in = ctx.getAssets().open(path);
        byte[] buffer = new byte[in.available()];
        in.read(buffer);
        in.close();
        return new String(buffer, encode);
    }

    @SneakyThrows
    public static String readAssetText(String path) {
        return readAssetText(CommonApplication.ctx, path, "UTF-8");
    }

    @SneakyThrows
    public static InputStream readAssetStream(String path) {
        return CommonApplication.ctx.getAssets().open(path);
    }

    //通过资源id解出drawable
    public static Drawable drawable(@DrawableRes int drawableId) {
        return CommonApplication.ctx.getDrawable(drawableId);
    }
}
