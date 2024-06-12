package com.easing.commons.android.ui.control.attach_viewer;

import com.easing.commons.android.data.Jsonable;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.io.Files;

@SuppressWarnings("all")
public class Attach implements Jsonable {

    public String onlineID;
    public String offlineID;
    public String file;
    public String url;

    public String name;
    public String ext = "";
    public String type = "other"; //text文本，image图片，audio音频，video视频，doc文档

    public static Attach createFromUUID(String newFile, String uuid) {
        Attach attach = new Attach();
        attach.offlineID = uuid;
        attach.file = newFile;
        attach.name = Files.getFileName(newFile);
        attach.ext = Files.getExtensionName(newFile);
        attach.type = Files.getFileType(newFile);
        return attach;
    }

    public static Attach create(String file, String name) {
        Attach attach = new Attach();
        attach.offlineID = Texts.random();
        attach.file = file;
        attach.name = name == null ? Files.getFileName(file) : name;
        attach.ext = Files.getExtensionName(file);
        attach.type = Files.getFileType(file);
        return attach;
    }

    public static Attach create(String url, String fileName ,String type) {
        Attach attach = new Attach();
        attach.offlineID = Texts.random();
        attach.url = url;
        attach.name = fileName;
        attach.ext = Files.getExtensionName(fileName);
        attach.type = type;
        return attach;
    }
}

