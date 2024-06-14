package com.easing.commons.android.service.keep_alive;

import android.app.Notification;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;

import com.easing.commons.android.app.Applications;
import com.easing.commons.android.code.Console;
import com.easing.commons.android.manager.Notifications;
import com.easing.commons.android.thread.ThreadFlag;
import com.easing.commons.android.thread.Threads;
import com.easing.commons.android.thread.WorkThread;
import com.easing.commons.android.value.identity.Codes;

//拉活服务，定期通过JobScheduler启动服务
//当服务被启动时，服务所在的进程，自然也随之被唤起
public class PullAliveService extends JobService {

    ThreadFlag threadFlag = ThreadFlag.create();

    @Override
    public void onCreate() {
        super.onCreate();
        WorkThread.postByInterval(() -> {
            for (Class<? extends Service> service : KeepAliveJob.getProtectedServices())
                startService(new Intent(this, service));
            Threads.sleep(5000);
        }, 5000, threadFlag);
        Notification notification = Notifications.buildForegroundNotification("正在为您工作", "这个服务保证软件能在后台继续运行");
        startForeground(Codes.CODE_BACKGROUND_RUNNING, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Console.info(getClass().getSimpleName(), Applications.currentProcessName(), Applications.currentProcessId(), hashCode());
        return Service.START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        for (Class<? extends Service> service : KeepAliveJob.getProtectedServices())
            startService(new Intent(this, service));
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
