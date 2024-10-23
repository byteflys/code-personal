package com.easing.commons.android.util;

import com.easing.commons.android.MediaPlayer.AudioPlayer;
import com.easing.commons.android.manager.Services;
import com.easing.commons.android.thread.SingleThreadPool;

/**
 * 振动加提示音
 */
public class AudioPlayerUtils {

    public static final SingleThreadPool singleThreadPool = SingleThreadPool.newInstance(5);

    //新消息提示
    public static void playNewMessageComing() {
        singleThreadPool.submit(() -> {
            AudioPlayer.playAsset("mp3/new_message_02.mp3");
            Services.vibrate(2);
        });
    }
}
