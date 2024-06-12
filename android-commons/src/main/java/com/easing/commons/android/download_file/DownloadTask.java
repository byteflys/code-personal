package com.easing.commons.android.download_file;

import static com.easing.commons.android.format.Maths.rahToStr;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import com.blankj.utilcode.util.LogUtils;
import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.format.Texts;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * String 在执行AsyncTask时需要传入的参数，可用于在后台任务中使用。
 * Integer 后台任务执行时，如果需要在界面上显示当前的进度，则使用这里指定的泛型作为进度单位。
 * Integer 当任务执行完毕后，如果需要对结果进行返回，则使用这里指定的泛型作为返回值类型。
 */
public class DownloadTask extends AsyncTask<String, String, String[]> {

    public static final String TYPE_SUCCESS = "0";

    public static final String TYPE_FAILED = "1";

    public static final String TYPE_PAUSED = "2";

    public static final String TYPE_CANCELED = "3";

    private DownloadListener listener;

    private boolean isCanceled = false;

    private boolean isPaused = false;

    private String lastProgress;


    public DownloadTask(DownloadListener listener) {
        this.listener = listener;
    }

    /**
     * 这个方法中的所有代码都会在子线程中运行，我们应该在这里处理所有的耗时任务。
     *
     * @param params
     * @return
     */
    @Override
    protected String[] doInBackground(String... params) {
        InputStream is = null;
        RandomAccessFile savedFile = null;


        //downloadTask.execute(downloadUrl, outPath, fileName, dowTag);

        File file = null;
        long downloadLength = 0;   //记录已经下载的文件长度
        //文件下载地址
        String downloadUrl = params[0];

        //下载文件的名称
        //下载文件存放的目录
        String directory = params[1];

        String fileName = params[2];

        if (Texts.isEmpty(fileName))
            downloadUrl.substring(downloadUrl.lastIndexOf("="));


        File direFile = new File(directory);

        if (!direFile.exists())
            direFile.mkdirs(); //新建文件夹

        //创建一个文件
        file = new File(directory + "/" + fileName);
        if (file.exists()) {
            //如果文件存在的话，得到文件的大小
            downloadLength = file.length();
        } else {
            try {
                file.createNewFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        //得到下载内容的大小
        long contentLength = getContentLength(downloadUrl);

        LogUtils.i("progress --contentLength = " + contentLength + "  downloadLength = " + downloadLength);
        if (contentLength == 0) {
            return new String[]{TYPE_FAILED, params[3]};
        } else if (contentLength <= downloadLength) {
            //已下载字节和文件总字节相等，说明已经下载完成了
            return new String[]{TYPE_SUCCESS, params[3], file.getPath()};
        }
        OkHttpClient client = new OkHttpClient();
        /**
         * HTTP请求是有一个Header的，里面有个Range属性是定义下载区域的，它接收的值是一个区间范围，
         * 比如：Range:bytes=0-10000。这样我们就可以按照一定的规则，将一个大文件拆分为若干很小的部分，
         * 然后分批次的下载，每个小块下载完成之后，再合并到文件中；这样即使下载中断了，重新下载时，
         * 也可以通过文件的字节长度来判断下载的起始点，然后重启断点续传的过程，直到最后完成下载过程。
         */
        Request request = new Request.Builder()

                //.addHeader("RANGE", "bytes=" + downloadLength + "-")  //断点续传要用到的，指示下载的区间
                .addHeader("RANGE", downloadLength + "")  //断点续传要用到的，指示下载的区间
                //.addHeader("Accept-Encoding", "identity")
                .url(downloadUrl)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response != null) {
                is = response.body().byteStream();
                savedFile = new RandomAccessFile(file, "rw");
                savedFile.seek(downloadLength);//跳过已经下载的字节
                byte[] b = new byte[1024];
                long total = 0;
                int len;
                while ((len = is.read(b)) != -1) {
                    if (isCanceled) {
                        return new String[]{TYPE_CANCELED, params[3]};
                    } else if (isPaused) {
                        return new String[]{TYPE_PAUSED, params[3]};
                    } else {
                        total += len;
                        savedFile.write(b, 0, len);
                        //计算已经下载的百分比
                        double progress = ((Double.valueOf((total + downloadLength)) / contentLength) * 100d);
                        //注意：在doInBackground()中是不可以进行UI操作的，如果需要更新UI,比如说反馈当前任务的执行进度，
                        //可以调用publishProgress()方法完成。

                        if (progress > 99.99)
                            progress = 99.99;
                        String progre;
                        if (progress != 0) {
                            progre = rahToStr(progress);
                        } else {
                            progre = "0.00";
                        }
                        LogUtils.i("progress = " + progress + "   total =" + total);
                        publishProgress(progre, params[3]);
                    }

                }
                response.body().close();
                return new String[]{TYPE_SUCCESS, params[3], file.getPath()};
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (savedFile != null) {
                    savedFile.close();
                }
                if (isCanceled && file != null) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new String[]{TYPE_FAILED, params[3]};
    }

    /**
     * 当在后台任务中调用了publishProgress(Progress...)方法之后，onProgressUpdate()方法
     * 就会很快被调用，该方法中携带的参数就是在后台任务中传递过来的。在这个方法中可以对UI进行操作，利用参数中的数值就可以
     * 对界面进行相应的更新。
     *
     * @param values
     */
    protected void onProgressUpdate(String... values) {
        String progress = values[0];
        //  if (Integer.parseInt(progress) > Integer.parseInt(lastProgress)) {
        if (listener != null)
            listener.onProgress(values[1], progress);
        lastProgress = progress;
        //  }
    }

    /**
     * 当后台任务执行完毕并通过Return语句进行返回时，这个方法就很快被调用。返回的数据会作为参数
     * 传递到此方法中，可以利用返回的数据来进行一些UI操作。
     *
     * @param status
     */
    @Override
    protected void onPostExecute(String[] status) {
        switch (status[0]) {
            case TYPE_SUCCESS: //下载成功后更新文件
                BreakpointDownloadService.isDownload = false;
                CommonApplication.currentActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(status[2]))));
                if (listener != null)
                    listener.onSuccess(status[1], status[2]);
                break;
            case TYPE_FAILED:
                BreakpointDownloadService.isDownload = false;
                if (listener != null)
                    listener.onFailed(status[1]);
                break;
            case TYPE_PAUSED:
                if (listener != null)
                    listener.onPaused(status[1]);
                break;
            case TYPE_CANCELED:
                BreakpointDownloadService.isDownload = false;
                if (listener != null)
                    listener.onCanceled(status[1]);
                break;
            default:
                break;
        }
    }

    public void pauseDownload() {
        isPaused = true;
    }

    public void cancelDownload() {
        isCanceled = true;
    }

    /**
     * 得到下载内容的大小
     *
     * @param downloadUrl
     * @return
     */
    private long getContentLength(String downloadUrl) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .addHeader("Accept-Encoding", "identity")
                .url(downloadUrl)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                long contentLength = response.body().contentLength();
                response.body().close();
                return contentLength;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
