package com.easing.commons.android.value.identity;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class RequestCodeUtils {

    static final Map<String, List<Integer>> codeMap = new LinkedHashMap();

    static {
        codeMap.put("pickFile", new LinkedList());
        codeMap.put("captureImage", new LinkedList());
        codeMap.put("captureVideo", new LinkedList());
        codeMap.put("captureAudio", new LinkedList());
        addFilePickCode(Codes.CODE_PICK_FILE);
        addImageCaptureCode(Codes.CODE_IMAGE_CAPTURE);
        addVideoCaptureCode(Codes.CODE_VIDEO_CAPTURE);
        addAudioCaptureCode(Codes.CODE_AUDIO_CAPTURE);
    }

    public static void addFilePickCode(Integer requestCode) {
        List<Integer> codes = codeMap.get("pickFile");
        codes.add(requestCode);
    }

    public static void addImageCaptureCode(Integer requestCode) {
        List<Integer> codes = codeMap.get("captureImage");
        codes.add(requestCode);
    }

    public static void addVideoCaptureCode(Integer requestCode) {
        List<Integer> codes = codeMap.get("captureVideo");
        codes.add(requestCode);
    }

    public static void addAudioCaptureCode(Integer requestCode) {
        List<Integer> codes = codeMap.get("captureAudio");
        codes.add(requestCode);
    }

    public static void removeFilePickCode(Integer requestCode) {
        List<Integer> codes = codeMap.get("pickFile");
        codes.remove(requestCode);
    }

    public static void removeImageCaptureCode(Integer requestCode) {
        List<Integer> codes = codeMap.get("captureImage");
        codes.remove(requestCode);
    }

    public static void removeVideoCaptureCode(Integer requestCode) {
        List<Integer> codes = codeMap.get("captureVideo");
        codes.remove(requestCode);
    }

    public static void removeAudioCaptureCode(Integer requestCode) {
        List<Integer> codes = codeMap.get("captureAudio");
        codes.remove(requestCode);
    }

    public static boolean isFilePickCode(Integer requestCode) {
        List<Integer> codes = codeMap.get("pickFile");
        return codes.contains(requestCode);
    }

    public static boolean isImageCaptureCode(Integer requestCode) {
        List<Integer> codes = codeMap.get("captureImage");
        return codes.contains(requestCode);
    }

    public static boolean isVideoCaptureCode(Integer requestCode) {
        List<Integer> codes = codeMap.get("captureVideo");
        return codes.contains(requestCode);
    }

    public static boolean isAudioCaptureCode(Integer requestCode) {
        List<Integer> codes = codeMap.get("captureAudio");
        return codes.contains(requestCode);
    }
}
