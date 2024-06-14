package com.easing.commons.android.cache;

import static android.content.Context.BIND_AUTO_CREATE;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.download_file.BreakpointDownloadService;
import com.easing.commons.android.download_file.DownloadListener;
import com.easing.commons.android.ui.dialog.TipBox;

import org.greenrobot.greendao.annotation.NotNull;

/**
 * 文件断点下载
 */
public class FileBreakpointDownUtil {

    private static FileBreakpointDownUtil fileDownUtil;

    private CommonApplication mActivity;

    public static FileBreakpointDownUtil getInstance() {
        if (fileDownUtil == null)
            fileDownUtil = new FileBreakpointDownUtil();
        return fileDownUtil;
    }

    private FileBreakpointDownUtil() {
        mActivity = CommonApplication.ctx;
        if (breakpointDownloadIntent == null) {
            breakpointDownloadIntent = new Intent(mActivity, BreakpointDownloadService.class);
            mActivity.startService(breakpointDownloadIntent);
            mActivity.bindService(breakpointDownloadIntent, serviceConnection, BIND_AUTO_CREATE);
            BreakpointDownloadService.isDownload = false;
        }
    }

    private Intent breakpointDownloadIntent;//下载服务
    private BreakpointDownloadService.DownloadBinder downloadBinder;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (BreakpointDownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    /**
     * 断点下载 单个下载
     *
     * @param url              下载url地址 支持断点下载
     * @param outPath          文件保存的路径
     * @param fileName         文件名包括后缀
     * @param dowTag           下载的唯一标签方便区分
     * @param downloadListener 下载监听 可以为null 空用
     * @OnEvent(type = "BreakpointDownloadService")
     * public void onBreakpointDownloadService(String type, DownloadReqBean download) {
     * download.getDownloadTypeTag()
     * }
     */
    public void startBreakpointDown(@NotNull String url, @NotNull String outPath, String fileName, String dowTag, DownloadListener downloadListener) {

        if (BreakpointDownloadService.isDownload) {
            TipBox.tipInCenter("下载中，稍后再试");
            return;
        }
        if (downloadBinder != null)
            downloadBinder.startDownloadFile(url, outPath, fileName, dowTag, downloadListener); //下载地图

    }

    /**
     * 取消下载
     */
    public void cancelDownload() {

        if (downloadBinder != null)
            downloadBinder.cancelDownload(); //下载地图

    }
}
