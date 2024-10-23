package com.easing.commons.android.media;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;

import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.manager.Services;

import lombok.SneakyThrows;

@SuppressWarnings("all")
public class MediaManager {

    public static final String ASSET_COMING_MESSAGE = "mp3/new_message_01.mp3";

    public static final int SCENE_CALL = 1;
    public static final int SCENE_MUSIC = 2;
    public static final int SCENE_MOVIE = 3;
    public static final int SCENE_VIDEO_CHAT = 4;
    public static final int SCENE_NOTIFICATION = 5;

    //播放
    @SneakyThrows
    public static void play(MediaPlayer player, String path) {
        player.setDataSource(path);
        player.prepare();
        player.start();
    }

    //停止
    @SneakyThrows
    public static void stop(MediaPlayer player) {
        if (player.isPlaying())
            player.stop();
    }

    //获取音视频时长
    @SneakyThrows
    public static int getMediaDuration(String path) {
        MediaPlayer player = new MediaPlayer();
        player.setDataSource(path);
        player.prepare();
        Integer duration = player.getDuration();
        player.release();
        return duration;
    }

    //播放本地文件
    @SneakyThrows
    public static void playFile(String path) {
        MediaPlayer player = new MediaPlayer();
        player.setDataSource(path);
        player.prepare();
        player.start();
    }

    //播放Asset资源
    @SneakyThrows
    public static void playAsset(Context context, String name) {
        MediaPlayer player = new MediaPlayer();
        AssetManager manager = context.getAssets();
        AssetFileDescriptor descriptor = manager.openFd(name);
        player.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getStartOffset());
        player.prepare();
        player.start();
    }

    //自动根据场景选择播放设备
    public static void autoSelectPlayDevice(Context context, int scene) {
        AudioManager audioManager = Services.getAudioManager(context);
        if (scene == SCENE_CALL)
            audioManager.setMode(AudioManager.MODE_IN_CALL);
        if (scene == SCENE_MUSIC || scene == SCENE_MOVIE || scene == SCENE_VIDEO_CHAT)
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        if (scene == SCENE_NOTIFICATION)
            audioManager.setMode(AudioManager.MODE_NORMAL);
        if (audioManager.isBluetoothA2dpOn()) {
            audioManager.setBluetoothScoOn(true);
            audioManager.setSpeakerphoneOn(false);
            audioManager.startBluetoothSco();
        } else if (audioManager.isWiredHeadsetOn()) {
            audioManager.setWiredHeadsetOn(true);
            audioManager.setSpeakerphoneOn(false);
        } else {
            if (scene == SCENE_CALL)
                audioManager.setSpeakerphoneOn(false);
            else
                audioManager.setSpeakerphoneOn(true);
        }
    }

    //使用外放播音
    public static void useSpeaker(Context context) {
        AudioManager audioManager = Services.getAudioManager(context);
        audioManager.setSpeakerphoneOn(true);
    }

    //使用听筒播音
    public static void useReceiver(Context context) {
        AudioManager audioManager = Services.getAudioManager(context);
        audioManager.setSpeakerphoneOn(false);
    }

    //使用耳机播音
    public static void useHeadset(Context context) {
        AudioManager audioManager = Services.getAudioManager(context);
        audioManager.setWiredHeadsetOn(true);
    }

    //使用蓝牙播音
    public static void useBluetooth(Context context) {
        AudioManager audioManager = Services.getAudioManager(context);
        audioManager.setBluetoothScoOn(true);
    }

    //开启麦克风
    public static void openMicrophone(Context context) {
        AudioManager audioManager = Services.getAudioManager(context);
        audioManager.setMicrophoneMute(false);
    }

    //禁用麦克风
    public static void closeMicrophone(Context context) {
        AudioManager audioManager = Services.getAudioManager(context);
        audioManager.setMicrophoneMute(true);
    }

    //静音
    public static void mute(Context context, boolean willMute) {
        AudioManager audioManager = Services.getAudioManager(context);
        audioManager.setRingerMode(willMute ? AudioManager.RINGER_MODE_SILENT : AudioManager.RINGER_MODE_NORMAL);
        audioManager.getStreamVolume(AudioManager.STREAM_RING);
    }

    //更新媒体库
    public static void updateMediaLibrary(String newMediaFile) {
        MediaScannerConnection.scanFile(CommonApplication.ctx, new String[]{newMediaFile}, null, null);
    }
}
