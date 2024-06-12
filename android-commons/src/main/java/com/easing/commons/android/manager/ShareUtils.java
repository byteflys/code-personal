package com.easing.commons.android.manager;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.code.Console;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.io.Files;
import com.easing.commons.android.media.MediaType;
import com.easing.commons.android.ui.dialog.MessageDialog;

import java.util.List;

public class ShareUtils {

    //分享文件到微信
    public static void shareWithWechat(CommonActivity ctx, String file) {

        //判断文件存不存在
        boolean exist = Files.exist(file);
        if (!exist) {
            MessageDialog.create(ctx).message("要分享的文件不存在").showWithoutIcon();
            return;
        }

        //判断有没有安装微信
        boolean installed = isWechatInstalled();
        if (!installed) {
            MessageDialog.create(ctx).message("未安装微信客户端").showWithoutIcon();
            return;
        }

        //启动微信分享组件
        Uri uri = Uris.fromFile(file);
        String mimeType = MediaType.getMimeType(file);
        Intent intent = new Intent();
        ComponentName component = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(component);
        intent.setAction(Intent.ACTION_SEND);
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        ctx.startActivity(Intent.createChooser(intent, "Share"));
    }

    //分享文件到QQ
    public static void shareWithQQ(CommonActivity ctx, String file) {

        //判断文件存不存在
        boolean exist = Files.exist(file);
        if (!exist) {
            MessageDialog.create(ctx).message("要分享的文件不存在").showWithoutIcon();
            return;
        }

        //判断有没有安装QQ
        boolean installed = isQQInstalled();
        if (!installed) {
            MessageDialog.create(ctx).message("未安装QQ客户端").showWithoutIcon();
            return;
        }

        //启动QQ分享组件
        Uri uri = Uris.fromFile(file);
        String mimeType = MediaType.getMimeType(file);
        Intent intent = new Intent();
        ComponentName component = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
        intent.setComponent(component);
        intent.setAction(Intent.ACTION_SEND);
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        ctx.startActivity(Intent.createChooser(intent, "Share"));
    }

    //判断电脑有无安装微信
    public static boolean isWechatInstalled() {
        PackageManager packageManager = CommonApplication.ctx.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        for (int i = 0; i < packageInfos.size(); i++)
            if (Texts.equal(packageInfos.get(i).packageName, "com.tencent.mm"))
                return true;
        return false;
    }

    //判断电脑有无安装QQ
    public static boolean isQQInstalled() {
        PackageManager packageManager = CommonApplication.ctx.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        for (int i = 0; i < packageInfos.size(); i++) {
            String packageName = packageInfos.get(i).packageName;
            Console.info("packageName " + packageName);
            if (Texts.equal(packageName, "com.tencent.mobileqq"))
                return true;
        }
        return false;
    }
}
