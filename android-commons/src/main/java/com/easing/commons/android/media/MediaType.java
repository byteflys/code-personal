package com.easing.commons.android.media;

import android.webkit.MimeTypeMap;

import com.easing.commons.android.format.Texts;

import java.io.File;

@SuppressWarnings("all")
public class MediaType {

    public static final String TYPE_ALL = "*/*";
    public static final String TYPE_TEXT = "text/*";
    public static final String TYPE_IMAGE = "image/*";
    public static final String TYPE_IMAGE_JPEG = "image/jpeg";
    public static final String TYPE_AUDIO = "audio/*";
    public static final String TYPE_VIDEO = "video/*";
    public static final String TYPE_ZIP = "application/x-zip-compressed";
    public static final String TYPE_KML = "application/vnd.google-earth.kml+xml";

    public static String getMimeType(String path) {
        String name = new File(path).getName().toLowerCase();
        int index = name.lastIndexOf(".");
        if (index > 0) {
            String ext = name.substring(index + 1);
            if (!Texts.isEmpty(ext)) {
                MimeTypeMap map = MimeTypeMap.getSingleton();
                if (map.hasExtension(ext))
                    return map.getMimeTypeFromExtension(ext);
            }
        }
        return "*/*";
    }

    public static boolean isImage(String path) {
        if (Texts.isEmpty(path))
            return false;
        if (path.toLowerCase().endsWith(".png"))
            return true;
        if (path.toLowerCase().endsWith(".bmp"))
            return true;
        if (path.toLowerCase().endsWith(".jpg"))
            return true;
        if (path.toLowerCase().endsWith(".jpeg"))
            return true;
        if (path.toLowerCase().endsWith(".gif"))
            return true;
        return false;
    }

    public static boolean isAudio(String path) {
        if (Texts.isEmpty(path))
            return false;
        if (path.toLowerCase().endsWith(".mp3"))
            return true;
        if (path.toLowerCase().endsWith(".wav"))
            return true;
        if (path.toLowerCase().endsWith(".aac"))
            return true;
        if (path.toLowerCase().endsWith(".flac"))
            return true;
        if (path.toLowerCase().endsWith(".ape"))
            return true;
        if (path.toLowerCase().endsWith(".m4a"))
            return true;
        return false;
    }

    public static boolean isVideo(String path) {
        if (Texts.isEmpty(path))
            return false;
        if (path.toLowerCase().endsWith(".mp4"))
            return true;
        if (path.toLowerCase().endsWith(".avi"))
            return true;
        if (path.toLowerCase().endsWith(".flv"))
            return true;
        if (path.toLowerCase().endsWith(".rmvb"))
            return true;
        if (path.toLowerCase().endsWith(".mkv"))
            return true;
        return false;
    }

    public static boolean isText(String path) {
        if (Texts.isEmpty(path))
            return false;
        if (path.toLowerCase().endsWith(".txt"))
            return true;
        if (path.toLowerCase().endsWith(".ini"))
            return true;
        if (path.toLowerCase().endsWith(".info"))
            return true;
        if (path.toLowerCase().endsWith(".info"))
            return true;
        if (path.toLowerCase().endsWith(".error"))
            return true;
        if (path.toLowerCase().endsWith(".xml"))
            return true;
        if (path.toLowerCase().endsWith(".json"))
            return true;
        if (path.toLowerCase().endsWith(".html"))
            return true;
        if (path.toLowerCase().endsWith(".css"))
            return true;
        if (path.toLowerCase().endsWith(".js"))
            return true;
        if (path.toLowerCase().endsWith(".java"))
            return true;
        if (path.toLowerCase().endsWith(".h"))
            return true;
        if (path.toLowerCase().endsWith(".c"))
            return true;
        if (path.toLowerCase().endsWith(".cpp"))
            return true;
        if (!path.toLowerCase().contains("."))
            return true;
        return false;
    }

    /**
     * word文档
     */
    public static boolean isDocument(String path) {
        if (Texts.isEmpty(path))
            return false;
        if (path.toLowerCase().endsWith(".doc"))
            return true;
        if (path.toLowerCase().endsWith(".docx"))
            return true;
        return false;
    }

    /**
     * 电子表格
     */
    public static boolean isXls(String path) {
        if (Texts.isEmpty(path))
            return false;
        if (path.toLowerCase().endsWith(".xls"))
            return true;
        if (path.toLowerCase().endsWith(".xlsx"))
            return true;
        return false;
    }

    /**
     * pdf
     */
    public static boolean isPdf(String path) {
        if (Texts.isEmpty(path))
            return false;
        if (path.toLowerCase().endsWith(".pdf"))
            return true;
        return false;
    }

    public static boolean isWebResource(String path) {

        if (Texts.isEmpty(path))
            return false;
        if (path.startsWith("http://"))
            return true;
        if (path.startsWith("https://"))
            return true;
        return false;
    }
}
