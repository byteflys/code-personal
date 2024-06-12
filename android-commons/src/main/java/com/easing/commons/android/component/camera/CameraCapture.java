package com.easing.commons.android.component.camera;

import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.data.DataCenter;

public class CameraCapture {

    public static void start(Integer requestCode, boolean singleMode) {
        DataCenter.set("CameraCaptureActivity.requestCode", requestCode);
        DataCenter.set("CameraCaptureActivity.singleMode", singleMode);
        CommonApplication.startActivity(CameraCaptureActivity.class);
    }
}
