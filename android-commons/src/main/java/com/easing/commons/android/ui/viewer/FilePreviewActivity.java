package com.easing.commons.android.ui.viewer;

import android.net.Uri;

import com.easing.commons.android.R;
import com.easing.commons.android.R2;
import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.manager.Uris;
import com.easing.commons.android.media.MediaType;
import com.easing.commons.android.ui.control.title_bar.TitleBar;
import com.easing.commons.android.ui.dialog.MessageDialog;
import com.easing.commons.android.view.Views;

import butterknife.BindView;

public class FilePreviewActivity extends CommonActivity<FilePreviewActivity> {

    @BindView(R2.id.title_bar)
    TitleBar titleBar;

    @Override
    protected boolean beforeCreate() {
        statusBarColor = R.color.color_transparent;
        return super.beforeCreate();
    }

    @Override
    protected void create() {

        setContentView(R.layout.activity_text_viewer);
        Views.viewBinding(this, ctx);

        //设置标题栏
        titleBar.setTitleText("文件查看器");
        titleBar.showReturnButton(false);

        Uri uri = getIntent().getData();
        String path = Uris.uriToPath(uri);

        if (MediaType.isImage(path))
            ViewImageDialog.create(path).onClose(this::endProcess).show(ctx);
        else if (MediaType.isVideo(path))
            ViewVideoDialog.create(path).onClose(this::endProcess).show(ctx);
        else if (MediaType.isAudio(path))
            ViewAudioDialog.create(path).onClose(this::endProcess).show(ctx);
        else if (MediaType.isText(path))
            ViewTextDialog.create(path).onClose(this::endProcess).show(ctx);
        else
            MessageDialog.create(ctx).message("不支持的格式").onClose(this::endProcess).showWithoutIcon();
    }

    protected void endProcess() {
        finishAndRemoveTask();
    }

}
