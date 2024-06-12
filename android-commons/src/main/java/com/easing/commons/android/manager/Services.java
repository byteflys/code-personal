package com.easing.commons.android.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.Service;
import android.app.job.JobScheduler;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Rect;
import android.media.AudioManager;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.PowerManager;
import android.os.ResultReceiver;
import android.os.Vibrator;
import android.print.PrintManager;
import android.view.View;
import android.view.autofill.AutofillManager;
import android.view.inputmethod.InputMethodManager;

import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.thread.MainThread;

@SuppressWarnings("all")
public class Services {

    public static AudioManager getAudioManager(Context context) {
        return (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public static LocationManager getLocationManager(Context context) {
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public static PowerManager getPowerManager(Context context) {
        return (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    }

    public static BatteryManager getBatteryManager(Context context) {
        return (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
    }

    public static AlarmManager getAlarmManager(Context context) {
        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public static Vibrator getVibratorManager(Context context) {
        return (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
    }

    public static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
    }

    public static ActivityManager getActivityManager(Context context) {
        return (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    public static PrintManager getPrintManager(Context context) {
        return (PrintManager) context.getSystemService(Context.PRINT_SERVICE);
    }

    public static AutofillManager getAutofillManager(Context context) {
        return context.getSystemService(AutofillManager.class);
    }

    public static JobScheduler jobScheduler() {
        return CommonApplication.ctx.getSystemService(JobScheduler.class);
    }

    public static <T> T service(Class<T> clazz) {
        return CommonApplication.ctx.getSystemService(clazz);
    }

    public static void vibrate() {
        Services.vibrate(3);
    }

    public static void vibrate(int second) {
        Vibrator vibrator = Services.getVibratorManager(CommonApplication.ctx);
        long[] intervals = new long[1 + second * 2];
        int index = 0;
        intervals[index++] = 100L;
        for (int i = 0; i < second; i++) {
            intervals[index++] = 500L;
            intervals[index++] = 500L;
        }
        vibrator.vibrate(intervals, -1);
    }

    //复制文本到剪贴板
    public static void copyToClipboard(Context ctx, String text) {
        ClipboardManager clipboard = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("simple text", text);
        clipboard.setPrimaryClip(clip);
    }

    //复制文本到剪贴板
    public static void copyToClipboard(String text) {
        copyToClipboard(CommonApplication.ctx, text);
    }

    //判断软键盘是否打开
    public static boolean isKeyboardOpen(Activity activity) {
        InputMethodManager manager = CommonApplication.ctx.getSystemService(InputMethodManager.class);
        int windowHeight = activity.getWindow().getDecorView().getHeight();
        Rect rect = new android.graphics.Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int visibleHeight = rect.bottom;
        return windowHeight - visibleHeight > Dimens.toPx(100);
    }

    //打开软键盘
    public static void openKeyboard(View view) {
        InputMethodManager manager = view.getContext().getSystemService(InputMethodManager.class);
        manager.showSoftInputFromInputMethod(view.getWindowToken(), InputMethodManager.SHOW_IMPLICIT);
    }

    //关闭软键盘
    public static void closeKeyboard(View view) {
        InputMethodManager manager = view.getContext().getSystemService(InputMethodManager.class);
        manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS, new ResultReceiver(CommonApplication.handler));
    }

    //关闭软键盘
    @Deprecated
    public static void closeKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive() && activity.getCurrentFocus() != null)
            if (activity.getCurrentFocus().getWindowToken() != null)
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //获取电量
    public static int getBatteryLevel() {
        BatteryManager batteryManager = getBatteryManager(CommonApplication.ctx);
        int level = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        return level;
    }

    //获取喇叭音量
    public static float getSpeakerVolume(int streamType) {
        AudioManager audioManager = getAudioManager(CommonApplication.ctx);
        int volume = audioManager.getStreamVolume(streamType);
        int minVolume = audioManager.getStreamMinVolume(streamType);
        int maxVolume = audioManager.getStreamMaxVolume(streamType);
        float volumeLevel = (volume - minVolume) / (float) (maxVolume - minVolume);
        return volume;
    }

    //设置喇叭音量
    public static void setSpeakerVolume(float volumeLevel, int streamType) {
        AudioManager audioManager = getAudioManager(CommonApplication.ctx);
        int maxVolume = audioManager.getStreamMaxVolume(streamType);
        int minVolume = audioManager.getStreamMinVolume(streamType);
        int volume = (int) (minVolume + volumeLevel * (maxVolume - minVolume));
        audioManager.setStreamVolume(streamType, volume, 0);
    }

    //设置媒体音量
    public static void setMediaSpeakerVolume(float volumeLevel) {
        setSpeakerVolume(volumeLevel, AudioManager.STREAM_MUSIC);
    }

    //设置通话音量
    public static void setCallSpeakerVolume(float volumeLevel) {
        setSpeakerVolume(volumeLevel, AudioManager.STREAM_VOICE_CALL);
    }
}

