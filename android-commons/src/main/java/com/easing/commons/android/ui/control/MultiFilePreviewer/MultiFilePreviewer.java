package com.easing.commons.android.ui.control.MultiFilePreviewer;

import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.data.DataCenter;
import com.easing.commons.android.ui.control.attach_viewer.Attach;

import java.util.List;

public class MultiFilePreviewer {

    public static void preview(List<String> fileList, int index) {
        DataCenter.set("fileList", fileList);
        DataCenter.set("selectedIndex", index);
        CommonApplication.startActivity(FilePreviewActivity.class);
    }

    public static void previewUrl(List<Attach> fileList, int index) {
        DataCenter.set("fileDataList", fileList);
        DataCenter.set("selectedIndex", index);
        CommonApplication.startActivity(FilePreviewActivity.class);
    }
}
