package com.easing.commons.android.MediaPlayer;

import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.easing.commons.android.MediaCodec.callback.SurfaceHolderCallback;
import com.easing.commons.android.helper.callback.Action;

import java.nio.ByteBuffer;

import lombok.SneakyThrows;

public class H264PlayView extends SurfaceView {

    MediaCodec mediaCodec;
    int frameIndex;

    Action onInitialized;

    public H264PlayView(Context context) {
        this(context, null);
    }

    public H264PlayView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    //控件初始化
    public void init(Context context, AttributeSet attributeSet) {
        getHolder().addCallback(new SurfaceHolderCallback() {
            @Override
            @SneakyThrows
            public void surfaceCreated(SurfaceHolder holder) {
                //初始化H264解码器
                mediaCodec = MediaCodec.createDecoderByType("video/avc");
                MediaFormat mediaFormat = MediaFormat.createVideoFormat("video/avc", 1280, 720);
                mediaCodec.configure(mediaFormat, getHolder().getSurface(), null, 0);
                mediaCodec.start();
                //执行初始化回调
                if (onInitialized != null)
                    onInitialized.runAndPostException();
            }
        });
    }

    //解码帧数据到Surface
    public boolean decodeFrame(byte[] buffer, int offset, int length) {

        // get input buffer index
        ByteBuffer[] inputBuffers = mediaCodec.getInputBuffers();
        int inputBufferIndex = mediaCodec.dequeueInputBuffer(100);
        if (inputBufferIndex < 0) return false;
        ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
        inputBuffer.clear();
        inputBuffer.put(buffer, offset, length);
        mediaCodec.queueInputBuffer(inputBufferIndex, 0, length, frameIndex * 30, 0);
        frameIndex++;

        // get output buffer index
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        int outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 100);
        while (outputBufferIndex >= 0) {
            mediaCodec.releaseOutputBuffer(outputBufferIndex, true);
            outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 0);
        }
        return true;
    }

    //设置控件回调
    public H264PlayView setCallback(Action onInitialized) {
        this.onInitialized = onInitialized;
        return this;
    }

}
