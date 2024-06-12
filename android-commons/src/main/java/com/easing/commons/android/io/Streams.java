package com.easing.commons.android.io;

import com.easing.commons.android.code.Console;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import lombok.SneakyThrows;

public class Streams {

    //关闭流
    @SneakyThrows
    public static void close(InputStream is) {
        if (is != null)
            is.close();
    }

    //关闭流
    @SneakyThrows
    public static void close(OutputStream os) {
        if (os != null)
            os.close();
    }

    //关闭流
    @SneakyThrows
    public static void close(InputStream is, OutputStream os) {
        if (os != null)
            os.close();
        if (is != null)
            is.close();
    }

    //输入流转字符串
    @SneakyThrows
    public static String streamToString(InputStream is, String encode) {
        if (encode == null)
            encode = "UTF-8";
        byte[] buffer = new byte[is.available()];
        is.read(buffer);
        Streams.close(is);
        return new String(buffer, encode);
    }

    //输入流转字符串
    @SneakyThrows
    public static byte[] streamToByteArray(InputStream is) {
        byte[] buffer = new byte[is.available()];
        is.read(buffer);
        Streams.close(is, null);
        return buffer;
    }

    //读取文件字节
    @SneakyThrows
    public static byte[] bytesFromFile(String path) {
        try {
            FileInputStream is = new FileInputStream(path);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return buffer;
        } catch (Throwable e) {
            Console.error(e);
            return null;
        }
    }

    //读取文件中存储的文本
    @SneakyThrows
    public static String stringFromFile(String path) {
        byte[] bytes = bytesFromFile(path);
        return new String(bytes);
    }

    //将输入流的数据拷贝到输出流
    @SneakyThrows
    public static void write(InputStream is, OutputStream os) {
        byte[] buffer = new byte[1024 * 1024];
        while (true) {
            int len = is.read(buffer);
            if (len < 0) break;
            os.write(buffer, 0, len);
        }
        os.flush();
        is.close();
        os.close();
    }
}
