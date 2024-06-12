package com.easing.commons.android.audio;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.easing.commons.android.thread.Threads;
import com.easing.commons.android.thread.WorkThread;

import java.util.Arrays;

import lombok.Getter;

@SuppressWarnings("all")
public class PcmRecorder {

    AudioRecord audioRecord;
    OnAudioRecord callback;

    @Getter
    State state = State.INIT;

    //一次读取的缓冲区大小
    int defaultBufferSize = 512;
    int bufferSize = defaultBufferSize;

    //采样率
    int sampleRate = 44100;
    //通道数
    int audioChannel = AudioFormat.CHANNEL_IN_FRONT;
    //比特率
    int audioFormat = AudioFormat.ENCODING_PCM_16BIT;

    public enum State {
        INIT, RECORDING, COMPLETE
    }

    public static PcmRecorder create() {
        PcmRecorder recorder = new PcmRecorder();
        return recorder;
    }

    public PcmRecorder() {
        bufferSize = AudioRecord.getMinBufferSize(sampleRate, audioChannel, audioFormat);
        bufferSize = bufferSize > defaultBufferSize ? defaultBufferSize : bufferSize;
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, audioChannel, audioFormat, bufferSize);
        WorkThread.post(() -> {
            while (state == State.INIT)
                Threads.sleep(100);
            byte[] buffer = new byte[bufferSize];
            audioRecord.startRecording();
            while (state == State.RECORDING) {
                int length = audioRecord.read(buffer, 0, bufferSize);
                if (callback != null) {
                    byte[] data = Arrays.copyOf(buffer, length);
                    callback.onAudioRecord(data);
                }
            }
            audioRecord.stop();
        });
    }

    public PcmRecorder onAudioRecord(OnAudioRecord callback) {
        this.callback = callback;
        return this;
    }

    public PcmRecorder start() {
        state = State.RECORDING;
        return this;
    }

    public PcmRecorder stop() {
        state = State.COMPLETE;
        callback = null;
        return this;
    }

    public interface OnAudioRecord {

        void onAudioRecord(byte[] data);
    }
}
