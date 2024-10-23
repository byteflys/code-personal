package com.easing.commons.android.manager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;

import com.easing.commons.android.R;
import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.value.apk.SdkVersion;
import com.easing.commons.android.value.identity.Codes;

/**
 * 状态通知
 */
@SuppressWarnings("all")
public class Notifications {

    //显示普通文本通知
    public static void showNotification(String content) {
        showNotification(CommonApplication.projectLabel, content, null, Codes.randomCode(), R.drawable.md007_001_app_icon_2);
    }

    //显示普通文本通知
    public static void showNotification(String title, String content, Intent action, int id) {
        showNotification(title, content, action, id, R.drawable.md007_001_app_icon_2);
    }

    //显示普通文本通知
    public static void showNotification(String title, String content, Intent action, int id, @DrawableRes int drawableId) {
        if (Device.sdkVersionCode() >= SdkVersion.ANDROID_8) {
            //创建NotificationChannel
            String channelName = "应用消息通知";
            NotificationChannel channel = new NotificationChannel(channelName, channelName, NotificationManager.IMPORTANCE_HIGH);
            channel.setName(channelName);
            if (Device.sdkVersionCode() >= SdkVersion.ANDROID_10)
                channel.setAllowBubbles(true);
            //创建NotificationBuilder
            Notification.Builder builder = new Notification.Builder(CommonApplication.ctx, channelName);
            builder.setPriority(NotificationManager.IMPORTANCE_HIGH);
            builder.setContentTitle(title);
            builder.setContentText(content);
            builder.setSmallIcon(drawableId);
            if (action != null) {
                PendingIntent pi = PendingIntent.getActivity(CommonApplication.ctx, id, action, PendingIntent.FLAG_CANCEL_CURRENT);
                builder.setContentIntent(pi);
            }

            builder  .setLights(Color.GREEN,1000,1000);//设置前置LED灯进行闪烁， 第一个为颜色值  第二个为亮的时长  第三个为暗的时长

            builder.setShowWhen(true);
            builder.setAutoCancel(true);
            //创建并显示Notification
            Notification notification = builder.build();
            NotificationManager manager = Services.getNotificationManager(CommonApplication.ctx);
            manager.createNotificationChannel(channel);
            manager.notify(id, notification);
            //震动提醒
            Services.vibrate();
        } else {
            //创建NotificationBuilder
            Notification.Builder builder = new Notification.Builder(CommonApplication.ctx);
            builder.setPriority(Notification.PRIORITY_MAX);
            builder.setContentTitle(title);
            builder.setContentText(content);
            builder.setSmallIcon(R.drawable.icon_favor);

            if (action != null) {
                PendingIntent pi = PendingIntent.getActivity(CommonApplication.ctx, id, action, PendingIntent.FLAG_CANCEL_CURRENT);
                builder.setContentIntent(pi);
            }
            builder.setShowWhen(true);
            builder.setAutoCancel(true);
            //创建并显示Notification
            Notification notice = builder.build();
            NotificationManager manager = Services.getNotificationManager(CommonApplication.ctx);
            manager.notify(id, notice);
            //震动提醒
            Services.vibrate();
        }
    }

    //更新进度
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void updateProgress(int id, String title, String text, int progress) {
        if (Device.sdkVersionCode() >= SdkVersion.ANDROID_8) {
            //创建NotificationChannel
            String channelName = "PackageUpdate";
            NotificationChannel channel = new NotificationChannel(channelName, channelName, NotificationManager.IMPORTANCE_MIN);
            channel.setName("PackageUpdate");
            if (Device.sdkVersionCode() >= SdkVersion.ANDROID_10)
                channel.setAllowBubbles(false);
            //创建NotificationBuilder
            Notification.Builder builder = new Notification.Builder(CommonApplication.ctx, channelName);
            builder.setContentTitle(null);
            builder.setContentText(text);
            builder.setSmallIcon(R.drawable.color_icon_m02_download);
            //使用自定义布局
            RemoteViews remoteViews = new RemoteViews(CommonApplication.ctx.getPackageName(), R.layout.layout_progress_notification);
            builder.setCustomContentView(remoteViews);
            remoteViews.setTextViewText(R.id.text_title, title);
            remoteViews.setTextViewText(R.id.text_content, text);
            remoteViews.setProgressBar(R.id.progress_bar, 100, progress, false);
            //设置显示方式
            builder.setShowWhen(true);
            builder.setAutoCancel(true);
            //创建并显示Notification
            Notification notification = builder.build();
            NotificationManager manager = Services.getNotificationManager(CommonApplication.ctx);
            manager.createNotificationChannel(channel);
            manager.notify(id, notification);
        } else {
            //创建NotificationBuilder
            Notification.Builder builder = new Notification.Builder(CommonApplication.ctx);
            builder.setContentTitle(null);
            builder.setContentText(text);
            builder.setSmallIcon(R.drawable.color_icon_m02_download);
            //使用自定义布局
            RemoteViews remoteViews = new RemoteViews(CommonApplication.ctx.getPackageName(), R.layout.layout_progress_notification);
            builder.setCustomContentView(remoteViews);
            remoteViews.setTextViewText(R.id.text_title, title);
            remoteViews.setTextViewText(R.id.text_content, text);
            remoteViews.setProgressBar(R.id.progress_bar, 100, progress, false);
            //设置显示方式
            builder.setShowWhen(true);
            builder.setAutoCancel(false);
            //创建并显示Notification
            Notification notification = builder.build();
            NotificationManager manager = Services.getNotificationManager(CommonApplication.ctx);
            manager.notify(id, notification);
        }
    }

    //构建一个与Service相绑定的的前台通知
    //Service与Notification绑定后，即使切换到后台，仍然能正常运行
    public static Notification buildForegroundNotification(String title, String content) {
        if (Device.sdkVersionCode() >= SdkVersion.ANDROID_8) {
            String channelName = "Foreground Service";
            NotificationChannel channel = new NotificationChannel(channelName, channelName, NotificationManager.IMPORTANCE_LOW);
            //创建NotificationBuilder
            Notification.Builder builder = new Notification.Builder(CommonApplication.ctx, channelName);
            builder.setContentTitle(title);
            builder.setContentText(content);
            builder.setSmallIcon(R.drawable.icon_foreground_service);
            //创建并显示Notification
            NotificationManager manager = Services.getNotificationManager(CommonApplication.ctx);
            manager.createNotificationChannel(channel);
            Notification notification = builder.build();
            return notification;
        } else {
            //创建NotificationBuilder
            Notification.Builder builder = new Notification.Builder(CommonApplication.ctx);
            builder.setContentTitle(title);
            builder.setContentText(content);
            builder.setSmallIcon(R.drawable.icon_foreground_service);
            //创建并显示Notification
            Notification notification = builder.build();
            return notification;
        }
    }

    //显示默认通知
    public static void showDefaultNotice() {
        Notifications.showNotification("这是一条测试消息，请忽略");
    }

    //取消单个通知
    public static void cancel(int id) {
        Services.getNotificationManager(CommonApplication.ctx).cancel(id);
    }

    //取消全部通知
    public static void cancelAll() {
        Services.getNotificationManager(CommonApplication.ctx).cancelAll();
    }

}
