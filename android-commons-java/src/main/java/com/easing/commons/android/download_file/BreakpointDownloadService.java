package com.easing.commons.android.download_file;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.easing.commons.android.event.EventBus;

import org.greenrobot.greendao.annotation.NotNull;

import java.io.File;

/**
 * 为了保证DownloadTask可以一直在后台运行，我们还需要创建一个下载的服务。
 */
public class BreakpointDownloadService extends Service {

    private DownloadTask downloadTask;

    private String downloadUrl;
    private DownloadReqBean downloadReqBean;

    /**
     * 当前是否再下载
     */
    public static boolean isDownload = false;

    private DownloadListener listener = new DownloadListener() {

        /**
         * 构建了一个用于显示下载进度的通知
         * @param progress
         */
        @Override
        public void onProgress(String dowTag, String progress) {
            //NotificationManager的notify()可以让通知显示出来。
            //notify(),接收两个参数，第一个参数是id:每个通知所指定的id都是不同的。第二个参数是Notification对象。
            // getNotificationManager().notify(1,getNotification("Downloading...",progress));

            //progress = 2147455564

            if (downloadReqBean == null)
                downloadReqBean = new DownloadReqBean();
            downloadReqBean.setStatus(DownloadStatus.DOWNLOAD_PROGRESS);
            downloadReqBean.setDownloadTypeTag(dowTag);
            downloadReqBean.setValue("已下载：" + progress + " %");

            post(downloadReqBean);
            Log.i("DownloadService", "progress = " + progress);

        }

        @Override
        public void onStart(String dowTag) {
            if (downloadReqBean == null)
                downloadReqBean = new DownloadReqBean();
            downloadReqBean.setStatus(DownloadStatus.DOWNLOAD_START);
            downloadReqBean.setDownloadTypeTag(dowTag);
            downloadReqBean.setValue(dowTag + "开始下载");

            post(downloadReqBean);
        }

        /**
         * 创建了一个新的通知用于告诉用户下载成功啦
         */
        @Override
        public void onSuccess(String dowTag, String filePath) {
            downloadTask = null;
            //下载成功时将前台服务通知关闭，并创建一个下载成功的通知
            // stopForeground(true);
            // getNotificationManager().notify(1, getNotification("Download Success", -1));
            Log.i("DownloadService", "progress = Download Success");


            if (downloadReqBean == null)
                downloadReqBean = new DownloadReqBean();
            downloadReqBean.setStatus(DownloadStatus.DOWNLOAD_SUCCESS);
            downloadReqBean.setDownloadTypeTag(dowTag);
            downloadReqBean.setValue(filePath);
            post(downloadReqBean);

        }

        /**
         *用户下载失败
         */
        @Override
        public void onFailed(String dowTag) {
            downloadTask = null;
            //下载失败时，将前台服务通知关闭，并创建一个下载失败的通知
            stopForeground(true);
            //  getNotificationManager().notify(1, getNotification("Download Failed", -1));
            Log.i("DownloadService", "progress =  Download Failed");

            if (downloadReqBean == null)
                downloadReqBean = new DownloadReqBean();
            downloadReqBean.setStatus(DownloadStatus.DOWNLOAD_FAILED);
            downloadReqBean.setDownloadTypeTag(dowTag);
            downloadReqBean.setValue(dowTag + "下载失败");
            post(downloadReqBean);
        }

        /**
         * 用户暂停
         */
        @Override
        public void onPaused(String dowTag) {
            downloadTask = null;
            if (downloadReqBean == null)
                downloadReqBean = new DownloadReqBean();
            downloadReqBean.setStatus(DownloadStatus.DOWNLOAD_PAUSED);
            downloadReqBean.setDownloadTypeTag(dowTag);
            downloadReqBean.setValue(dowTag + "暂停下载");
            post(downloadReqBean);
        }


        /**
         * 用户取消
         */
        @Override
        public void onCanceled(String dowTag) {
            downloadTask = null;
            //取消下载，将前台服务通知关闭，并创建一个下载失败的通知
            stopForeground(true);
            if (downloadReqBean == null)
                downloadReqBean = new DownloadReqBean();
            downloadReqBean.setStatus(DownloadStatus.DOWNLOAD_CANCELED);
            downloadReqBean.setDownloadTypeTag(dowTag);
            downloadReqBean.setValue(dowTag + "取消下载");
            post(downloadReqBean);
        }
    };

    private DownloadBinder mBinder = new DownloadBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * 为了要让DownloadService可以和活动进行通信，我们创建了一个DownloadBinder对象
     */
    public class DownloadBinder extends Binder {

        /**
         * 开始下载
         *
         * @param outPath  保存路径
         * @param url      下载地址
         * @param fileName 文件名
         */
        public void startDownloadFile(@NotNull String url, @NotNull String outPath, String fileName, String dowTag, DownloadListener downloadListener) {
            //  if (downloadTask == null)
            downloadTask = new DownloadTask(downloadListener);
            downloadUrl = url;
            if (downloadListener == null)
                downloadListener = listener;

            //防止空指针
            if (fileName == null)
                fileName = "";

            BreakpointDownloadService.isDownload = true;
            if (downloadListener != null)
                downloadListener.onStart(dowTag);

            //启动下载任务
            downloadTask.execute(downloadUrl, outPath, fileName, dowTag);
            //  startForeground(1, getNotification("Downloading...", 0));
            Log.i("DownloadService", "progress = start Download ");
            // Toast.makeText(BreakpointDownloadService.this, "Downloading...", Toast.LENGTH_SHORT).show();


        }

        /**
         * 暂停下载
         */
        public void pauseDownload() {
            if (downloadTask != null) {
                downloadTask.pauseDownload();
            }
        }

        /**
         * 取消下载
         */
        public void cancelDownload() {
            if (downloadTask != null) {
                downloadTask.cancelDownload();
            } else {
                if (downloadUrl != null) {
                    //取消下载时需要将文件删除，并将通知关闭
                    String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                    String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                    File file = new File(directory + fileName);
                    if (file.exists()) {
                        file.delete();
                    }
                    //  getNotificationManager().cancel(1);
                    Log.i("DownloadService", "progress = canceled Download ");
                    stopForeground(true);
                    //  Toast.makeText(BreakpointDownloadService.this, "Canceled", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

    private void post(DownloadReqBean reqBean) {
        EventBus.core.emit("BreakpointDownloadService", reqBean);

    }

    /**
     * 下载状态
     */
    public enum DownloadStatus {
        DOWNLOAD_START,
        DOWNLOAD_PROGRESS,
        DOWNLOAD_SUCCESS,
        DOWNLOAD_FAILED,
        DOWNLOAD_PAUSED,
        DOWNLOAD_CANCELED,

    }
}
