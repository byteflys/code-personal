package com.easing.commons.android.lbs;

import android.app.Notification;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.event.EventBus;
import com.easing.commons.android.format.Maths;
import com.easing.commons.android.manager.Notifications;
import com.easing.commons.android.media.MediaManager;
import com.easing.commons.android.thread.Threads;
import com.easing.commons.android.thread.WorkThread;
import com.easing.commons.android.time.LaunchTime;
import com.easing.commons.android.time.Times;

@SuppressWarnings("all")
public class BDLocationManager {

    //前台服务对应的通知编号
    protected static final int foregroundServiceCode = 10001;

    //定位间隔
    protected static volatile int locateInterval = 30 * 1000;

    //定位客户端
    protected static LocationClient client;
    protected static LocationClientOption option;
    protected static BDAbstractLocationListener listener;
    protected static Notification notification;

    //上次定位时间
    protected static volatile long lastLocateTime;

    //坐标优化器
    protected static LBSLocationOptimizer optimizer = new OptimizerForTime();

    //坐标获取回调
    protected static OnLocation onAllLocation = OnLocation.Default;
    protected static OnLocation onGoodLocation = OnLocation.Default;
    protected static OnLocation onBadLocation = OnLocation.Default;
    protected static OnLocation onSingleLocation = OnLocation.Default;

    //单词定位请求码
    protected static String requestCode;

    //定位模式
    protected static LocateMode mode = LocateMode.Period;

    //是否正在定位中
    protected static boolean locating = false;

    //上次语音提示时间
    protected static long lastVoiceTime = 0L;

    //设置坐标优化器
    public static void optimizer(LBSLocationOptimizer optimizer) {
        BDLocationManager.optimizer = optimizer;
    }

    //设置回调
    public static void callback(OnLocation onAllLocation, OnLocation onGoodLocation, OnLocation onBadLocation) {
        if (onAllLocation == null)
            onAllLocation = OnLocation.Default;
        if (onGoodLocation == null)
            onGoodLocation = OnLocation.Default;
        if (onBadLocation == null)
            onBadLocation = OnLocation.Default;
        BDLocationManager.onAllLocation = onAllLocation;
        BDLocationManager.onGoodLocation = onGoodLocation;
        BDLocationManager.onBadLocation = onBadLocation;
    }

    //单次定位
    public static void locateOnce(String requestCode, OnLocation callback) {
        BDLocationManager.requestCode = requestCode;
        BDLocationManager.onSingleLocation = callback;
        mode(LocateMode.Once);
        restart();
    }

    //设置定位模式
    public static void mode(LocateMode mode) {
        BDLocationManager.mode = mode;
        if (mode == LocateMode.Once)
            interval(10);
        if (mode == LocateMode.Period)
            interval(30);
        if (mode == LocateMode.Track)
            interval(5);
    }

    //获取定位模式
    public static LocateMode mode() {
        return BDLocationManager.mode;
    }

    //设置定位间隔，单位秒
    public static void interval(int interval) {
        BDLocationManager.locateInterval = interval * 1000;
    }

    //初始化定位SDK
    public static void init() {

        //已经初始化
        if (BDLocationManager.client != null)
            return;

        //初始化SDK
        SDKInitializer.initialize(CommonApplication.ctx);
        SDKInitializer.setCoordType(CoordType.GCJ02);

        //创建回调
        BDAbstractLocationListener listener = new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {

                //无定位
                if (bdLocation.getLatitude() == 0 && bdLocation.getLongitude() == 0)
                    return;

                //定位更新时间
                BDLocationManager.lastLocateTime = LaunchTime.time;

                //记录坐标信息
                LBSLocation location = new LBSLocation();
                location.latitude = Maths.keepFloat2(bdLocation.getLatitude(), 6);
                location.longitude = Maths.keepFloat2(bdLocation.getLongitude(), 6);

                if (bdLocation.getAltitude() == Double.MIN_VALUE)
                    location.altitude = 0d;
                else
                    location.altitude = bdLocation.getAltitude();

                location.direction = bdLocation.getDirection();
                location.time = Times.now();
                location.country = bdLocation.getCountry();
                location.province = bdLocation.getProvince();
                location.city = bdLocation.getCity();
                location.district = bdLocation.getDistrict();
                location.street = bdLocation.getStreet();
                location.streetNumber = bdLocation.getStreetNumber();
                location.address = bdLocation.getAddrStr();
                location.speed = bdLocation.getSpeed();
                location.radius = bdLocation.getRadius();
                location.gpsStatus = bdLocation.getGpsAccuracyStatus();

                //错误数据纠正
                if (location.city != null) {
                    if (location.city.equals("广州市")) {
                        if (location.district.equals("萝岗区"))
                            location.district = "黄埔区";
                    }
                }

                //记录定位精度
                int accuracy = bdLocation.getGpsAccuracyStatus();
                if (accuracy == BDLocation.GPS_ACCURACY_GOOD)
                    location.accuracy = LBSLocation.SIGNAL_STRONG;
                else if (accuracy == BDLocation.GPS_ACCURACY_MID)
                    location.accuracy = LBSLocation.SIGNAL_MEDIUM;
                else if (accuracy == BDLocation.GPS_ACCURACY_BAD)
                    location.accuracy = LBSLocation.SIGNAL_WEAK;
                else location.accuracy = LBSLocation.SIGNAL_BAD;
                boolean bad = optimizer.bad(location);

                //更新信号强度
                EventBus.core.emit("onSignalLevelChange", location.accuracy);

                //全部坐标回调
                if (onAllLocation != null) {
                    onAllLocation.onLocation(location);
                    EventBus.core.emit("onAllLocation", location);
                }
                //高精度坐标回调
                if (onGoodLocation != null && !bad) {
                    onGoodLocation.onLocation(location);
                    EventBus.core.emit("onGoodLocation", location);
                }
                //低精度坐标回调
                if (onBadLocation != null && bad) {
                    onBadLocation.onLocation(location);
                    EventBus.core.emit("onBadLocation", location);
                }

                //如果是单次定位，立刻停止定位
                if (mode == LocateMode.Once) {
                    onSingleLocation.onLocation(location);
                    EventBus.core.emit("onSingleLocation", requestCode, location);
                    requestCode = null;
                    onSingleLocation = OnLocation.Default;
                    stop();
                }
            }
        };

