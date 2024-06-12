package com.easing.commons.android.MediaCodec;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import lombok.Getter;
import lombok.Setter;

//带初始化监听的SurfaceView
public class StateSurfaceView extends SurfaceView {

    @Getter
    boolean initialized = false;

    @Setter
    SurfaceViewStateCallback callback;

    public StateSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                initialized = true;
                if (callback != null) callback.onSurfaceCreated(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if (callback != null) callback.onSurfaceChanged(holder, format, width, height);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                initialized = false;
                if (callback != null) callback.onSurfaceDestroyed(holder);
            }
        });
    }

    public interface SurfaceViewStateCallback {

        default void onSurfaceCreated(SurfaceHolder holder) {}

        default void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

        default void onSurfaceDestroyed(SurfaceHolder holder) {}
    }

}
