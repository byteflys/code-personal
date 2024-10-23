package com.easing.commons.android.component.camera;

import android.graphics.ImageFormat;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;
import android.widget.ImageView;

import com.easing.commons.android.R;
import com.easing.commons.android.R2;
import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.data.DataCenter;
import com.easing.commons.android.event.EventBus;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.io.Files;
import com.easing.commons.android.drawable.Bitmaps;
import com.easing.commons.android.MediaCodec.AspectRatioTextureView;
import com.easing.commons.android.MediaCodec.Cameras;
import com.easing.commons.android.MediaCodec.callback.CameraStateCallback;
import com.easing.commons.android.MediaCodec.callback.CaptureSessionCallback;
import com.easing.commons.android.MediaCodec.callback.ImageAvailableListener;
import com.easing.commons.android.struct.Collections;
import com.easing.commons.android.thread.Threads;
import com.easing.commons.android.time.Times;
import com.easing.commons.android.ui.dialog.MessageDialog;
import com.easing.commons.android.value.measure.Size;
import com.easing.commons.android.view.Views;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import lombok.SneakyThrows;

@SuppressWarnings("all")
public class CameraCaptureActivity extends CommonActivity<CameraCaptureActivity> {

    @BindView(R2.id.texture_view)
    AspectRatioTextureView textureView;

    @BindView(R2.id.bt_close)
    ImageView closeButton;
    @BindView(R2.id.bt_switch)
    ImageView switchButton;
    @BindView(R2.id.bt_capture_image)
    ImageView imageCaptureButton;
    @BindView(R2.id.bt_capture_video)
    ImageView videoRecordButton;

    MessageDialog messageDialog = MessageDialog.create(this);

    final Integer requestCode = DataCenter.get("CameraCaptureActivity.requestCode");
    final Boolean singleMode = DataCenter.get("CameraCaptureActivity.singleMode");
    final List<String> capturedFiles = Collections.emptyList();

    Handler backgroundHandler;

    CameraDevice device;
    CameraCaptureSession session;
    CaptureRequest request;

    ImageReader imageReader;

    MediaRecorder mediaRecorder;
    String recordPath;

    Size previewSize;

    @Override
    protected boolean beforeCreate() {
        statusBarColor = R.color.color_transparent;
        navigationBarColor = R.color.color_transparent;
        return super.beforeCreate();
    }

    @Override
    @SneakyThrows
    protected void create() {
        setContentView(R.layout.activity_camera_capture);
        Views.viewBinding(this, ctx);

        //创建HandlerThread，在主线程处理任务
        HandlerThread backgroundThread = new HandlerThread("HandlerThread");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());

        //默认打开后置摄像头
        openBackCamera();

        //退出
        onClick(closeButton, () -> {
            finish();
        });

        //拍照
        onClick(imageCaptureButton, () -> {
            startImageCapture();
        });

        //录像
        onClick(videoRecordButton, () -> {
            if (mediaRecorder == null) {
                startVideoRecord();
                Views.tintColor(videoRecordButton, R.color.color_red);
                Views.visibility(closeButton, Views.GONE);
                Views.visibility(switchButton, Views.GONE);
            } else {
                stopVideoRecord();
                Views.removeTintColor(videoRecordButton);
                Views.visibility(closeButton, Views.VISIBLE);
                Views.visibility(switchButton, Views.VISIBLE);
            }
        });

