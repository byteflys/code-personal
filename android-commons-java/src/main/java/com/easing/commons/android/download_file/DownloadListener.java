package com.easing.commons.android.download_file;

/**
 * Created by Administrator on 2017/2/23.
 */
public interface DownloadListener {


    /**
     * 开始下载
     *
     * @param dowTag
     */
    void onStart(String dowTag);

    /**
     * 通知当前的下载进度
     *
     * @param progress
     */
    void onProgress(String dowTag, String progress);

    /**
     * 通知下载成功
     */
    void onSuccess(String dowTag, String filePath);

    /**
     * 通知下载失败
     */
    void onFailed(String dowTag);

    /**
     * 通知下载暂停
     */
    void onPaused(String dowTag);

    /**
     * 通知下载取消事件
     */
    void onCanceled(String dowTag);

}
