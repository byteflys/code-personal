package com.easing.commons.android.ui_component.attach_selector;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.easing.commons.android.R;
import com.easing.commons.android.R2;
import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.event.EventBus;
import com.easing.commons.android.event.OnEvent;
import com.easing.commons.android.format.Maths;
import com.easing.commons.android.media.MediaType;
import com.easing.commons.android.value.identity.Codes;
import com.easing.commons.android.view.Views;

import butterknife.BindView;

/**
 * 附件选择
 */
@SuppressWarnings("all")
public class AttachSelector extends FrameLayout {

    CommonActivity ctx;

    @BindView(R2.id.bt_pick_file)
    View pickFileButton;
    @BindView(R2.id.bt_take_photo)
    View takePhotoButton;
    @BindView(R2.id.bt_record_video)
    View recordVideoButton;
    @BindView(R2.id.bt_record_audio)
    View recordAudioButton;

    Integer requestCode;

    OnFileSelect onFileSelect;

    public AttachSelector(Context context) {
        this(context, null);
    }

    public AttachSelector(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public AttachSelector onFileSelect(OnFileSelect onFileSelect) {
        this.onFileSelect = onFileSelect;
        return this;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.core.subscribe(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        EventBus.core.unsubscribe(this);
        super.onDetachedFromWindow();
    }

    protected void init(Context context, AttributeSet attributeSet) {
        ctx = (CommonActivity) context;
        View root = Views.inflateAndAttach(ctx, R.layout.layout_attach_selector, this, true);
        Views.viewBinding(this, root);

        //选文件
        Views.onClick(pickFileButton, () -> {
            requestCode = Codes.randomCode();
            ctx.pickFile(requestCode, MediaType.TYPE_ALL);
        });

        //拍照
        Views.onClick(takePhotoButton, () -> {
            requestCode = Codes.randomCode();
            ctx.captureImage(requestCode, null);
        });

        //录像
        Views.onClick(recordVideoButton, () -> {
            requestCode = Codes.randomCode();
            ctx.captureVideo(requestCode, null);
        });

        //录音
        Views.onClick(recordAudioButton, () -> {
            requestCode = Codes.randomCode();
            ctx.captureAudio(requestCode, null);
        });
    }

    @OnEvent(type = "onFileSelect")
    public final void onFileSelect(String type, Integer requestCode, String path) {
        if (Maths.equal(requestCode, this.requestCode))
            if (onFileSelect != null)
                onFileSelect.onFileSelect(path);
    }

    public interface OnFileSelect {

        void onFileSelect(String path);
    }
}
