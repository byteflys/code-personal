package com.easing.commons.android.MediaPlayer;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

import com.easing.commons.android.app.CommonApplication;

import lombok.SneakyThrows;

public class AudioPlayer {

    //音频播放器
    protected static final MediaPlayer player = new MediaPlayer();

    //播放音频文件
    @SneakyThrows
    public static void playFile(String file) {
        player.reset();
        player.setDataSource(file);
        player.prepare();
        player.setOnPreparedListener(mp -> {
            player.start();
        });
    }

    //播放音频资源
    @SneakyThrows
    public static void playAsset(String asset) {
        AssetManager manager = CommonApplication.ctx.getAssets();
        AssetFileDescriptor fd = manager.openFd(asset);
        player.reset();
        player.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getDeclaredLength());
        player.prepare();
        player.setOnPreparedListener(mp -> {
            player.start();
        });
    }
}

