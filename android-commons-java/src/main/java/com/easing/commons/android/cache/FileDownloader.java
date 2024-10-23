package com.easing.commons.android.cache;

import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.event.EventBus;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.helper.exception.BizException;
import com.easing.commons.android.http.Postman;
import com.easing.commons.android.io.Files;
import com.easing.commons.android.thread.Threads;
import com.easing.commons.android.thread.WorkThread;
import com.easing.commons.android.value.http.HttpMethod;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@SuppressWarnings("all")
public class FileDownloader {

    static Queue<Task> taskQueue = new ConcurrentLinkedQueue();

    static Map<String, Task> taskMap = new ConcurrentHashMap();

    static boolean started = false;

    //开始下载图片
    public static void start() {
        if (started)
            return;
        WorkThread.postByInterval(() -> {
            if (taskQueue.isEmpty())
                return;
            //去除队列首部任务执行
            Task task = taskQueue.poll();
            try {
                Postman.create()
                        .url(task.url)
                        .method(HttpMethod.GET)
                        .head(task.header)
                        .onIoException((postman, call, e) -> {
                            throw BizException.THREAD_NORMAL_EXIT;
                        })
                        .onBizException((postman, call, e) -> {
                            throw BizException.THREAD_NORMAL_EXIT;
                        })
                        .onResponse((postman, call, response) -> {
                            if (response.code() >= 300)
                                throw BizException.THREAD_NORMAL_EXIT;
                            byte[] bytes = response.body().bytes();
                            //文件一般比较大
                            //如果字节数组比较小，或里面存储的是JSON/XML字符串，则代表请求失败
                            if (bytes.length < 1024 * 10) {
                                if (Texts.isJsonString(bytes) || Texts.isXmlString(bytes))
                                    throw BizException.THREAD_NORMAL_EXIT;
                            }
                            //先保存到临时位置，下载完成后再修改到正确位置
                            String tempPath = task.path + ".downloadtask";
                            Files.writeToFile(tempPath, bytes);
                            //重命名至正确路径
                            Files.renameFile(tempPath, task.path);
                            //下载完成
                            taskMap.remove(task.url);
                            taskQueue.remove(task);
                            //发布图片下载完成通知
                            EventBus.core.emitLater(500, "onFileCacheUpdate", task.url, task.path);
                            EventBus.core.emitLater(500, "onFileCacheUpdateTask", task); //有用勿删
                        })
                        .execute(true);
            } catch (Throwable e) {
                //处理失败后，重新添加到队列尾部，最后执行
                taskQueue.offer(task);
                Threads.sleep(3000);
            }
        }, 500, CommonApplication.componentState);
    }

    public static boolean contains(String url) {
        boolean containsKey = taskMap.containsKey(url);
        return containsKey;
    }

    public static void add(Task task) {
        if (taskMap.containsKey(task.url))
            return;
        taskMap.put(task.url, task);
        taskQueue.offer(task);
    }

    public static void remove(String url) {
        if (!taskMap.containsKey(url))
            return;
        Task task = taskMap.get(url);
        taskMap.remove(url);
        taskQueue.remove(task);
    }

    /**
     * @see FileDownloader#add(Task task)
     */
    @Deprecated
    public static void addTask(Task task) {
        if (taskMap.containsKey(task.url))
            return;
        taskMap.put(task.url, task);
        taskQueue.offer(task);
    }

    public static class Task<T> {

        public String url;
        public String path;

        //用于携带下载时的请求头
        public Map<String, Object> header;

        //用于携带额外信息
        public T tag;

        public Task(String url, String path) {
            this(url, path, null);
        }

        public Task(String url, String path, Map<String, Object> header) {
            if (header == null)
                header = new LinkedHashMap();
            this.url = url;
            this.path = path;
            this.header = header;
        }

        public Task header(String key, String value) {
            header.put(key, value);
            return this;
        }
    }
}

