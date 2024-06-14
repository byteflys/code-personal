package com.easing.commons.android.MediaCodec;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;

import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.code.Console;
import com.easing.commons.android.manager.Device;
import com.easing.commons.android.struct.Collections;
import com.easing.commons.android.value.measure.Size;

import java.util.List;

import lombok.SneakyThrows;

//摄像头管理类
@SuppressWarnings("all")
public class Cameras {

    //停止相机
    public static void stopBackCamera() {
        Camera camera = openBackCamera();
        camera.stopPreview();
    }

    //打卡正面相机
    public static Camera openFrontCamera() {
        try {
            int cameraCount = Camera.getNumberOfCameras();
            for (int i = 0; i < cameraCount; i++) {
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(i, info);
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) return Camera.open(i);
            }
        } catch (Exception e) {
            Console.error(e);
        }
        return null;
    }

    //打卡背面相机
    public static Camera openBackCamera() {
        try {
            int cameraCount = Camera.getNumberOfCameras();
            for (int i = 0; i < cameraCount; i++) {
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(i, info);
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) return Camera.open(i);
            }
        } catch (Exception e) {
            Console.error(e);
        }
        return null;
    }

    //获取摄像头支持的最小尺寸
    public static Camera.Size getMinSupportedVideoSize(Camera camera, int minSize) {
        List<Camera.Size> supportedVideoSizes = camera.getParameters().getSupportedVideoSizes();
        Collections.sort(supportedVideoSizes, (l, r) -> l.width * l.height - r.width * r.height);
        for (Camera.Size size : supportedVideoSizes)
            if (size.width * size.height >= minSize)
                return size;
        return null;
    }

    //获取前置摄像头id
    @SneakyThrows
    public static String getFrontCameraId() {
        CameraManager cameraManager = CommonApplication.ctx.getSystemService(CameraManager.class);
        for (String cameraId : cameraManager.getCameraIdList()) {
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
            Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
            if (facing == CameraCharacteristics.LENS_FACING_FRONT) return cameraId;
        }
        return null;
    }

    //获取后置摄像头id
    @SneakyThrows
    public static String getBackCameraId() {
        CameraManager cameraManager = CommonApplication.ctx.getSystemService(CameraManager.class);
        for (String cameraId : cameraManager.getCameraIdList()) {
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
            Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
            if (facing == CameraCharacteristics.LENS_FACING_BACK) return cameraId;
        }
        return null;
    }

    //获取外挂摄像头id
    @SneakyThrows
    public static String getExternalCameraId() {
        CameraManager cameraManager = CommonApplication.ctx.getSystemService(CameraManager.class);
        for (String cameraId : cameraManager.getCameraIdList()) {
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
            Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
            if (facing == CameraCharacteristics.LENS_FACING_EXTERNAL) return cameraId;
        }
        return null;
    }

    //获取最佳尺寸，即和全屏比例最近的尺寸
    @SneakyThrows
    public static Size getBestSize(String cameraId) {
        Size size = Device.screenSize();
        double ratio = size.w > size.h ? size.w / 1.0d / size.h : size.h / 1.0d / size.w;

        CameraManager cameraManager = CommonApplication.ctx.getSystemService(CameraManager.class);
        CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
        StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        android.util.Size[] outputSizes = map.getOutputSizes(ImageFormat.JPEG);
        List<android.util.Size> sizes = Collections.asList(outputSizes);
        Collections.sort(sizes, (a, b) -> {
            double dRatio1 = a.getWidth() / 1.0d / a.getHeight() - ratio;
            double dRatio2 = b.getWidth() / 1.0d / b.getHeight() - ratio;
            if (Math.abs(dRatio1) < Math.abs(dRatio2)) return -1;
            if (Math.abs(dRatio1) > Math.abs(dRatio2)) return 1;
            return 0;
        });
        android.util.Size bestSize = sizes.get(0);
        return new Size(bestSize.getWidth(), bestSize.getHeight());
    }
}
