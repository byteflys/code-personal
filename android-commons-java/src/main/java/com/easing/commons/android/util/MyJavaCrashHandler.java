package com.easing.commons.android.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import com.easing.commons.android.time.Times;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Java 异常捕获
 */
public class MyJavaCrashHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Log.e("程序出现异常了", "Thread = " + t.getName() + "\nThrowable = " + e.getMessage());
        String stackTraceInfo = getStackTraceInfo(e);
        Log.e("stackTraceInfo", stackTraceInfo);
        saveThrowableMessage(stackTraceInfo);
    }

    /**
     * 获取错误的信息
     *
     * @param throwable
     * @return
     */
    private String getStackTraceInfo(final Throwable throwable) {
        PrintWriter pw = null;
        Writer writer = new StringWriter();
        try {
            pw = new PrintWriter(writer);
            throwable.printStackTrace(pw);
        } catch (Exception e) {
            return "";
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
        return writer.toString();
    }

    @SuppressLint("SdCardPath")
    private String logFilePath = "/sdcard/AoDunCrashLog";

    private void saveThrowableMessage(String errorMessage) {
        if (TextUtils.isEmpty(errorMessage)) {
            return;
        }

        //  String logFilePath = "sdcard/Crashlog" + Times.now();

        File file = new File(logFilePath);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            if (mkdirs) {
                writeStringToFile(errorMessage, file);
            }
        } else {
            writeStringToFile(errorMessage, file);
        }
    }

    private void writeStringToFile(final String errorMessage, final File file) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileOutputStream outputStream = null;
                try {
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(errorMessage.getBytes());
                    outputStream = new FileOutputStream(new File(file, Times.now() + ".txt"));
                    int len = 0;
                    byte[] bytes = new byte[1024];
                    while ((len = inputStream.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, len);
                    }
                    outputStream.flush();
                    Log.e("程序出异常了", "写入本地文件成功：" + file.getAbsolutePath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

}
