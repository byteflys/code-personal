package com.easing.commons.android.format;

import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.io.Files;
import com.easing.commons.android.manager.Resources;

import lombok.SneakyThrows;

//简单加解密，用于满足一些简单需求
public class SimpleTextEncryptor {

    //加密
    @SneakyThrows
    public static byte[] encrypt(byte[] originBytes) {
        byte[] encryptBytes = new byte[originBytes.length];
        for (int i = 0; i < originBytes.length; i++)
            encryptBytes[i] = (byte) (originBytes[i] + 60);
        return encryptBytes;
    }

    //解密
    @SneakyThrows
    public static byte[] decrypt(byte[] encryptBytes) {
        byte[] originBytes = new byte[encryptBytes.length];
        for (int i = 0; i < encryptBytes.length; i++)
            originBytes[i] = (byte) (encryptBytes[i] - 60);
        return originBytes;
    }

    //加密文件
    //这个方法会将Asset文件内容加密后，存储到存储卡相同位置
    @SneakyThrows
    public static byte[] encryptAssetToSdcard(String path) {
        byte[] originBytes = Resources.readAssetBytes(CommonApplication.ctx, path);
        byte[] encryptBytes = encrypt(originBytes);
        Files.writeToFile("sdcard/" + path, encryptBytes);
        return encryptBytes;
    }

    //解密
    @SneakyThrows
    public static String decryptTextFromAsset(String path) {
        byte[] encryptBytes = Resources.readAssetBytes(CommonApplication.ctx, path);
        byte[] originBytes = decrypt(encryptBytes);
        String originText = new String(originBytes, "UTF-8");
        return originText;
    }
}