        //切换摄像头
        onClick(switchButton, () -> {
            boolean isFront = Texts.equal(Cameras.getFrontCameraId(), device.getId());
            if (isFront)
                openBackCamera();
            else
                openFrontCamera();
        });
    }

    @Override
    protected void destroy() {
        //关闭已打开的设备
        if (device != null) {
            session.close();
            device.close();
            imageReader.close();
        }

        //删除未录制完的文件
        if (recordPath != null)
            Files.deleteFile(recordPath);

        //推送已拍摄的文件列表
        DataCenter.set("capturedFiles", capturedFiles);
        EventBus.core.emit("onCaptureResult", requestCode, capturedFiles);
    }

    //打开后置摄像头
    @SneakyThrows
    protected void openBackCamera() {

        //关闭已打开的设备
        if (device != null) {
            session.close();
            device.close();
        }

        //获取摄像头id和全屏预览尺寸
        String cameraId = Cameras.getBackCameraId();
        previewSize = Cameras.getBestSize(cameraId);

        //创建ImageReader，用于存储抓取的帧图像
        //CaptureRequest执行成功时，就会触发OnImageAvailable回调
        imageReader = ImageReader.newInstance(previewSize.w, previewSize.h, ImageFormat.JPEG, 1);
        imageReader.setOnImageAvailableListener(new ImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                writeOutImage(reader);
            }
        }, backgroundHandler);

        //打开摄像头
        CameraManager manager = ctx.getSystemService(CameraManager.class);
        manager.openCamera(cameraId, new CameraStateCallback() {
            @Override
            @SneakyThrows
            public void onOpened(CameraDevice device) {
                CameraCaptureActivity.this.device = device;

                //等待TextureView初始化完毕，再设置SurfaceTexture大小
                while (textureView.getSurfaceTexture() == null)
                    Threads.sleep(100);
                textureView.setPreviewSize(previewSize.w, previewSize.h);

                //默认打开摄像头后开始预览
                startVideoPreview();
            }
        }, backgroundHandler);
    }

    //打开前置摄像头
    @SneakyThrows
    protected void openFrontCamera() {

        //关闭已打开的设备
        if (device != null) {
            session.close();
            device.close();
        }

        //获取摄像头id和全屏预览尺寸
        String cameraId = Cameras.getFrontCameraId();
        previewSize = Cameras.getBestSize(cameraId);

        //创建ImageReader，用于存储抓取的帧图像
        //CaptureRequest执行成功时，就会触发OnImageAvailable回调
        imageReader = ImageReader.newInstance(previewSize.w, previewSize.h, ImageFormat.JPEG, 1);
        imageReader.setOnImageAvailableListener(new ImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                writeOutImage(reader);
            }
        }, backgroundHandler);

        //打开摄像头
        CameraManager manager = ctx.getSystemService(CameraManager.class);
        manager.openCamera(cameraId, new CameraStateCallback() {
            @Override
            @SneakyThrows
            public void onOpened(CameraDevice device) {
                CameraCaptureActivity.this.device = device;

                //等待TextureView初始化完毕，再设置SurfaceTexture大小
                while (textureView.getSurfaceTexture() == null)
                    Threads.sleep(100);
                textureView.setPreviewSize(previewSize.w, previewSize.h);

                //默认打开摄像头后开始预览
                startVideoPreview();
            }
        }, backgroundHandler);
    }

    //视频预览
    @SneakyThrows
    protected void startVideoPreview() {

        //停止其它会话
        if (session != null)
            session.close();

        //创建预览会话
        List<Surface> surfaces = new LinkedList();
        surfaces.add(textureView.getSurface());
        surfaces.add(imageReader.getSurface());
        device.createCaptureSession(surfaces, new CaptureSessionCallback() {
            @Override
            @SneakyThrows
            public void onConfigured(CameraCaptureSession session) {
                CameraCaptureActivity.this.session = session;
                //开始预览
                CaptureRequest.Builder captureRequestBuilder = device.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                captureRequestBuilder.addTarget(textureView.getSurface());
                request = captureRequestBuilder.build();
                session.setRepeatingRequest(request, null, backgroundHandler);
            }
        }, backgroundHandler);
    }

    //录制视频
    @SneakyThrows
    protected void startVideoRecord() {

        //停止其它会话
        if (session != null)
            session.close();

        //设置录制参数
        recordPath = CommonApplication.getFilePath("video/" + Times.now(Times.FORMAT_03) + ".mp4");
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(recordPath);
        mediaRecorder.setVideoEncodingBitRate(10000000);
        mediaRecorder.setVideoFrameRate(30);
        mediaRecorder.setVideoSize(previewSize.w, previewSize.h);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOrientationHint(90);
        mediaRecorder.prepare();

        //创建录制会话
        List<Surface> surfaces = new LinkedList();
        surfaces.add(textureView.getSurface());
        surfaces.add(mediaRecorder.getSurface());
        surfaces.add(imageReader.getSurface());
        device.createCaptureSession(surfaces, new CaptureSessionCallback() {
            @Override
            @SneakyThrows
            public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                session = cameraCaptureSession;
                //发出录制请求
                CaptureRequest.Builder builder = device.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
                builder.addTarget(textureView.getSurface());
                builder.addTarget(mediaRecorder.getSurface());
                builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
                request = builder.build();
                session.setRepeatingRequest(request, null, backgroundHandler);
                //开始录制
                mediaRecorder.start();
            }
        }, backgroundHandler);
    }

    //停止录制视频
    protected void stopVideoRecord() {
        //停止录制
        mediaRecorder.stop();
        mediaRecorder = null;
        //成功提示
        capturedFiles.add(recordPath);
        recordPath = null;
        String directory = CommonApplication.getPathDescription("video");
        messageDialog.message("录像成功，已保存至\n" + directory).onClose(() -> {
            if (singleMode) finish();
        }).showWithoutIcon();
        //恢复预览会话
        startVideoPreview();
    }

    //抓拍图片
    @SneakyThrows
    protected void startImageCapture() {
        CaptureRequest.Builder captureRequestBuilder = device.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
        captureRequestBuilder.addTarget(imageReader.getSurface());
        CaptureRequest captureRequest = captureRequestBuilder.build();
        session.capture(captureRequest, null, backgroundHandler);
    }

    //写出图片
    @SneakyThrows
    protected void writeOutImage(ImageReader reader) {
        //保存图片到本地
        String path = CommonApplication.getFilePath("picture/" + Times.now(Times.FORMAT_03) + ".jpg");
        String directory = CommonApplication.getPathDescription("picture");
        Image image = reader.acquireNextImage();
        Bitmaps.writeReversedImage(image, path, previewSize);
        //成功提示
        capturedFiles.add(path);
        messageDialog.message("拍照成功，已保存至\n" + directory).onClose(() -> {
            if (singleMode) finish();
        }).showWithoutIcon();
    }


}
