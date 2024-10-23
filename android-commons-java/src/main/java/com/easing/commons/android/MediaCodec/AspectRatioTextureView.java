package com.easing.commons.android.MediaCodec;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;

@SuppressWarnings("all")
public class AspectRatioTextureView extends TextureView {

    Surface surface;

    Double ratio;

    public AspectRatioTextureView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    protected void init(Context context, AttributeSet attributeSet) {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (ratio == null) {
            setMeasuredDimension(width, height);
            return;
        }
        if (width < height * ratio)
            setMeasuredDimension(width, (int) (width / ratio));
        else
            setMeasuredDimension((int) (height * ratio), height);
    }

    //设置宽高比
    public void setAspectRatio(int previewWidth, int previewHeight) {
        ratio = (double) previewWidth / (double) previewHeight;
        requestLayout();
    }

    //不调整宽高比，按实际布局决定视频尺寸
    public void resetAspectRatio() {
        ratio = null;
    }

    //设置预览大小
    //此方法必须在TextureView初始化完毕，SurfaceTexture不为空时执行
    public void setPreviewSize(int previewWidth, int previewHeight) {
        SurfaceTexture texture = getSurfaceTexture();
        texture.setDefaultBufferSize(previewWidth, previewHeight);
        surface = new Surface(texture);
    }

    //获得SurfaceTexture持有的Surface
    public Surface getSurface() {
        return surface;
    }

}
