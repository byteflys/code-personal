package com.easing.commons.android.service.keep_alive;

import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;

import com.easing.commons.android.annotation_processor.Reflection;
import com.easing.commons.android.app.Applications;
import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.manager.Services;
import com.easing.commons.android.preference.Preference;
import com.easing.commons.android.value.identity.Codes;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class KeepAliveJob {

    //将要保活的服务列表存到数据库
    //由于保活功能是跨进程的，对象值是无法相互访问的，所以借助数据库来中转
    public static void saveProtectedServices(Class<? extends Service>... protectedServices) {
        List names = new ArrayList();
        names.add(SA.class.getName());
        names.add(SB.class.getName());
        names.add(PullAliveService.class.getName());
        for (Class clazz : protectedServices)
            names.add(clazz.getName());
        Preference.set("keep_alive_servive_list", names);
    }

    //从数据库获取要保活的服务列表
    public static List<Class<? extends Service>> getProtectedServices() {
        List<Class<? extends Service>> serviceList = new ArrayList();
        String[] names = Preference.get("keep_alive_servive_list", String[].class, new String[]{});
        for (String name : names) {
            Class<? extends Service> service = Reflection.findClass(name);
            serviceList.add(service);
        }
        return serviceList;
    }

    //清空要保活的服务列表
    public static void clearProtectedServices(Class<? extends Service>... protectedServices) {
        Preference.set("keep_alive_servive_list", null);
    }

    //通过JobScheduler开启一个JobService
    public static void startJobService(int jobId, Class<? extends JobService> service) {
        ComponentName componentName = new ComponentName(Applications.packageName(), service.getName());
        JobInfo.Builder builder = new JobInfo.Builder(jobId, componentName);
        builder.setPeriodic(15 * 60 * 1000);
        builder.setPersisted(true);
        JobInfo jobInfo = builder.build();
        JobScheduler scheduler = Services.jobScheduler();
        scheduler.schedule(jobInfo);
    }

    //停止JobService
    public static void stopJobService(int jobId) {
        JobScheduler scheduler = Services.jobScheduler();
        scheduler.cancel(jobId);
    }

    //开启保活工作
    public static void startKeepAliveJob(Class<? extends Service>... protectedServices) {
        //保存要保活的服务列表
        KeepAliveJob.saveProtectedServices(protectedServices);
        //启动保活服务
        CommonApplication.startService(SA.class);
        CommonApplication.startService(SB.class);
        CommonApplication.startService(PullAliveService.class);
        //启动拉活服务
        KeepAliveJob.startJobService(Codes.CODE_PULL_ALIVE_JOB, PullAliveService.class);
    }

    //停止保活工作
    public static void stopKeepAliveJob() {
        //停止保活服务
        List<Class<? extends Service>> temp = KeepAliveJob.getProtectedServices();
        KeepAliveJob.clearProtectedServices();
        for (Class<? extends Service> service : temp)
            CommonApplication.stopService(service);
        //取消拉活服务
        KeepAliveJob.stopJobService(Codes.CODE_PULL_ALIVE_JOB);
    }
}
