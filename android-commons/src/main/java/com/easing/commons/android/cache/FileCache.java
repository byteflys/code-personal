package com.easing.commons.android.cache;

import com.easing.commons.android.format.Texts;
import com.easing.commons.android.greendao.cache.FileCacheGreenDaoHandler;
import com.easing.commons.android.io.Files;
import com.easing.commons.android.io.ProjectFile;

import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;

import lombok.Data;

@Data
@SuppressWarnings("all")
public class FileCache {

    @Id
    @Unique
    public String url;
    public String path;

    public FileCache(String url, String path) {
        this.url = url;
        this.path = path;
    }

    //判断文件是否已下载
    public static boolean cached(String url) {
        boolean exist = exist(url);
        if (!exist)
            return false;
        FileCacheDao dao = FileCacheGreenDaoHandler.session.getFileCacheDao();
        List<FileCache> caches = dao.queryRaw("where url = ?", url);
        FileCache cache = caches.get(0);
        if (Files.exist(cache.path) && Files.size(cache.path) > 0)
            return true;
        return false;
    }

    //判断任务是否已存在
    //如果没有会自动添加
    public static boolean exist(String url) {
        FileCacheDao dao = FileCacheGreenDaoHandler.session.getFileCacheDao();
        List<FileCache> caches = dao.queryRaw("where url = ?", url);
        if (caches.isEmpty()) {
            addCache(url);
            return false;
        }
        FileCache cache = caches.get(0);
        if (Files.exist(cache.path) && Files.size(cache.path) > 0)
            return true;
        //检查是否有下载任务，无则添加
        checkDownloadTask(cache);
        return true;
    }

    //返回缓存地址
    //如果没有缓存任务，则自动创建
    public static String findCache1(String url) {
        boolean exist = exist(url);
        FileCacheDao dao = FileCacheGreenDaoHandler.session.getFileCacheDao();
        List<FileCache> caches = dao.queryRaw("where url = ?", url);
        FileCache cache = caches.get(0);
        return cache.path;
    }

    //返回缓存地址
    //如果没有则返回原地址
    public static String findCache2(String url) {
        boolean cached = cached(url);
        if (!cached)
            return url;
        FileCacheDao dao = FileCacheGreenDaoHandler.session.getFileCacheDao();
        List<FileCache> caches = dao.queryRaw("where url = ?", url);
        FileCache cache = caches.get(0);
        return cache.path;
    }

    //添加缓存任务
    public static void addCache(String url) {
        String ext = Files.getExtensionName(url);
        String path = ProjectFile.getProjectFilePath(Texts.random() + "." + ext);
        FileCache cache = new FileCache(url, path);
        FileCacheDao dao = FileCacheGreenDaoHandler.session.getFileCacheDao();
        dao.insertOrReplace(cache);
        //检查是否有下载任务，无则添加
        checkDownloadTask(cache);
    }

    //移除缓存任务
    public static void removeCache(String url) {
        FileCacheDao dao = FileCacheGreenDaoHandler.session.getFileCacheDao();
        List<FileCache> caches = dao.queryRaw("where url = ?", url);
        dao.deleteInTx(caches);
    }

    //移除缓存任务和已缓存的文件
    public static void removeCacheAndFile(String url) {
        FileCacheDao dao = FileCacheGreenDaoHandler.session.getFileCacheDao();
        List<FileCache> caches = dao.queryRaw("where url = ?", url);
        dao.deleteInTx(caches);
        FileDownloader.remove(url);
    }

    //清空全部缓存任务
    public static void clearCache() {
        FileCacheDao dao = FileCacheGreenDaoHandler.session.getFileCacheDao();
        dao.deleteAll();
    }

    //清空全部缓存任务和已缓存的文件
    public static void clearCacheAndFile() {
        FileCacheDao dao = FileCacheGreenDaoHandler.session.getFileCacheDao();
        List<FileCache> caches = dao.loadAll();
        dao.deleteAll();
        for (FileCache cache : caches)
            FileDownloader.remove(cache.url);
    }

    //检查是否有下载任务，无则添加
    public static void checkDownloadTask(FileCache cache) {
        if (!FileDownloader.contains(cache.url)) {
            FileDownloader.Task task = new FileDownloader.Task(cache.url, cache.path);
            FileDownloader.addTask(task);
        }
    }
}