        //创建客户端
        LocationClient client = new LocationClient(CommonApplication.ctx);
        //创建客户端配置
        LocationClientOption option = new LocationClientOption();
        //设置定位方式
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //设置坐标系
        option.setCoorType("GCJ02");
        //设置定位间隔
        option.setScanSpan(locateInterval);
        //开启地址信息
        option.setIsNeedAddress(true);
        //开启方向信息
        option.setNeedDeviceDirect(true);
        //开启GPS
        option.setOpenGps(true);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);
        //设置缓存有效期
        option.setWifiCacheTimeOut(2000);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(false);
        //可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        option.setIsNeedAltitude(true);

        //设置定位选项
        client.setLocOption(option);
        //设置回调
        client.registerLocationListener(listener);
        //开启室内定位模式
        client.startIndoorMode();

        //创建前台服务通知
        BDLocationManager.notification = Notifications.buildForegroundNotification("正在为您定位", "这个服务保证软件能在后台继续获取位置信息");

        //启动定位程序
        BDLocationManager.client = client;
        BDLocationManager.option = option;
        BDLocationManager.listener = listener;

        //定时重启客户端
        WorkThread.postByLoop("#百度定位恢复线程", () -> {
            Threads.sleep(1000L);
            if (!locating)
                return;
            client.enableLocInForeground(foregroundServiceCode, notification);
            //连续无定位，说明定位服务可能被杀，重新启动客户端
            if (mode == LocateMode.Track) {
                boolean timeout = LaunchTime.time - BDLocationManager.lastLocateTime >= locateInterval * 3;
                if (timeout) {
                    showSignalLoseTip();
                    restart();
                }
            }
        });
    }

    //开始定位
    public static void start() {
        if (BDLocationManager.client == null)
            init();
        BDLocationManager.lastLocateTime = LaunchTime.time;
        locating = true;
        option.setScanSpan(locateInterval);
        client.setLocOption(BDLocationManager.option);
        client.registerLocationListener(listener);
        client.start();
    }

    //停止定位
    public static void stop() {
        if (BDLocationManager.client == null)
            return;
        locating = false;
        client.unRegisterLocationListener(listener);
        client.disableLocInForeground(true);
        client.stop();
        client = null;
    }

    //重新开始
    public static void restart() {
        if (client == null || !client.isStarted()) {
            start();
            return;
        }
        BDLocationManager.lastLocateTime = LaunchTime.time;
        locating = true;
        option.setScanSpan(locateInterval);
        client.setLocOption(BDLocationManager.option);
        client.registerLocationListener(listener);
        client.restart();
    }

    //提示信号丢失
    public static void showSignalLoseTip() {
        if (LaunchTime.time - lastVoiceTime > 10 * 1000L) {
            lastVoiceTime = LaunchTime.time;
            MediaManager.playAsset(CommonApplication.ctx, "locate/signal_lose.mp3");
        }
        EventBus.core.emit("onSignalLose");
    }

    public interface OnLocation {

        void onLocation(LBSLocation location);

        public static final OnLocation Default = location -> {
        };
    }
}

