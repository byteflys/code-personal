package com.easing.commons.android.ui.control.attach_viewer;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.easing.commons.android.R;
import com.easing.commons.android.io.Files;
import com.easing.commons.android.media.MediaType;
import com.easing.commons.android.view.Views;

import lombok.Getter;

public class AttachViewer extends FrameLayout {

    Context context;
    Handler handler = new Handler();

    @Getter
    String path;
    @Getter
    String name;
    String url;
    String fileType;

    ImageView imageView;
    TextView textView;

    public AttachViewer(Context context, String path, String name) {
        super(context);
        init(context, null, path, name);
    }

    public AttachViewer(Context context) {
        super(context);
        init(context, null, null, null);
    }

    public AttachViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, null, null);
    }

    public AttachViewer(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        init(context, attrs, null, null);
    }

    private void init(Context context, AttributeSet attributeSet, String path, String name) {
        this.context = context;
        View root = Views.inflate(context, R.layout.layout_attach_viewer);
        imageView = Views.findView(root, R.id.image);
        textView = Views.findView(root, R.id.text);
        super.addView(root, new FrameLayout.LayoutParams(Views.MATCH_PARENT, Views.MATCH_PARENT, Gravity.CENTER));

        //默认显示内容
        initContent();
    }

    //延迟加载内容
    public void loadLater(Attach attach/*String path, String name, String fileType*/, long ms) {
        handler.postDelayed(() -> load(attach), ms);
    }

    //加载内容
    public void load(Attach attach/*,String path, String name, String fileType*/) {
        this.path = attach.file;
        this.name = attach.name;
        this.fileType = attach.type;
        this.url = attach.url;
        textView.setText(name);

        //缩放模式与边距
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(0, 0, 0, 0);

        //网络图片
        if (MediaType.isWebResource(url) && fileType != null) {

            if (fileType.contains("video")) {
                Glide.with(context).asBitmap().load(R.drawable.image_file_type_m01_video).into(imageView);

            } else if (fileType.contains("audio")) {
                Glide.with(context).asBitmap().load(R.drawable.image_file_type_m01_audio).into(imageView);

            } else if (fileType.contains("image")) {
                Glide.with(context).asBitmap().load(url).into(imageView);

            } else {
                Glide.with(context).asBitmap().load(R.drawable.image_file_type_m01_unknown).into(imageView);

            }

            //文件不存在
        } else if (!Files.exist(path))
            textView.setText("loading...");

            //图片文件
        else if (MediaType.isImage(name)||MediaType.isImage(path))
            Glide.with(context).asBitmap().load(path).into(imageView);

            //音频文件
        else if (MediaType.isAudio(name)||MediaType.isAudio(path))
            Glide.with(context).asBitmap().load(R.drawable.image_file_type_m01_audio).into(imageView);

            //视频文件
        else if (MediaType.isVideo(name)||MediaType.isVideo(path))
            Glide.with(context).asBitmap().load(R.drawable.image_file_type_m01_video).into(imageView);

            //文本文件
        else if (MediaType.isText(name)||MediaType.isText(path))
            Glide.with(context).asBitmap().load(R.drawable.image_file_type_m01_text).into(imageView);

            //文档文件
        else if (MediaType.isDocument(name)||MediaType.isDocument(path))
            Glide.with(context).asBitmap().load(R.drawable.image_file_type_m01_doc).into(imageView);

            //未知资源
        else {
            textView.setText("unknown type");
            Glide.with(context).asBitmap().load(R.drawable.image_file_type_m01_unknown).into(imageView);
        }
    }

    //默认显示内容
    public void initContent() {
        Glide.with(context).asGif().load(R.drawable.gif_loading).into(imageView);
        textView.setText("loading");
    }

    //是否显示文件名
    public void showName(boolean showName) {
        if (showName)
            textView.setVisibility(View.VISIBLE);
        else
            textView.setVisibility(View.GONE);
    }
}
