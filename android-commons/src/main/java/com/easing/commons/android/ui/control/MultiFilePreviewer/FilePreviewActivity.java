package com.easing.commons.android.ui.control.MultiFilePreviewer;

import android.widget.FrameLayout;

import com.easing.commons.android.R;
import com.easing.commons.android.R2;
import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.data.DataCenter;
import com.easing.commons.android.ui.control.attach_viewer.Attach;
import com.easing.commons.android.ui.control.scroll.HorizontalScroller;
import com.easing.commons.android.ui.control.title_bar.TitleBar;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * 多功能文件预览
 */
public class FilePreviewActivity extends CommonActivity<FilePreviewActivity> {

    public static FilePreviewCallback callback = new FilePreviewCallback() {
    };

    public static UrlFilePreviewCallback callbackUrl = new UrlFilePreviewCallback() {
    };

    @BindView(R2.id.title_bar)
    TitleBar titleBar;

    @BindView(R2.id.scroller)
    HorizontalScroller<Object, FrameLayout> scroller;

    List<Object> fileList = DataCenter.get("fileList");
    List<Object> fileDataList = DataCenter.get("fileDataList");
    int selectedIndex = DataCenter.get("selectedIndex");

    @Override
    protected boolean beforeCreate() {
        statusBarColor = R.color.color_transparent;
        navigationBarColor = R.color.color_black_10;
        return super.beforeCreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (fileList != null)
            DataCenter.remove("fileList");
        if (fileDataList != null)
            DataCenter.remove("fileDataList");
    }

    @Override
    protected void create() {
        setContentView(R.layout.md001_multi_file_preview_activity);

        //设置标题栏
        titleBar.setTitleText("查看文件");
        titleBar.showReturnButton(false);
        titleBar.setBackgroundResource(R.drawable.color_black_10);

        //添加文件列表
        postLater(this::addFileList, 500);
    }

    //添加文件列表
    protected void addFileList() {

        //根据数据创建Item视图
        scroller.viewMapper(data -> {
            FrameLayout layout = new FrameLayout(ctx);
            return layout;
        });
        //Item滑入
        scroller.onSlideIn((data, frameLayout) -> {


            if (fileList != null)
                callback.addItem(ctx, (String) data, frameLayout);
            else if (fileDataList != null)
                callbackUrl.addItem(ctx, (Attach) data, frameLayout);
        });
        //Item滑出
        scroller.onSlideOut((data, frameLayout) -> {


            if (fileList != null)
                callback.disposeItem(ctx, (String) data, frameLayout);
            else if (fileDataList != null)
                callbackUrl.disposeItem(ctx, (Attach) data, frameLayout);

        });
        //Item被选中
        scroller.onItemSelect((data, frameLayout) -> {

            if (fileList != null) {
                String title = callback.getTitle((String) data);
                if (title != null)
                    titleBar.setTitleText(title);
                callback.selectItem(ctx, (String) data, frameLayout);
            } else if (fileDataList != null) {
                String title = callbackUrl.getTitle((Attach) data);
                if (title != null)
                    titleBar.setTitleText(title);
                callbackUrl.selectItem(ctx, (Attach) data, frameLayout);
            }

        });
        //Item被点击
        scroller.onItemClick((data, frameLayout) -> {

        });
        //内容为空时，Scroller被点击
        scroller.onParentClick(() -> {

        });
        //滑动停止时，自动调整控件居中
        scroller.autoAdjustToCenter(true);
        //指定初始位置
        scroller.firstSelection(selectedIndex);
        //添加数据
        if (fileList != null)
            scroller.addAll(fileList);
        if (fileDataList != null)
            scroller.addAll(fileDataList);
    }
}

