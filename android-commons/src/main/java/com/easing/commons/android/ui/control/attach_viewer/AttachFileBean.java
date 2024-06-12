package com.easing.commons.android.ui.control.attach_viewer;

import com.easing.commons.android.data.Jsonable;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.io.Files;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务器附件bean
 */
@NoArgsConstructor
@Data
public class AttachFileBean<T> implements Jsonable {

    private String offlineFileId;
    private String fileId;
    private String url;
    private String filePath;

    private String fileName;
    private String ext = "";
    private String fileType = "other"; //text文本，image图片，audio音频，video视频，doc文档
    private T tag; //额为参数

    public AttachFileBean(String fileId, String filePath, String fileName, String fileType) {
        this.fileId = fileId;
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileType = fileType;
    }

    public static AttachFileBean createFromUUID(String newFile, String uuid) {
        AttachFileBean attach = new AttachFileBean();
        attach.offlineFileId = uuid;
        attach.filePath = newFile;
        attach.fileName = Files.getFileName(newFile);
        attach.ext = Files.getExtensionName(newFile);
        attach.fileType = Files.getFileType(newFile);
        return attach;
    }

    public static AttachFileBean create(String file, String name) {
        AttachFileBean attach = new AttachFileBean();
        attach.offlineFileId = Texts.random();
        attach.filePath = file;
        attach.fileName = name == null ? Files.getFileName(file) : name;
        attach.ext = Files.getExtensionName(file);
        attach.fileType = Files.getFileType(file);
        return attach;
    }
}

