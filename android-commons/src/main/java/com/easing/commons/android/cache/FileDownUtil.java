package com.easing.commons.android.cache;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.helper.callback.HttpRestCallBackListener;
import com.easing.commons.android.helper.exception.BizException;
import com.easing.commons.android.http.Postman;
import com.easing.commons.android.io.Files;
import com.easing.commons.android.thread.WorkThread;
import com.easing.commons.android.ui.dialog.TipBox;
import com.easing.commons.android.value.http.HttpMethod;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 下载文件
 */
public class FileDownUtil {

    //开始下载图片
    public static void start(String url, String path, boolean isNewThread, HttpRestCallBackListener<Object> callback) {
        WorkThread.post(() -> {
            Postman.create()
                    .url(url)
                    .method(HttpMethod.GET)
                    .onIoException((postman, call, e) -> {
                        if (callback != null) {
                            callback.onFailCall(500, "下载失败");
                        }
                        throw BizException.THREAD_NORMAL_EXIT;
                    })
                    .onBizException((postman, call, e) -> {
                        if (callback != null) {
                            callback.onFailCall(500, "下载失败");
                        }
                        throw BizException.THREAD_NORMAL_EXIT;
                    })
                    .onResponse((postman, call, response) -> {
                        if (response.code() >= 300)
                            throw BizException.THREAD_NORMAL_EXIT;
                        byte[] bytes = response.body().bytes();
                        //文件一般比较大
                        //如果字节数组比较小，或里面存储的是JSON/XML字符串，则代表请求失败
                        if (bytes.length < 1024 * 10) {
                            if (Texts.isJsonString(bytes) || Texts.isXmlString(bytes))
                                throw BizException.THREAD_NORMAL_EXIT;
                        }
                        //先保存到临时位置，下载完成后再修改到正确位置
                        String tempPath = path + ".downloadtask";
                        Files.writeToFile(tempPath, bytes);
                        //重命名至正确路径
                        Files.renameFile(tempPath, path);

                      /*  try {
                            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                                    file.getAbsolutePath(), file.getName(), null);
                            TipBox.tipInCenter("已保存至" + file);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            TipBox.tipInCenter("保存失败" + file);
                        }*/
                        // 最后通知图库更新
                        CommonApplication.currentActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(path))));

                        if (callback != null) {
                            callback.onSucceedCall(path);
                        }
                    })
                    .execute(true);

        }, isNewThread);

    }


}
