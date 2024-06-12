package com.easing.commons.android.io;

import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.format.Texts;

@SuppressWarnings("all")
public class ProjectFile {

    public static final String EXTERNAL_STORAGE = "/storage/emulated/0/";
    public static final String EXTERNAL_ENCLOSURE = "enclosure"; //存放附件的临时文件目录

    //初始化项目目录
    public static void initProjectDirectory() {
        CommonApplication.ctx.getExternalFilesDir("");
    }

    //获取相对于存储卡根目录的文件路径
    public static String getStorageFile(String relativePath) {
        String path = EXTERNAL_STORAGE + relativePath;
        Files.createFile(path);
        return path;
    }

    //获取相对于存储卡根目录的文件夹路径
    public static String getStorageDirectory(String relativePath) {
        String path = EXTERNAL_STORAGE + relativePath;
        Files.createDirectory(path);
        return path;
    }

    //获取项目根目录
    public static String getProjectRootDirectory() {
        String path = EXTERNAL_STORAGE + "Android/data/" + CommonApplication.ctx.getPackageName() + "/" + CommonApplication.projectName + "/";
        Files.createDirectory(path);
        return path;
    }

    //获取相对于项目根目录的路径
    //这个接口只获取路径，不自动创建文件或文件夹
    public static String getProjectPath(String relativePath) {
        String path = getProjectRootDirectory() + relativePath;
        return path;
    }

    //获取相对于项目根目录的文件夹路径
    public static String getProjectFile(String relativePath) {
        String path = getProjectRootDirectory() + relativePath;
        Files.createFile(path);
        return path;
    }

    //获取相对于项目根目录的文件夹路径
    public static String getProjectDirectory(String relativePath) {
        String path = getProjectRootDirectory() + relativePath + "/";
        Files.createDirectory(path);
        return path;
    }

    public static String getProjectImagePath(String relativePath) {
        String path = getProjectRootDirectory() + "image/" + relativePath;
        Files.createFile(path);
        return path;
    }

    public static String getProjectImageDirectory() {
        String path = getProjectRootDirectory() + "image/";
        Files.createDirectory(path);
        return path;
    }

    public static String getProjectAudioPath(String relativePath) {
        String path = getProjectRootDirectory() + "audio/" + relativePath;
        Files.createFile(path);
        return path;
    }

    public static String getProjectAudioDirectory() {
        String path = getProjectRootDirectory() + "audio/";
        Files.createDirectory(path);
        return path;
    }

    public static String getProjectVideoPath(String relativePath) {
        String path = getProjectRootDirectory() + "video/" + relativePath;
        Files.createFile(path);
        return path;
    }

    public static String getProjectVideoDirectory() {
        String path = getProjectRootDirectory() + "video/";
        Files.createDirectory(path);
        return path;
    }

    public static String getProjectDocumentPath(String relativePath) {
        String path = getProjectRootDirectory() + "document/" + relativePath;
        Files.createFile(path);
        return path;
    }

    public static String getProjectDocumentDirectory() {
        String path = getProjectRootDirectory() + "document/";
        Files.createDirectory(path);
        return path;
    }

    public static String getProjectDatabasePath(String relativePath) {
        String path = getProjectRootDirectory() + "db/" + relativePath;
        Files.createFile(path);
        return path;
    }

    public static String getProjectDatabaseDirectory() {
        String path = getProjectRootDirectory() + "db/";
        Files.createDirectory(path);
        return path;
    }

    public static String getProjectPackagePath(String relativePath) {
        String path = getProjectRootDirectory() + "package/" + relativePath;
        Files.createFile(path);
        return path;
    }

    public static String getProjectPackageDirectory() {
        String path = getProjectRootDirectory() + "package/";
        Files.createDirectory(path);
        return path;
    }

    public static String getProjectInfoPath(String relativePath) {
        String path = getProjectRootDirectory() + "info/" + relativePath;
        Files.createFile(path);
        return path;
    }

    public static String getProjectInfoDirectory() {
        String path = getProjectRootDirectory() + "info/";
        Files.createDirectory(path);
        return path;
    }

    public static String getProjectErrorPath(String relativePath) {
        String path = getProjectRootDirectory() + "error/" + relativePath;
        Files.createFile(path);
        return path;
    }

    public static String getProjectErrorDirectory() {
        String path = getProjectRootDirectory() + "error/";
        Files.createDirectory(path);
        return path;
    }

    public static String getProjectExportPath(String relativePath) {
        String path = getProjectRootDirectory() + "export/" + relativePath;
        Files.createFile(path);
        return path;
    }

    public static String getProjectExportDirectory() {
        String path = getProjectRootDirectory() + "export/";
        Files.createDirectory(path);
        return path;
    }

    public static String getProjectFilePath(String relativePath) {
        String path = getProjectRootDirectory() + "file/" + relativePath;
        Files.createFile(path);
        return path;
    }

    public static String getProjectFileDirectory() {
        String path = getProjectRootDirectory() + "file/";
        Files.createDirectory(path);
        return path;
    }

    public static String getFileDescription(String path) {
        String relativePath = path.toLowerCase().replaceAll(EXTERNAL_STORAGE, "");
        return relativePath;
    }

    public static String getDirectoryDescription(String path) {
        if (!Files.isDirectory(path))
            path = Files.getParent(path);
        if (path.endsWith("/"))
            path = path.substring(0, path.length() - 1);
        String relativePath = path.toLowerCase().replaceAll(EXTERNAL_STORAGE, "");
        return relativePath;
    }

    public static String getFileSaveTip(String path) {
        String relativePath = getFileDescription(path);
        String tip = "数据保存于存储卡下的" + relativePath + "文件中";
        return tip;
    }

    public static String getDirectorySaveTip(String path) {
        String relativePath = getDirectoryDescription(path);
        String tip = "数据保存于存储卡下的" + relativePath + "文件夹中";
        return tip;
    }

    public static String getPathDescription(String path) {
        boolean isFile = Files.isFile(path);
        return isFile ? getFileDescription(path) : getDirectoryDescription(path);
    }

    //拷贝文件到项目附件目录
    public static String copyToProjectFile(String file, String uuid) {
        String ext = Files.getFileSuffix(file, false);
        String newName = ext.isEmpty() ? uuid : uuid + "." + ext;
        String newFile = ProjectFile.getProjectFilePath(newName);
        Files.copyToFile(file, newFile);
        return newFile;
    }
}
